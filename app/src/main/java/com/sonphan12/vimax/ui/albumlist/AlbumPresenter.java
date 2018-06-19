package com.sonphan12.vimax.ui.albumlist;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sonphan12.vimax.data.OfflineVideoAlbumRepository;
import com.sonphan12.vimax.utils.ApplyScheduler;

import io.reactivex.disposables.CompositeDisposable;

public class AlbumPresenter implements AlbumContract.Presenter {
    AlbumContract.View view;
    OfflineVideoAlbumRepository offlineVideoAlbumRepository;
    CompositeDisposable disposable = new CompositeDisposable();

    public AlbumPresenter(AlbumContract.View view) {
        this.view = view;
        this.offlineVideoAlbumRepository = new OfflineVideoAlbumRepository();
    }

    @Override
    public void getAlbums(Context ctx) {
        disposable.add(offlineVideoAlbumRepository.loadAll(ctx)
                .compose(ApplyScheduler.applySchedulers())
                .subscribe(albums -> {
                    view.hideProgressCircle();
                    view.showAlbums(albums);
                }, e -> {
                    view.showToastMessage(e.toString(), Toast.LENGTH_SHORT);
                    Log.d("ERROR_GET_ALBUMS", e.toString());
                }));
    }

    @Override
    public void setView(AlbumContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        disposable.dispose();
    }
}
