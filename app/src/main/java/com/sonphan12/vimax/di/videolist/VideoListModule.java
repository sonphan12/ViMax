package com.sonphan12.vimax.di.videolist;

import com.sonphan12.vimax.data.OfflineVideoRepository;
import com.sonphan12.vimax.ui.videolist.VideoContract;
import com.sonphan12.vimax.ui.videolist.VideoPresenter;


import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class VideoListModule {

    @Provides
    VideoContract.Presenter videoPresenter(OfflineVideoRepository offlineVideoRepository
            , CompositeDisposable compositeDisposable) {
        return new VideoPresenter(offlineVideoRepository, compositeDisposable);
    }
}
