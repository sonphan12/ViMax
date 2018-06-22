package com.sonphan12.vimax.di.videolist;

import com.sonphan12.vimax.di.application.ApplicationModule;
import com.sonphan12.vimax.ui.videolist.VideoFragment;

import dagger.Component;

@Component(modules = {ApplicationModule.class, VideoListModule.class})
public interface VideoListComponent {
    void inject(VideoFragment fragment);
}
