package com.sonphan12.vimax.ui.tabfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ui.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FolderFragment extends BaseFragment {


    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

}
