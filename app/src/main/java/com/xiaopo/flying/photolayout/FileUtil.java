package com.xiaopo.flying.photolayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.xiaopo.flying.puzzle.PuzzleView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by snowbean on 16-8-5.
 */
public class FileUtil {
  private static final String TAG = "FileUtil";

  public static String getFolderName(String name) {
    File mediaStorageDir =
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            name);

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return "";
      }
    }

    return mediaStorageDir.getAbsolutePath();
  }

  private static boolean isSDAvailable() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  public static File getNewFile(Context context, String folderName) {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

    String timeStamp = simpleDateFormat.format(new Date());

    String path;
    if (isSDAvailable()) {
      path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
    } else {
      path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
    }

    if (TextUtils.isEmpty(path)) {
      return null;
    }

    return new File(path);
  }

  public static Bitmap createBitmap(PuzzleView puzzleView) {
    puzzleView.clearHandling();

    puzzleView.invalidate();

    Bitmap bitmap =
        Bitmap.createBitmap(puzzleView.getWidth(), puzzleView.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    puzzleView.draw(canvas);

    return bitmap;
  }

  public static void savePuzzle(PuzzleView puzzleView, File file, int quality, Callback callback) {
    Bitmap bitmap = null;
    FileOutputStream outputStream = null;

    try {
      bitmap = createBitmap(puzzleView);
      outputStream = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

      if (!file.exists()) {
        Log.e(TAG, "notifySystemGallery: the file do not exist.");
        return;
      }

      try {
        MediaStore.Images.Media.insertImage(puzzleView.getContext().getContentResolver(),
            file.getAbsolutePath(), file.getName(), null);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      puzzleView.getContext()
          .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

      if (callback != null) {
        callback.onSuccess();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      if (callback != null) {
        callback.onFailed();
      }
    } finally {
      if (bitmap != null) {
        bitmap.recycle();
      }

      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}