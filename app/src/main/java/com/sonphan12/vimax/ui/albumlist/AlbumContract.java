package com.sonphan12.vimax.ui.albumlist;

import android.content.Context;
import android.content.Intent;

import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragmentView;
import com.sonphan12.vimax.ui.base.BasePresenter;
import java.util.List;

public interface AlbumContract {
    interface View extends BaseFragmentView {
        void showAlbums(List<Album> listAlbum);
        void showCurrentAlbumsWithOptions(String option);
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
        void checkAlbum(Album album);
        void deleteCheckedAlbums(List<Album> listAlbum);
        void searchAlbum(String query);
        void showCurrentAlbumsWithOptions(List<Album> listCurrentAlbums, String option);
    }

    interface AlbumItemListener {
        void onCheckClick(int position);
    }
}
