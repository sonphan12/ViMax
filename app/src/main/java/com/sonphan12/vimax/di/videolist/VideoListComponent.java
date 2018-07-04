package com.sonphan12.vimax.di.videolist;

import com.sonphan12.vimax.ui.videolist.VideoFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {VideoListModule.class})
public interface VideoListComponent {
    void inject(VideoFragment fragment);
}
