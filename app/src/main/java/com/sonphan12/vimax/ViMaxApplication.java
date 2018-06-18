package com.sonphan12.vimax;

import android.app.Application;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class ViMaxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        loadFfmepgBinary();
    }

    private void loadFfmepgBinary() {
        FFmpeg ffmpeg = FFmpeg.getInstance(getApplicationContext());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "FFmpeg loaded successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Toast.makeText(getApplicationContext(), "FFmpeg not supported", Toast.LENGTH_SHORT).show();
        }
    }
}