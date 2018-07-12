package com.sonphan12.vimax.di.videoedit;

import com.sonphan12.vimax.ui.videoedit.VideoEditContract;
import com.sonphan12.vimax.ui.videoedit.VideoEditPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class VideoEditModule {
    VideoEditContract.View view;

    public VideoEditModule(VideoEditContract.View view) {
        this.view = view;
    }

    @Provides
    VideoEditContract.Presenter videoEditPresenter(CompositeDisposable compositeDisposable) {
        return new VideoEditPresenter(this.view, compositeDisposable);
    }
}
