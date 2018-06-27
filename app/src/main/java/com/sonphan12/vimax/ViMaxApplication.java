package com.sonphan12.vimax;

import android.app.Application;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.sonphan12.vimax.di.albumlist.AlbumListComponent;
import com.sonphan12.vimax.di.albumlist.AlbumListModule;
import com.sonphan12.vimax.di.application.ApplicationComponent;
import com.sonphan12.vimax.di.application.ApplicationModule;
import com.sonphan12.vimax.di.application.DataSourceModule;
import com.sonphan12.vimax.di.application.FfmpegModule;
import com.sonphan12.vimax.di.application.DaggerApplicationComponent;
import com.sonphan12.vimax.di.videolist.VideoListComponent;
import com.sonphan12.vimax.di.videolist.VideoListModule;

import javax.inject.Inject;

public class ViMaxApplication extends Application {
    ApplicationComponent applicationComponent;
    VideoListComponent videoListComponent;
    AlbumListComponent albumListComponent;
    @Inject
    FFmpeg ffmpeg;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = createAppComponent();
        applicationComponent.inject(this);
        loadFfmpegBinary();
    }

    private ApplicationComponent createAppComponent() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .ffmpegModule(new FfmpegModule())
                .dataSourceModule(new DataSourceModule())
                .build();
    }

    private void loadFfmpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "FFmpeg loaded successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Toast.makeText(getApplicationContext(), "FFmpeg not supported", Toast.LENGTH_SHORT).show();
        }
    }

    public FFmpeg getFfmpegInstance() {
        return this.ffmpeg;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public VideoListComponent createVideoListComponent() {
        videoListComponent = applicationComponent.plus(new VideoListModule());
        return videoListComponent;
    }

    public AlbumListComponent createAlbumListComponent() {
        albumListComponent = applicationComponent.plus(new AlbumListModule());
        return albumListComponent;
    }

    public void releaseVideoListComponent() {
        videoListComponent = null;
    }

    public void releaseAlbumListComponent() {
        albumListComponent = null;
    }
}