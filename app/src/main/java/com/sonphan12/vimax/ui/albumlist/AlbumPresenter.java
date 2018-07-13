package com.sonphan12.vimax.ui.albumlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.data.OfflineVideoAlbumRepository;
import com.sonphan12.vimax.data.OfflineVideoRepository;
import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ApplyScheduler;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class AlbumPresenter implements AlbumContract.Presenter {
    private AlbumContract.View view;
    private OfflineVideoAlbumRepository offlineVideoAlbumRepository;
    private OfflineVideoRepository offlineVideoRepository;
    private CompositeDisposable compositeDisposable;

    public AlbumPresenter(OfflineVideoAlbumRepository offlineVideoAlbumRepository,
                          OfflineVideoRepository offlineVideoRepository,
                          CompositeDisposable compositeDisposable) {
        this.offlineVideoAlbumRepository = offlineVideoAlbumRepository;
        this.offlineVideoRepository = offlineVideoRepository;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void getAlbums(Context ctx) {
        compositeDisposable.add(offlineVideoAlbumRepository.loadAll(ctx)
                .compose(ApplyScheduler.applySchedulersObservableIO())
                .subscribe(albums -> {
                    view.hideProgressCircle();
                    view.showAlbums(albums);
                }, e -> {
                    view.showToastMessage(e.toString(), Toast.LENGTH_SHORT);
                    Log.d("ERROR_GET_ALBUMS", e.toString());
                }));
    }

    @Override
    public void onReceiveAction(Context ctx, Intent intent) {
        switch (intent.getAction()) {
            case AppConstants.ACTION_UPDATE_DATA:
                getAlbums(ctx);
                break;
            case AppConstants.ACION_SEARCH:
                searchAlbum(intent.getStringExtra(AppConstants.EXTRA_SEARCH_QUERY));
                break;
            case AppConstants.ACTION_SORT_ASC:
                view.showCurrentAlbumsWithOptions(AppConstants.ACTION_SORT_ASC);
                break;
            case AppConstants.ACTION_SORT_DESC:
                view.showCurrentAlbumsWithOptions(AppConstants.ACTION_SORT_DESC);
                break;
        }
    }

    @Override
    public void setView(AlbumContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        compositeDisposable.dispose();
        this.view = null;
    }

    @Override
    public void onListScroll(int lastVisiblePosition, int listSize, int dx, int dy) {
        if (lastVisiblePosition >= 10) view.showBackOnTopButton();
        else view.hideBackOnTopButton();
    }

    @Override
    public void onBtnBackOnTopClicked() {
        view.scrollOnTop();
    }

    @Override
    public boolean enableAllCheckBox(AlbumAdapter adapter, int position) {
        Album album = adapter.getListAlbum().get(position);
        adapter.setEnableAllCheckbox(true);
        album.setChecked(true);
        adapter.notifyDataSetChanged();
        view.showHiddenLayout();
        ((BaseFragment) view).setInitialState(false);
        return false;
    }

    @Override
    public void returnToInitialState(AlbumAdapter adapter) {
        for (Album album : adapter.getListAlbum()) {
            album.setChecked(false);
        }
        adapter.setEnableAllCheckbox(false);
        adapter.notifyDataSetChanged();
        view.hideHiddenLayout();
        ((BaseFragment) view).setInitialState(true);
    }

    @Override
    public void setCheckAll(List<Album> listAlbum) {
        for (Album album : listAlbum) {
            album.setChecked(true);
        }
        view.showAlbums(listAlbum);
    }

    @Override
    public void setUncheckAll(List<Album> listAlbum) {
        for (Album album : listAlbum) {
            album.setChecked(false);
        }
        view.showAlbums(listAlbum);
    }

    @Override
    public void checkAlbum(Album album) {
        album.setChecked(!album.isChecked());
    }

    @Override
    public void deleteCheckedAlbums(List<Album> listAlbum) {
        for (Album album : listAlbum) {
            if (album.isChecked()) {
                compositeDisposable.add(offlineVideoAlbumRepository.deleteAlbum(
                        ((BaseFragment) view).getContext(),
                        album,
                        offlineVideoRepository
                )
                        .compose(ApplyScheduler.applySchedulersCompletableIO())
                        .subscribe(() -> {
                            view.showToastMessage("Deleted!", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(AppConstants.ACTION_UPDATE_DATA);
                            LocalBroadcastManager.getInstance(((BaseFragment) view).getContext()).sendBroadcast(intent);
                        }, Throwable::printStackTrace));
            }
        }
    }

    @Override
    public void searchAlbum(String query) {
        compositeDisposable.clear();
        Disposable d = Observable.just(query)
                .switchMap((Function<String, ObservableSource<List<Album>>>) q -> offlineVideoAlbumRepository.searchAlbum(((BaseFragment) view).getContext(), q))
                .compose(ApplyScheduler.applySchedulersObservableIO())
                .subscribe(
                        listAlbum -> view.showAlbums(listAlbum),
                        error -> view.showToastMessage(((BaseFragment) view).getContext().getString(R.string.error), Toast.LENGTH_SHORT));
        compositeDisposable.add(d);
    }

    @Override
    public void showCurrentAlbumsWithOptions(List<Album> listCurrentAlbums, String option) {
        compositeDisposable.clear();

        Disposable d = Completable.create(emitter -> {
                    if (option.equals(AppConstants.ACTION_SORT_ASC)) {
                        Collections.sort(listCurrentAlbums, this::compareAlbum);
                    } else {
                        Collections.sort(listCurrentAlbums, (a1, a2) -> -compareAlbum(a1, a2));
                    }
                    emitter.onComplete();
                }
        )
                .compose(ApplyScheduler.applySchedulersCompletableComputation())
                .subscribe(
                        () -> view.showAlbums(listCurrentAlbums),
                        error -> view.showToastMessage(((BaseFragment)view).getString(R.string.error), Toast.LENGTH_SHORT)
                );

        compositeDisposable.add(d);
    }

    private int compareAlbum(Album a1, Album a2) {
        if (TextUtils.isEmpty(a1.getName())) {
            if (!TextUtils.isEmpty(a2.getName())) {
                return -1;
            } else {
                return 0;
            }
        }
        if (TextUtils.isEmpty(a2.getName())) {
            if (!TextUtils.isEmpty(a1.getName())) {
                return 1;
            } else {
                return 0;
            }
        }
        return a1.getName().compareTo(a2.getName());
    }
}
