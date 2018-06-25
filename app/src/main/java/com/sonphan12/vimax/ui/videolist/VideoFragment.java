package com.sonphan12.vimax.ui.videolist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.di.application.ApplicationModule;
import com.sonphan12.vimax.di.videolist.DaggerVideoListComponent;
import com.sonphan12.vimax.di.videolist.VideoListComponent;
import com.sonphan12.vimax.di.videolist.VideoListModule;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.ui.videoedit.VideoEditActivity;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ItemClickSupport;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment implements VideoContract.View {
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
    @BindView(R.id.lvVideos) RecyclerView lvVideos;
    @BindView(R.id.llHidden) LinearLayout llHidden;
    @BindView(R.id.btnDelete) Button btnDelete;
    @BindView(R.id.btnSelectAll) Button btnSelectAll;
    VideoAdapter videoAdapter;
    @Inject
    VideoContract.Presenter presenter;
    @Inject
    LinearLayoutManager layoutManager;

    public VideoFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video, container, false);

        inject();

        ButterKnife.bind(this, v);

        videoAdapter = new VideoAdapter(getContext());
        ItemClickSupport.addTo(lvVideos).setOnItemClickListener((recyclerView, position, v1) -> onVideoItemClick(v1, position));
        ItemClickSupport.addTo(lvVideos).setOnItemLongClickListener((recyclerView, position, v12) -> onVideoItemLongClick(v12, position));
        lvVideos.setLayoutManager(layoutManager);
        lvVideos.setAdapter(videoAdapter);

        presenter.setView(this);
        presenter.getVideos(getContext());

        // Inflate the layout for this fragment
        return v;
    }

    private void inject() {
        VideoListComponent component =
                DaggerVideoListComponent.builder().applicationModule(new ApplicationModule(getContext()))
                .videoListModule(new VideoListModule())
                .build();
        component.inject(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    public void onVideoItemClick(View v, int position) {
            Intent intent = new Intent(getContext(), VideoEditActivity.class);
            intent.putExtra(AppConstants.EXTRA_VIDEO_PATH, videoAdapter.getListVideo().get(position).getFileSrc());
            getContext().startActivity(intent);
    }


    public boolean onVideoItemLongClick(View v, int position) {
        Video video = videoAdapter.getListVideo().get(position);
        videoAdapter.setEnableAllCheckbox(true);
        video.setChecked(true);
        videoAdapter.notifyDataSetChanged();
        llHidden.setVisibility(View.VISIBLE);
        return false;
    }



}
