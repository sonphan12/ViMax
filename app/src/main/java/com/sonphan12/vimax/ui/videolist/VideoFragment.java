package com.sonphan12.vimax.ui.videolist;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.utils.ApplyScheduler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment implements VideoContract.View {
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
    @BindView(R.id.lvVideos) RecyclerView lvVideos;
    VideoAdapter videoAdapter;
    VideoContract.Presenter presenter;

    public VideoFragment() {
        // Required empty public constructor
    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.bind(this, v);

        videoAdapter = new VideoAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        lvVideos.setLayoutManager(layoutManager);
        lvVideos.setAdapter(videoAdapter);

        presenter = new VideoPresenter(this);
        presenter.getVideos(getContext());

        // Inflate the layout for this fragment
        return v;
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
}
