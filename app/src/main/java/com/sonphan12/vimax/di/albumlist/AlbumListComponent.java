package com.sonphan12.vimax.di.albumlist;

import com.sonphan12.vimax.di.application.ApplicationModule;
import com.sonphan12.vimax.ui.albumlist.AlbumFragment;

import dagger.Component;

@Component(modules = {ApplicationModule.class, AlbumListModule.class})
public interface AlbumListComponent {
    void inject(AlbumFragment fragment);
}
