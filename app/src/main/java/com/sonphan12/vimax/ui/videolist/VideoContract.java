package com.sonphan12.vimax.ui.videolist;

import android.content.Context;
import android.content.Intent;

import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BaseFragmentView;
import com.sonphan12.vimax.ui.base.BasePresenter;

import java.util.List;

import io.reactivex.Observable;

public interface VideoContract {
    interface View extends BaseFragmentView {
        void showVideos(List<Video> listVideo);
        void showProgressCircle();
        void hideProgressCircle();
        void showBackOnTopButton();
        void hideBackOnTopButton();
        void scrollOnTop();
    }

    interface Presenter extends BasePresenter<View> {
        void getVideos(Context ctx);
        void setCheckAll(List<Video> listVideo);
        void setUncheckAll(List<Video> listVideo);
        void returnToInitialState(VideoAdapter adapter);
        boolean enableAllCheckBox(VideoAdapter adapter, int position);
        void deleteCheckedVideos(List<Video> listVideo);
        void checkVideo(Video video);
        void onReceiveAction(Context context, Intent intent);
        void onListScroll(int lastVisiblePosition, int listSize, int dx, int dy);
        void onBtnBackOnTopClicked();
    }

    interface VideoItemListener {
        void onCheckClick(int position);
    }
}
