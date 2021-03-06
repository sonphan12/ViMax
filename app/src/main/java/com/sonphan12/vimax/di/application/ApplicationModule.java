package com.sonphan12.vimax.di.application;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
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
