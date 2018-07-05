package com.sonphan12.vimax.utils;

import android.Manifest;

import java.util.ArrayList;

public interface AppConstants {
    int PERMISSION_REQUEST_ALL = 1000;
    String[] PERMISSIONS =
            {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String EXTRA_VIDEO_PATH = "VIDEO_PATH";
    String EXTRA_ALBUM_NAME = "ALBUM_NAME";
    String FOLDER_NAME = "ViMax";
    String ROTATE_PROGRESS_MESSAGE = "Rotating video, please wait...";
    String REVERSE_PROGRESS_MESSAGE = "Reversing video, please wait...";
    String ACTION_UPDATE_DATA = "action_update_data";
}
