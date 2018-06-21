package com.sonphan12.vimax.ui.videoedit;

public interface VideoEditContract {
    interface View {
        void showVideoPreview(String videoPath);
        void finishActivity();
    }

    interface Presenter {
        void showVideoPreview(String videoPath);
    }
}
