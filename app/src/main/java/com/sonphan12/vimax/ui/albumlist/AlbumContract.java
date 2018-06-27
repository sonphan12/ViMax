package com.sonphan12.vimax.ui.albumlist;

import android.content.Context;

import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragmentView;
import com.sonphan12.vimax.ui.base.BasePresenter;

import java.util.List;

public interface AlbumContract {
    interface View extends BaseFragmentView {
        void showAlbums(List<Album> listAlbum);
        void showProgressCircle();
        void hideProgressCircle();
    }

    interface Presenter extends BasePresenter<View> {
        void getAlbums(Context ctx);
    }
}
