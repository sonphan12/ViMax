package com.sonphan12.vimax.ui.videoedit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.utils.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoEditActivity extends AppCompatActivity implements VideoEditContract.View {
    @BindView(R.id.videoView) VideoView videoView;
    MediaController mediaController;
    @Inject
    VideoEditContract.Presenter presenter;
    public String videoPath;
    FFmpeg ffmpeg;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);

        ((ViMaxApplication)getApplication()).createVideoEditComponent(this).inject(this);

        ffmpeg = ((ViMaxApplication)getApplication()).getFfmpegInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(dialogInterface -> presenter.onProgressCancel(ffmpeg));

        ButterKnife.bind(this);
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
    public void createAndShowChangeSpeedDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.change_speed));
        View view = LayoutInflater.from(this).inflate(R.layout.changespeed_dialog, null);
        builder.setView(view);

        EditText edtSpeed = view.findViewById(R.id.edtSpeed);
        SeekBar sbSpeed = view.findViewById(R.id.sbSpeed);

        // edtSpeed change dynamically with the progress bar
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double dSpeed = Double.parseDouble(s.toString());
                    if (dSpeed < 0.5) {
                        dSpeed = 0.5;
                        edtSpeed.setText(String.valueOf(dSpeed));
                        return;
                    } else if (dSpeed > 2) {
                        dSpeed = 2.0;
                        edtSpeed.setText(String.valueOf(dSpeed));
                        return;
                    }
                    sbSpeed.setProgress((int)(dSpeed * 100 / 2));
                } catch (Exception e) {
                    edtSpeed.setText("0.5");
                    Log.d(this.getClass().getSimpleName(), e.toString());
                }
            }
        };

        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress < seekBar.getMax() / 4) {
                        seekBar.setProgress(seekBar.getMax() / 4);
                        return;
                    }
                    double dSpeed = ((double) progress) / 100 * 2;
                    String sSpeed = String.valueOf(((double) Math.round(dSpeed * 100)) / 100);
                    edtSpeed.removeTextChangedListener(textWatcher);
                    edtSpeed.setText(sSpeed);
                    edtSpeed.addTextChangedListener(textWatcher);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        edtSpeed.addTextChangedListener(textWatcher);

        builder.setPositiveButton(R.string.OK, (dialog, which) -> {
            String sSpeed = edtSpeed.getText().toString();
            double dSpeed;
            try {
                dSpeed = Double.parseDouble(sSpeed);
                presenter.changeVideoSpeed(videoPath, ffmpeg, dSpeed);
            } catch (Exception e) {
                Log.d(this.getClass().getSimpleName(), e.toString());
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            // DO NOTHING
        });

        builder.show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showToastMessage(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    @OnClick({R.id.btnRotate, R.id.btnReverse, R.id.btnChangeSpeed})
    public void btnClicked(View v) {
        switch (v.getId()) {
            case R.id.btnRotate:
                presenter.onBtnRotateClicked(videoPath, ffmpeg);
                break;
            case R.id.btnReverse:
                presenter.onBtnReverseClicked(videoPath, ffmpeg);
                break;
            case R.id.btnChangeSpeed:
                presenter.onBtnChangeSpeedClicked(videoPath, ffmpeg);
                break;
        }
    }
}
