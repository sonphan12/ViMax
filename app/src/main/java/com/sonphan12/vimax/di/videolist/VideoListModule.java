package com.sonphan12.vimax.di.videolist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.sonphan12.vimax.data.OfflineVideoRepository;
import com.sonphan12.vimax.ui.videolist.VideoAdapter;
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

    @Provides
    OfflineVideoRepository offlineVideoRepository() {
        return new OfflineVideoRepository();
    }
}
