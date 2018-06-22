package com.sonphan12.vimax;

import android.app.Application;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.sonphan12.vimax.di.application.ApplicationComponent;
import com.sonphan12.vimax.di.application.ApplicationModule;
import com.sonphan12.vimax.di.FfmpegModule;
import com.sonphan12.vimax.di.application.DaggerApplicationComponent;

import javax.inject.Inject;

public class ViMaxApplication extends Application {
    @Inject
    FFmpeg ffmpeg;
    @Override
    public void onCreate() {
        super.onCreate();
        inject();
        loadFfmpegBinary();
    }

    private void inject() {
        ApplicationComponent component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .ffmpegModule(new FfmpegModule())
                .build();
        component.inject(this);
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
}