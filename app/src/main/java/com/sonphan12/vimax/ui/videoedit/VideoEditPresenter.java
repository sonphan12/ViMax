package com.sonphan12.vimax.ui.videoedit;

import com.sonphan12.vimax.ui.videoedit.VideoEditContract.Presenter;

public class VideoEditPresenter implements Presenter {
    VideoEditContract.View view;

    VideoEditPresenter(VideoEditContract.View view) {
        this.view = view;
    }

    @Override
    public void showVideoPreview() {
        view.showVideoPreview();
    }
}
