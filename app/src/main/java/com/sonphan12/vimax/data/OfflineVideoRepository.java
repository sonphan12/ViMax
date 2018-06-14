package com.sonphan12.vimax.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.utils.TimeConversion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfflineVideoRepository implements Loadable<Video> {
    @Override
    public List<Video> load(Context ctx) {
        ArrayList<Video> listVideo = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION};
        Cursor c = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                File videoFile = new File(c.getString(0));
                Video video = new Video(c.getString(0),
                        c.getString(1), TimeConversion.milisToFullTime(c.getString(2)));
                listVideo.add(video);
            }
            c.close();
        }
        return listVideo;
    }
}
