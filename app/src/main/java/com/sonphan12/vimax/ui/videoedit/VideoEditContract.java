package com.sonphan12.vimax.ui.videoedit;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.sonphan12.vimax.ui.base.BasePresenter;
import com.sonphan12.vimax.ui.base.BaseView;

import java.io.File;

public interface VideoEditContract {
    interface View extends BaseView {
        void showVideoPreview();
        void showProgressDialog(String message);
        void updateProgressDialog(String progressMessage);
        void cancelProgressDialog();
        void createAndShowChangeSpeedDiaglog();
        void finishActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void showVideoPreview();
        void onBtnRotateClicked(String videoUri, FFmpeg ffmpeg);
        void onBtnReverseClicked(String videoUri, FFmpeg ffmpeg);
        void onBtnChangeSpeedClicked(String videoUri, FFmpeg ffmpeg);
        void changeVideoSpeed(String videoUri, FFmpeg ffmpeg, double speed);
        void executeFfmpegCommand(String[] command, FFmpeg ffmpeg, File output, String progressMessage);
        void onProgressCancel(FFmpeg ffmpeg);
    }
}
