package com.sonphan12.vimax.ui.albumlist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.ui.videolist.VideoFragment;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ItemClickSupport;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends BaseFragment implements AlbumContract.View {
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
    @BindView(R.id.lvAlbums) RecyclerView lvAlbums;
    AlbumAdapter albumAdapter;
    @Inject
    AlbumContract.Presenter presenter;
    @Inject
    LinearLayoutManager layoutManager;
    BroadcastReceiver receiver;
    public static final String FRAGMENT_TAG = "Album";

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);

        ((ViMaxApplication)getActivity().getApplication()).createAlbumListComponent().inject(this);

        ButterKnife.bind(this, v);
        albumAdapter = new AlbumAdapter(getContext());
        ItemClickSupport.addTo(lvAlbums).setOnItemClickListener((recyclerView, position, v1) -> onAlbumItemClick(v1, position));
        ItemClickSupport.addTo(lvAlbums).setOnItemLongClickListener((recyclerView, position, v12) -> onAlbumItemLongClick(v12, position));
        lvAlbums.setLayoutManager(layoutManager);
        lvAlbums.setAdapter(albumAdapter);

        presenter.setView(this);
        presenter.getAlbums(getContext());

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.onReceiveAction(context, intent);
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.ACTION_UPDATE_DATA);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        return v;
    }


    private void onAlbumItemClick(View v, int position) {
        // Switch to video fragment
        Album album = albumAdapter.getListAlbum().get(position);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        VideoFragment videoFragment = new VideoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.EXTRA_ALBUM_NAME, album.getName());
        videoFragment.setArguments(bundle);

        transaction.replace(R.id.fragmentDummy, videoFragment);
        transaction.addToBackStack(null);


        transaction.commit();
    }

    private boolean onAlbumItemLongClick(View v, int position) {
        // TODO: Impl On Item Long Click
        return false;
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
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }


    @Override
    public void showHiddenLayout() {
        // TODO: Impl show hidden layout
    }

    @Override
    public void hideHiddenLayout() {
        // TODO: Impl hide hidden layout
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViMaxApplication)getActivity().getApplication()).releaseAlbumListComponent();
    }
}
