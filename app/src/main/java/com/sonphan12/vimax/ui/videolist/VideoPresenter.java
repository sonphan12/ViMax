package com.sonphan12.vimax.ui.videolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.sonphan12.vimax.data.OfflineVideoRepository;
import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.ui.base.BaseFragment;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ApplyScheduler;

import java.io.File;
import java.util.List;


import io.reactivex.disposables.CompositeDisposable;

public class VideoPresenter implements VideoContract.Presenter {
    private VideoContract.View view;
    private OfflineVideoRepository offlineVideoRepository;
    private CompositeDisposable disposable;

    public VideoPresenter(OfflineVideoRepository offlineVideoRepository
            , CompositeDisposable compositeDisposable) {
        this.offlineVideoRepository = offlineVideoRepository;
        this.disposable = compositeDisposable;
    }


    @Override
    public void getVideos(Context ctx) {
        Bundle bundle = ((Fragment) view).getArguments();
        String albumName;
        if (bundle == null || (albumName = bundle.getString(AppConstants.EXTRA_ALBUM_NAME, null)) == null) {
            disposable.add(offlineVideoRepository.loadAll(ctx)
                    .compose(ApplyScheduler.applySchedulers())
                    .subscribe(videos -> {
                        view.hideProgressCircle();
                        view.showVideos(videos);
                    }, e -> view.showToastMessage(e.toString(), Toast.LENGTH_SHORT)));
        } else {
            disposable.add(offlineVideoRepository.loadFromAlbum(ctx, albumName)
                    .compose(ApplyScheduler.applySchedulers())
                    .subscribe(videos -> {
                        view.hideProgressCircle();
                        view.showVideos(videos);
                    }, e -> view.showToastMessage(e.toString(), Toast.LENGTH_SHORT)));
        }
    }

    @Override
    public void setCheckAll(List<Video> listVideo) {
        for (Video video : listVideo) {
            video.setChecked(true);
        }
        view.showVideos(listVideo);
    }

    @Override
    public void setUncheckAll(List<Video> listVideo) {
        for (Video video : listVideo) {
            video.setChecked(false);
        }
        view.showVideos(listVideo);
    }

    @Override
    public void returnToInitialState(VideoAdapter adapter) {
        for (Video video : adapter.getListVideo()) {
            video.setChecked(false);
        }
        adapter.setEnableAllCheckbox(false);
        adapter.notifyDataSetChanged();
        view.hideHiddenLayout();
        ((BaseFragment) view).setInitialState(true);
    }

    @Override
    public boolean enableAllCheckBox(VideoAdapter adapter, int position) {
        Video video = adapter.getListVideo().get(position);
        adapter.setEnableAllCheckbox(true);
        video.setChecked(true);
        adapter.notifyDataSetChanged();
        view.showHiddenLayout();
        ((BaseFragment)view).setInitialState(false);
        return false;
    }

    @Override
    public void deleteCheckedVideos(List<Video> listVideo) {
        int numVideoToDelete = 0;
        int numDeletedVideo = 0;
        for (Video video : listVideo) {
            if (video.isChecked()) {
                numVideoToDelete++;
                File file = new File(video.getFileSrc());
                if (file.exists()) {
//                    Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
//                    ((BaseFragment)view).getActivity().getContentResolver().delete(uri, null, null);
                    boolean isSuccess = file.delete();
                    if (isSuccess) {
                        numDeletedVideo++;
                    }
                }
            }
        }
        String deleteMessage = "Deleted " + String.valueOf(numDeletedVideo) + "/" +
                String.valueOf(numVideoToDelete) + " videos";
        view.showToastMessage(deleteMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void checkVideo(Video video) {
        video.setChecked(!video.isChecked());
    }


    @Override
    public void setView(VideoContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        disposable.dispose();
    }
}
