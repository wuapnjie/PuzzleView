package com.xiaopo.flying.poiphoto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.xiaopo.flying.poiphoto.datatype.Album;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Photo manager
 * @author wupanjie
 */
public class PhotoManager {
  private final String TAG = PhotoManager.class.getSimpleName();
  private ContentResolver mContentResolver;
  private List<String> mBucketIds;

  public PhotoManager(Context context) {
    mContentResolver = context.getContentResolver();
    mBucketIds = new ArrayList<>();
  }

  public List<Album> getAlbum() {
    mBucketIds.clear();

    List<Album> data = new ArrayList<>();
    String projects[] = new String[] {
        MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };
    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projects, null, null,
        MediaStore.Images.Media.DATE_MODIFIED);

    if (cursor != null && cursor.moveToFirst()) {
      do {
        Album album = new Album();

        String buckedId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

        if (mBucketIds.contains(buckedId)) continue;

        mBucketIds.add(buckedId);

        String buckedName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        String coverPath = getFrontCoverData(buckedId);

        album.setId(buckedId);
        album.setName(buckedName);
        album.setCoverPath(coverPath);

        data.add(album);
      } while (cursor.moveToNext());

      cursor.close();
    }

    return data;
  }

  public List<Photo> getPhoto(String buckedId) {
    List<Photo> photos = new ArrayList<>();

    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
        MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED
    }, MediaStore.Images.Media.BUCKET_ID + "=?", new String[] { buckedId }, MediaStore.Images.Media.DATE_MODIFIED);
    if (cursor != null && cursor.moveToFirst()) {
      do {

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

        Photo photo = new Photo(path, dataAdded, dataModified);

        photos.add(photo);
      } while (cursor.moveToNext());
      cursor.close();
    }

    return photos;
  }

  private String getFrontCoverData(String bucketId) {
    String path = "empty";
    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        new String[] { MediaStore.Images.Media.DATA }, MediaStore.Images.Media.BUCKET_ID + "=?",
        new String[] { bucketId }, MediaStore.Images.Media.DATE_MODIFIED);
    if (cursor != null && cursor.moveToFirst()) {
      path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
      cursor.close();
    }
    return path;
  }

  public List<Photo> getAllPhoto() {
    List<Photo> photos = new ArrayList<>();

    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
        MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED
    }, null, null, MediaStore.Images.Media.DATE_MODIFIED);
    if (cursor != null && cursor.moveToFirst()) {
      do {

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

        Photo photo = new Photo(path, dataAdded, dataModified);

        photos.add(photo);
      } while (cursor.moveToNext());
      cursor.close();
    }

    Collections.sort(photos, new Comparator<Photo>() {
      @Override
      public int compare(Photo lhs, Photo rhs) {
        long l = lhs.getDataModified();
        long r = rhs.getDataModified();
        return l > r ? -1 : (l == r ? 0 : 1);
      }
    });

    return photos;
  }
}
