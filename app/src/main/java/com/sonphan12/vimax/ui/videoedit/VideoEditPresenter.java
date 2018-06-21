package com.sonphan12.vimax.ui.videoedit;

import android.app.Activity;
import android.content.Intent;

import com.sonphan12.vimax.ui.videoedit.VideoEditContract.Presenter;

public class VideoEditPresenter implements Presenter {
    VideoEditContract.View view;

    public VideoEditPresenter(VideoEditContract.View view) {
        this.view = view;
    }

    @Override
    public void showVideoPreview(String videoPath) {
        if (videoPath == null || videoPath.isEmpty()) {
            view.finishActivity();
        } else {
            view.showVideoPreview(videoPath);
        }
    }
}
