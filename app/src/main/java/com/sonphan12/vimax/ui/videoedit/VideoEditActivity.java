package com.sonphan12.vimax.ui.videoedit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoEditActivity extends AppCompatActivity implements VideoEditContract.View {
    @BindView(R.id.videoView) VideoView videoView;
    MediaController mediaController;
    VideoEditContract.Presenter presenter;
    public String videoPath;
    FFmpeg ffmpeg;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        ffmpeg = ((ViMaxApplication)getApplication()).getFfmpegInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        ButterKnife.bind(this);
        presenter = new VideoEditPresenter(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) videoPath = extras.getString(AppConstants.EXTRA_VIDEO_PATH, "");
        presenter.showVideoPreview();

    }

    @Override
    public void showVideoPreview() {
        if (videoPath == null || videoPath.isEmpty()) {
            return;
        } else {
            if (mediaController == null) {
                mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
            }
            try {
                videoView.setVideoPath(videoPath);
                // To show the first frame
                videoView.seekTo(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            videoView.requestFocus();
        }
    }

    @Override
    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    @Override
    public void cancelProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void showToastMessage(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    @OnClick({R.id.btnRotate})
    public void btnClicked(View v) {
        switch (v.getId()) {
            case R.id.btnRotate:
                presenter.onBtnRotateClicked(videoPath, ffmpeg);
                break;
        }
    }

}
