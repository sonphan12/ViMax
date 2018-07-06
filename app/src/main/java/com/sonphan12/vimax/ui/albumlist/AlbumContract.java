package com.sonphan12.vimax.ui.albumlist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragmentView;
import com.sonphan12.vimax.ui.base.BasePresenter;
import com.sonphan12.vimax.ui.videolist.VideoAdapter;

import java.util.List;

public interface AlbumContract {
    interface View extends BaseFragmentView {
        void showAlbums(List<Album> listAlbum);
        void showProgressCircle();
        void hideProgressCircle();
        void showBackOnTopButton();
        void hideBackOnTopButton();
        void scrollOnTop();
    }

    interface Presenter extends BasePresenter<View> {
        void getAlbums(Context ctx);
        void onReceiveAction(Context ctx, Intent intent);
        void onListScroll(int lastVisiblePosition, int listSize, int dx, int dy);
        void onBtnBackOnTopClicked();
        boolean enableAllCheckBox(AlbumAdapter adapter, int position);
        void returnToInitialState(AlbumAdapter adapter);
        void setCheckAll(List<Album> listAlbum);
        void setUncheckAll(List<Album> listAlbum);
    }
}
