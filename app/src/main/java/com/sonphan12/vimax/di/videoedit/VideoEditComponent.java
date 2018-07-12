package com.sonphan12.vimax.di.videoedit;

import com.sonphan12.vimax.ui.videoedit.VideoEditActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {VideoEditModule.class})
public interface VideoEditComponent {
    void inject(VideoEditActivity videoEditActivity);
}
