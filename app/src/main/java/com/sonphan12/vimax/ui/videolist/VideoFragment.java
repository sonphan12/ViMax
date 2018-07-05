package com.sonphan12.vimax.ui.videolist;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.ui.videoedit.VideoEditActivity;
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
public class VideoFragment extends BaseFragment implements VideoContract.View, VideoContract.VideoItemListener {
    @BindView(R.id.loadingProgress)
    ProgressBar loadingProgress;
    @BindView(R.id.lvVideos)
    RecyclerView lvVideos;
    @BindView(R.id.llHidden)
    LinearLayout llHidden;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnSelectAll)
    Button btnSelectAll;
    @BindView(R.id.btnBackToTop)
    Button btnBackToTop;
    VideoAdapter videoAdapter;
    @Inject
    VideoContract.Presenter presenter;
    @Inject
    LinearLayoutManager layoutManager;
    BroadcastReceiver receiver;

    public VideoFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video, container, false);

        ((ViMaxApplication) getActivity().getApplication()).createVideoListComponent().inject(this);

        ButterKnife.bind(this, v);

        videoAdapter = new VideoAdapter(getContext(), this);
        ItemClickSupport.addTo(lvVideos).setOnItemClickListener((recyclerView, position, v1) -> onVideoItemClick(v1, position));
        ItemClickSupport.addTo(lvVideos).setOnItemLongClickListener((recyclerView, position, v12) -> onVideoItemLongClick(v12, position));
        lvVideos.setLayoutManager(layoutManager);
        lvVideos.setAdapter(videoAdapter);

        presenter.setView(this);
        presenter.getVideos(getContext());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.ACTION_UPDATE_DATA);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.onReceiveAction(context, intent);
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        lvVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.onListScroll(
                        layoutManager.findLastVisibleItemPosition(),
                        videoAdapter.getItemCount(),
                        dx,
                        dy
                );
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getVideos(getContext());
    }

    @Override
    public void showVideos(List<Video> listVideo) {
        videoAdapter.setListVideo(listVideo);
        videoAdapter.notifyDataSetChanged();
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
        lvVideos.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void onVideoItemClick(View v, int position) {
        Intent intent = new Intent(getContext(), VideoEditActivity.class);
        intent.putExtra(AppConstants.EXTRA_VIDEO_PATH, videoAdapter.getListVideo().get(position).getFileSrc());
        getContext().startActivity(intent);
    }


    public boolean onVideoItemLongClick(View v, int position) {
        return presenter.enableAllCheckBox(videoAdapter, position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.returnToInitialState(videoAdapter);
    }

    @OnClick({R.id.btnDelete, R.id.btnSelectAll, R.id.btnBackToTop})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                presenter.deleteCheckedVideos(videoAdapter.getListVideo());
                break;
            case R.id.btnSelectAll:
                presenter.setCheckAll(videoAdapter.getListVideo());
                break;
            case R.id.btnBackToTop:
                presenter.onBtnBackOnTopClicked();
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        ((ViMaxApplication) getActivity().getApplication()).releaseVideoListComponent();
    }

    @Override
    public void onCheckClick(int position) {
        Video video = videoAdapter.getListVideo().get(position);
        presenter.checkVideo(video);
    }

}
