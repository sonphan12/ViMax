package com.sonphan12.vimax.ui.videoedit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
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
import io.reactivex.Observable;
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
        File output = new File(viMaxDir.getAbsolutePath(), String.format("ViMax_%d.mp4", System.currentTimeMillis()));
        String outputPath = output.getAbsolutePath();
        String[] rotateCommand = {"-y", "-i", inputPath, "-vf", "transpose=1", "-vcodec", "mpeg4", "-c:a", "copy", "-c:v"
                , "libx264", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(rotateCommand, ffmpeg, output, AppConstants.ROTATE_PROGRESS_MESSAGE);
    }

    @Override
    public void onBtnReverseClicked(String videoUri, FFmpeg ffmpeg) {
        File viMaxDir = createVimaxDirIfNotExist();
        String inputPath = new File(videoUri).getAbsolutePath();
        File output = new File(viMaxDir.getAbsolutePath(), String.format("ViMax_%d.mp4", System.currentTimeMillis()));
        String outputPath = output.getAbsolutePath();
        String[] reverseCommand = {"-y", "-i", inputPath, "-vf", "reverse", "-af", "areverse", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(reverseCommand, ffmpeg, output, AppConstants.REVERSE_PROGRESS_MESSAGE);
    }

    @Override
    public void onBtnChangeSpeedClicked(String videoUri, FFmpeg ffmpeg) {
        view.createAndShowChangeSpeedDiaglog();
    }

    @Override
    public void changeVideoSpeed(String videoUri, FFmpeg ffmpeg, double speed) {
        File viMaxDir = createVimaxDirIfNotExist();
        String inputPath = new File(videoUri).getAbsolutePath();
        File output = new File(viMaxDir.getAbsolutePath(), String.format("ViMax_%d.mp4", System.currentTimeMillis()));
        String outputPath = output.getAbsolutePath();

        if (speed > 2) speed = 2;
        else if (speed < 0.5) speed = 0.5;

        String speedVideo = String.valueOf(1 / speed);
        String speedAudio = String.valueOf(speed);
        String[] changeSpeedCommand = {"-y", "-i", inputPath, "-filter_complex", "[0:v]setpts=" + speedVideo + "*PTS[v];[0:a]atempo=" + speedAudio + "[a]"
                , "-map", "[v]", "-map", "[a]", "-preset", "ultrafast", outputPath};
        executeFfmpegCommand(changeSpeedCommand, ffmpeg, output, AppConstants.CHANGE_SPEED_MESSAGE);
    }

    @Override
    public void executeFfmpegCommand(String[] command, FFmpeg ffmpeg, File output, String progressMessage) {
        Disposable d = Observable.create(emitter -> ffmpeg.execute(command, new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onProgress(String message) {
                Log.d("FFMPEG", message);
                String progress;
                try {
                    progress = message.substring(message.indexOf("size"), message.indexOf("time") - 1);
                } catch (Exception ex) {
                    progress = "";
                }
                emitter.onNext(progress);
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
                newFile = output;
                emitter.onComplete();
            }
        }))
                .compose(ApplyScheduler.applySchedulersObservableComputation())
                .doOnSubscribe(__ -> view.showProgressDialog(progressMessage))
                .doOnTerminate(() -> view.cancelProgressDialog())
                .subscribe(
                        progress -> {
                            if (!TextUtils.isEmpty((String) progress)) {
                                view.updateProgressDialog(progressMessage + "\n" + progress);
                            }
                        },
                        error -> {
                            Log.d("ERROR_FFMPEG", error.toString());
                            view.showToastMessage(((VideoEditActivity) view).getString(R.string.executing_error), Toast.LENGTH_SHORT);
                        },
                        () -> {
                            view.showToastMessage(((VideoEditActivity) view).getString(R.string.finish), Toast.LENGTH_SHORT);
                            ((Activity) view).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));
                            // Show new video preview
                            ((VideoEditActivity) view).videoPath = newFile.getPath();
                            view.showVideoPreview();
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
