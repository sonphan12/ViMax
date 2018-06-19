package com.sonphan12.vimax.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sonphan12.vimax.data.model.Video;
import com.sonphan12.vimax.utils.TimeConversion;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class OfflineVideoRepository {

    public Observable<List<Video>> loadAll(Context ctx) {
        return Observable.create(emitter -> {
                    ArrayList<Video> listVideo = new ArrayList<>();
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] projection = {MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.TITLE,
                            MediaStore.Video.Media.DURATION};
                    Cursor c = ctx.getContentResolver().query(uri, projection, null, null, null);
                    if (c != null) {
                        while (c.moveToNext()) {
                            Video video = new Video(c.getString(0),
                                    c.getString(1), TimeConversion.milisToFullTime(c.getString(2)));
                            listVideo.add(video);
                        }
                        c.close();
                    }
                    emitter.onNext(listVideo);
                    emitter.onComplete();
                }
        );
    }

    public Observable<List<Video>> loadFromAlbum(Context ctx, String albumName) {
        return Observable.create(emitter -> {
                    ArrayList<Video> listVideo = new ArrayList<>();
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] projection = {MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.TITLE,
                            MediaStore.Video.Media.DURATION};
                    String selection = MediaStore.Video.Media.ALBUM +"=?";
                    String[] selectionArgs = {albumName};
                    Cursor c = ctx.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                    if (c != null) {
                        while (c.moveToNext()) {
                            Video video = new Video(c.getString(0),
                                    c.getString(1), TimeConversion.milisToFullTime(c.getString(2)));
                            listVideo.add(video);
                        }
                        c.close();
                    }
                    emitter.onNext(listVideo);
                    emitter.onComplete();
                }
        );
    }
}
