package com.sonphan12.vimax.ui.videolist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.sonphan12.vimax.data.OfflineVideoRepository;
import com.sonphan12.vimax.utils.AppConstants;
import com.sonphan12.vimax.utils.ApplyScheduler;
import io.reactivex.disposables.CompositeDisposable;

public class VideoPresenter implements VideoContract.Presenter {
    private VideoContract.View view;
    private OfflineVideoRepository offlineVideoRepository;
    CompositeDisposable disposable = new CompositeDisposable();

    public VideoPresenter(VideoContract.View view) {
        this.view = view;
        this.offlineVideoRepository = new OfflineVideoRepository();
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
    public void setView(VideoContract.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        disposable.dispose();
    }
}
