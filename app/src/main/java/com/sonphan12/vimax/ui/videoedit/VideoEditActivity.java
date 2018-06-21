package com.sonphan12.vimax.ui.videoedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoEditActivity extends AppCompatActivity implements VideoEditContract.View {
    @BindView(R.id.videoView) VideoView videoView;
    MediaController mediaController;
    VideoEditContract.Presenter presenter;
    String videoPath;
    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        ffmpeg = ((ViMaxApplication)getApplication()).getFfmpegInstance();

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

}
