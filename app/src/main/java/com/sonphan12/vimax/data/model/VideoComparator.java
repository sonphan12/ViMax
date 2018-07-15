package com.sonphan12.vimax.data.model;

import android.text.TextUtils;

import java.util.Comparator;

public class VideoComparator implements Comparator<Video> {
    @Override
    public int compare(Video v1, Video v2) {
        if (TextUtils.isEmpty(v1.getName())) {
            if (!TextUtils.isEmpty(v2.getName())) {
                return -1;
            } else {
                return 0;
            }
        }
        if (TextUtils.isEmpty(v2.getName())) {
            if (!TextUtils.isEmpty(v1.getName())) {
                return 1;
            } else {
                return 0;
            }
        }
        return v1.getName().compareTo(v2.getName());
    }
}
