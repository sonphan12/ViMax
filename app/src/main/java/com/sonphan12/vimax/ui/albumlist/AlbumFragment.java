package com.sonphan12.vimax.ui.albumlist;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.Button;
import android.widget.LinearLayout;
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
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends BaseFragment implements AlbumContract.View {
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
    @BindView(R.id.lvAlbums) RecyclerView lvAlbums;
    @BindView(R.id.btnBackToTop) Button btnBackToTop;
    @BindView(R.id.llHidden) LinearLayout llHidden;

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

        lvAlbums.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onListScroll(
                        layoutManager.findLastVisibleItemPosition(),
                        albumAdapter.getItemCount(),
                        dx,
                        dy
                );
            }
        });

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
        return presenter.enableAllCheckBox(albumAdapter, position);
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
        llHidden.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHiddenLayout() {
        llHidden.setVisibility(View.GONE);
    }

    @Override
    public void showBackOnTopButton() {
        if (btnBackToTop.getVisibility() == View.GONE) {
            btnBackToTop.setVisibility(View.VISIBLE);
            btnBackToTop.setAlpha(0.0f);
            btnBackToTop
                    .animate()
                    .alpha(1.0f)
                    .setListener(null);
        }
    }

    @Override
    public void hideBackOnTopButton() {
        if (btnBackToTop.getVisibility() == View.VISIBLE) {
            btnBackToTop.animate()
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            btnBackToTop.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void scrollOnTop() {
        lvAlbums.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViMaxApplication)getActivity().getApplication()).releaseAlbumListComponent();
    }

    @OnClick ({R.id.btnDelete, R.id.btnSelectAll, R.id.btnClose, R.id.btnBackToTop})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                // TODO: Impl delete
                break;
            case R.id.btnSelectAll:
                presenter.setCheckAll(albumAdapter.getListAlbum());
                break;
            case R.id.btnClose:
                presenter.returnToInitialState(albumAdapter);
                break;
            case R.id.btnBackToTop:
                presenter.onBtnBackOnTopClicked();
                break;
        }
    }
}
