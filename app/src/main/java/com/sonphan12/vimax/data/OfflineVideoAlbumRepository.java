package com.sonphan12.vimax.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sonphan12.vimax.data.model.Album;
import com.sonphan12.vimax.data.model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class OfflineVideoAlbumRepository{
    public Observable<List<Album>> loadAll(Context ctx) {
        return Observable.create(emitter -> {
            ArrayList<Album> listAlbum = new ArrayList<>();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Video.Media.ALBUM ,
                    "COUNT(*)"};
            String selection = "0 == 0)" + " GROUP BY (" + MediaStore.Video.Media.ALBUM;

            Cursor c = ctx.getContentResolver().query(uri, projection, selection, null, null);
            if (c != null) {
                while (c.moveToNext()) {
                    Album album = new Album(c.getString(0), Integer.parseInt(c.getString(1)));
                    if (album.getNumVideos() > 0) {
                        listAlbum.add(album);
                    }
                }
                c.close();
            }
            emitter.onNext(listAlbum);
            emitter.onComplete();
        });
    }

    public Completable deleteAlbum(Context context, Album album, OfflineVideoRepository offlineVideoRepository) {
        return offlineVideoRepository
                .loadFromAlbum(context, album.getName())
                .flatMapIterable(video -> video)
                .switchMap(video -> {
                    File fileVideo = new File(video.getFileSrc());
                    if (fileVideo.exists()) {
                        fileVideo.delete();
                        return offlineVideoRepository.deleteVideo(context, video.getId()).toObservable();
                    } else {
                        return null;
                    }
                })
                .ignoreElements();
    }
}
