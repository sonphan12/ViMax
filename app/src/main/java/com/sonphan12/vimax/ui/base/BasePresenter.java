package com.sonphan12.vimax.ui.base;

public interface BasePresenter <T extends BaseView> {
    void setView(T view);
}
