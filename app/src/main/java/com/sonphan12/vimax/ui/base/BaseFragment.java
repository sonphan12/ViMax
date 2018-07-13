package com.sonphan12.vimax.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sonphan12.vimax.R;

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
