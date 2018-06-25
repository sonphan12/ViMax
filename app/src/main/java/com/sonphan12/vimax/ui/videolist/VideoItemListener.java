package com.sonphan12.vimax.ui.videolist;

import android.view.View;

public interface VideoItemListener {
    interface OnVideoItemClick {
        void onVideoItemClick(View v, int position);
    }
    interface OnVideoItemLongClick {
        void onVideoItemLongClick(View v, int position);
    }
}
