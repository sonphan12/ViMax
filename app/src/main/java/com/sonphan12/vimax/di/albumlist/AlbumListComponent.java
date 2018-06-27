package com.sonphan12.vimax.di.albumlist;

import com.sonphan12.vimax.ui.albumlist.AlbumFragment;

import dagger.Subcomponent;

@Subcomponent(modules = AlbumListModule.class)
public interface AlbumListComponent {
    void inject(AlbumFragment fragment);
}
