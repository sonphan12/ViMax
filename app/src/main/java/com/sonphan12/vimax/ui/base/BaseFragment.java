package com.sonphan12.vimax.ui.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment implements BaseView {
    private boolean isInitialState;
    public BaseFragment() {
        super();
        isInitialState = true;
    }

    public boolean isInitialState() {
        return isInitialState;
    }

    public void setInitialState(boolean initialState) {
        isInitialState = initialState;
    }

    @Override
    public void showToastMessage(String message, int length) {
        Toast.makeText(getContext(), message, length).show();
    }

}
