package com.sonphan12.vimax.ui.albumlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends BaseFragment implements AlbumContract.View {
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
    @BindView(R.id.lvAlbums) RecyclerView lvAlbums;

    AlbumAdapter albumAdapter;
    AlbumPresenter presenter;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);
        ButterKnife.bind(this, v);
        albumAdapter = new AlbumAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        lvAlbums.setLayoutManager(layoutManager);
        lvAlbums.setAdapter(albumAdapter);

        presenter = new AlbumPresenter(this);
        presenter.getAlbums(getContext());

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getAlbums(getContext());
    }

    @Override
    public void showAlbums(List<Album> listAlbum) {
        albumAdapter.setListAlbum(listAlbum);
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressCircle() {
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressCircle() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }
}
