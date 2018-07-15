package com.sonphan12.vimax.data.model;

import android.text.TextUtils;

import java.util.Comparator;

public class AlbumComparator implements Comparator<Album> {

    @Override
    public int compare(Album a1, Album a2) {
        if (TextUtils.isEmpty(a1.getName())) {
            if (!TextUtils.isEmpty(a2.getName())) {
                return -1;
            } else {
                return 0;
            }
        }
        if (TextUtils.isEmpty(a2.getName())) {
            if (!TextUtils.isEmpty(a1.getName())) {
                return 1;
            } else {
                return 0;
            }
        }
        return a1.getName().compareTo(a2.getName());
    }
}
