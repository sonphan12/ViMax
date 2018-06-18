package com.sonphan12.vimax.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sonphan12.vimax.data.model.Album;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class OfflineVideoAlbumRepository implements Loadable<Album> {
    @Override
    public Observable<List<Album>> load(Context ctx) {
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
                    listAlbum.add(album);
                }
                c.close();
            }
            emitter.onNext(listAlbum);
            emitter.onComplete();
        });
    }
}
