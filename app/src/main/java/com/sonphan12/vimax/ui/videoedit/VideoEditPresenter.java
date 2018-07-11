package com.sonphan12.vimax.ui.videoedit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.sonphan12.vimax.ui.videoedit.VideoEditContract.Presenter;
import com.sonphan12.vimax.utils.AppConstants;

import java.io.File;

public class VideoEditPresenter implements Presenter {
    private VideoEditContract.View view;
    File newFile;

    VideoEditPresenter(VideoEditContract.View view) {
        this.view = view;
    }

    @Override
    public void showVideoPreview() {
        view.showVideoPreview();
    }

    @Override
    public void onBtnRotateClicked(String videoUri, FFmpeg ffmpeg) {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        File viMaxDir = new File(moviesDir.getAbsolutePath() + File.separator + AppConstants.FOLDER_NAME);
        if (!viMaxDir.exists()) {
            viMaxDir.mkdirs();
        }
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
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        File viMaxDir = new File(moviesDir.getAbsolutePath() + File.separator + AppConstants.FOLDER_NAME);
        if (!viMaxDir.exists()) {
            viMaxDir.mkdirs();
        }
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
        // TODO: Impl change video speed
    }

    @Override
    public void executeFfmpegCommand(String[] command, FFmpeg ffmpeg, String progressMessage) {
        try {
            ffmpeg.execute(command, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {

                }

                @Override
                public void onProgress(String message) {

                }

                @Override
                public void onFailure(String message) {
                    view.showToastMessage("ERROR", Toast.LENGTH_SHORT);
                    Log.d("execute_error", message);
                }

                @Override
                public void onStart() {
                    view.showProgressDialog(progressMessage);
                }

                @Override
                public void onFinish() {
                    view.showToastMessage("Finish!", Toast.LENGTH_SHORT);
                    view.cancelProgressDialog();
                    ((Activity)view).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));
                    // Show new video preview
                    ((VideoEditActivity)view).videoPath = Uri.fromFile(newFile).toString();
                    view.showVideoPreview();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setView(VideoEditContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {

    }
}
