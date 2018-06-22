package com.sonphan12.vimax.di.application;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.di.FfmpegModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module(includes = FfmpegModule.class)
public class ApplicationModule {
    Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    Context applicationContext() {
        return context;
    }

    @Provides
    CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    LinearLayoutManager layoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
