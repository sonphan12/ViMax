package com.sonphan12.vimax.di.albumlist;


import com.sonphan12.vimax.data.OfflineVideoAlbumRepository;
import com.sonphan12.vimax.ui.albumlist.AlbumContract;
import com.sonphan12.vimax.ui.albumlist.AlbumPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class AlbumListModule {

    @Provides
    AlbumContract.Presenter albumPresenter(OfflineVideoAlbumRepository offlineVideoAlbumRepository,
                                           CompositeDisposable disposable) {
        return new AlbumPresenter(offlineVideoAlbumRepository, disposable);
    }

    @Provides
    OfflineVideoAlbumRepository offlineVideoAlbumRepository() {
        return new OfflineVideoAlbumRepository();
    }
}
