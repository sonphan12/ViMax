package com.sonphan12.vimax.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class ApplyScheduler {
    public static <T> ObservableTransformer<T, T> applySchedulersObservable() {
            return observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer applySchedulersCompletable() {
        return completable -> completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
