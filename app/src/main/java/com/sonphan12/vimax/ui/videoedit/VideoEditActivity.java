package com.sonphan12.vimax.ui.videoedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sonphan12.vimax.R;
import com.sonphan12.vimax.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoEditActivity extends AppCompatActivity {
    @BindView(R.id.videoView) VideoView videoView;
    MediaController mediaController;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            videoPath = intent.getStringExtra(AppConstants.EXTRA_VIDEO_PATH);
        }

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
