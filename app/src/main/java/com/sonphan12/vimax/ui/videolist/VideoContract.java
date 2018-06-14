package com.sonphan12.vimax.ui.videolist;

import android.content.Context;

import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BasePresenter;
import com.sonphan12.vimax.ui.base.BaseView;

import java.util.List;

public interface VideoContract {
    interface View extends BaseView {
        void showVideos(List<Video> listVideo);
    }

    interface Presenter extends BasePresenter<View> {
        List<Video> getVideos(Context ctx);
    }
}
