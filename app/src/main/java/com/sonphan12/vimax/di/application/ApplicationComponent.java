package com.sonphan12.vimax.di.application;

import com.sonphan12.vimax.ViMaxApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ViMaxApplication application);
}
