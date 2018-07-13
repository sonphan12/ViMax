package com.sonphan12.vimax.ui.videoedit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ui.videoedit.VideoEditContract.Presenter;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ApplyScheduler;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VideoEditPresenter implements Presenter {
    private VideoEditContract.View view;
    private CompositeDisposable compositeDisposable;
    File newFile;

    public VideoEditPresenter(VideoEditContract.View view, CompositeDisposable compositeDisposable) {
        this.view = view;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void showVideoPreview() {
        view.showVideoPreview();
    }

    @Override
    public void onBtnRotateClicked(String videoUri, FFmpeg ffmpeg) {
        File viMaxDir = createVimaxDirIfNotExist();
        String inputPath = new File(videoUri).getAbsolutePath();
        File output = new File(viMaxDir.getAbsolutePath(), String.valueOf(System.currentTimeMillis()) + ".mp4");
        String outputPath = output.getAbsolutePath();
        String[] rotateCommand = {"-y", "-i", inputPath, "-vf", "transpose=1", "-vcodec", "mpeg4", "-c:a", "copy", "-c:v"
                , "libx264", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(rotateCommand, ffmpeg, AppConstants.ROTATE_PROGRESS_MESSAGE);

        newFile = output;
    }

    @Override
    public void onBtnReverseClicked(String videoUri, FFmpeg ffmpeg) {
        File viMaxDir = createVimaxDirIfNotExist();
        String inputPath = new File(videoUri).getAbsolutePath();
        File output = new File(viMaxDir.getAbsolutePath(), String.valueOf(System.currentTimeMillis()) + ".mp4");
        String outputPath = output.getAbsolutePath();
        String[] reverseCommand = {"-y", "-i", inputPath, "-vf", "reverse", "-af", "areverse", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(reverseCommand, ffmpeg, AppConstants.REVERSE_PROGRESS_MESSAGE);

        newFile = output;
    }

    @Override
    public void onBtnChangeSpeedClicked(String videoUri, FFmpeg ffmpeg) {
        view.createAndShowChangeSpeedDiaglog();
    }

    @Override
    public void changeVideoSpeed(String videoUri, FFmpeg ffmpeg, double speed) {
        File viMaxDir = createVimaxDirIfNotExist();
        String inputPath = new File(videoUri).getAbsolutePath();
        File output = new File(viMaxDir.getAbsolutePath(), String.valueOf(System.currentTimeMillis()) + ".mp4");
        String outputPath = output.getAbsolutePath();

        if (speed > 2) speed = 2;
        else if (speed < 0.5) speed = 0.5;

        String speedVideo = String.valueOf(1 / speed);
        String speedAudio = String.valueOf(speed);
        String[] changeSpeedCommand = {"-y", "-i", inputPath, "-filter_complex", "[0:v]setpts=" + speedVideo + "*PTS[v];[0:a]atempo=" + speedAudio + "[a]"
                , "-map", "[v]", "-map", "[a]", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(changeSpeedCommand, ffmpeg, AppConstants.CHANGE_SPEED_MESSAGE);

        newFile = output;
    }

    @Override
    public void executeFfmpegCommand(String[] command, FFmpeg ffmpeg, String progressMessage) {
        view.showProgressDialog(progressMessage);
        Disposable d = Completable.create(emitter -> {
            ffmpeg.execute(command, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                }

                @Override
                public void onProgress(String message) {
                }

                @Override
                public void onFailure(String message) {
                    emitter.onError(new Throwable(message));
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onFinish() {
                    emitter.onComplete();
                }
            });
        })
                .compose(ApplyScheduler.applySchedulersCompletableComputation())
                .subscribe(() -> {
                            view.showToastMessage(((VideoEditActivity)view).getString(R.string.finish), Toast.LENGTH_SHORT);
                            view.cancelProgressDialog();
                            ((Activity) view).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));
                            // Show new video preview
                            ((VideoEditActivity) view).videoPath = Uri.fromFile(newFile).toString();
                            view.showVideoPreview();
                        },
                        error -> {
                            Log.d("ERROR_FFMPEG", error.toString());
                            view.cancelProgressDialog();
                            view.showToastMessage(((VideoEditActivity) view).getString(R.string.executing_error), Toast.LENGTH_SHORT);
                        });
        compositeDisposable.add(d);
    }

    @Override
    public void onProgressCancel(FFmpeg ffmpeg) {
        if (ffmpeg.isFFmpegCommandRunning()) {
            ffmpeg.killRunningProcesses();
            compositeDisposable.clear();
            view.cancelProgressDialog();
            view.showToastMessage(((VideoEditActivity) view).getString(R.string.command_is_terminated), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void setView(VideoEditContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        compositeDisposable.dispose();
        this.view = null;
    }

    private File createVimaxDirIfNotExist() {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        File viMaxDir = new File(moviesDir.getAbsolutePath() + File.separator + AppConstants.FOLDER_NAME);
        if (!viMaxDir.exists()) {
            viMaxDir.mkdirs();
        }

        return viMaxDir;
    }
}
