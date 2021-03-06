package com.sonphan12.vimax.di.application;

import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.di.albumlist.AlbumListComponent;
import com.sonphan12.vimax.di.albumlist.AlbumListModule;
import com.sonphan12.vimax.di.videoedit.VideoEditComponent;
import com.sonphan12.vimax.di.videoedit.VideoEditModule;
import com.sonphan12.vimax.di.videolist.VideoListComponent;
import com.sonphan12.vimax.di.videolist.VideoListModule;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.disposables.CompositeDisposable;

@Singleton
@Component(modules = {ApplicationModule.class, FfmpegModule.class, DataSourceModule.class})
public interface ApplicationComponent {
    void inject(ViMaxApplication application);
    CompositeDisposable getCompositeDisposable();
    VideoListComponent plus(VideoListModule videoListModule);
    AlbumListComponent plus(AlbumListModule albumListModule);
    VideoEditComponent plus(VideoEditModule videoEditModule);
}
