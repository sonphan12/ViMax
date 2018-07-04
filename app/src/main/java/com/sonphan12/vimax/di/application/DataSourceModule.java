package com.sonphan12.vimax.di.application;

import com.sonphan12.vimax.data.OfflineVideoAlbumRepository;
import com.sonphan12.vimax.data.OfflineVideoRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {
    @Singleton
    @Provides
    OfflineVideoRepository offlineVideoRepository() {
        return new OfflineVideoRepository();
    }

    @Singleton
    @Provides
    OfflineVideoAlbumRepository offlineVideoAlbumRepository() {
        return new OfflineVideoAlbumRepository();
    }
}
