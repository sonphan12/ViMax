package com.sonphan12.vimax.ui.videolist;

import android.content.Context;

import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BaseFragmentView;
import com.sonphan12.vimax.ui.base.BasePresenter;
import com.sonphan12.vimax.ui.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

public interface VideoContract {
    interface View extends BaseFragmentView {
        void showVideos(List<Video> listVideo);
        void showProgressCircle();
        void hideProgressCircle();
    }

    interface Presenter extends BasePresenter<View> {
        void getVideos(Context ctx);
    }
}
