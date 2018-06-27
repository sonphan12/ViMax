package com.sonphan12.vimax.di.application;

import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FfmpegModule {
    @Singleton
    @Provides
    FFmpeg fFmpeg(Context context) {
        return FFmpeg.getInstance(context);
    }
}
