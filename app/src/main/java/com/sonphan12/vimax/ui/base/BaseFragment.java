package com.sonphan12.vimax.ui.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment implements BaseView {
    public BaseFragment() {
        super();
    }

    @Override
    public void showToastMessage(String message, int length) {
        Toast.makeText(getContext(), message, length).show();
    }
}
