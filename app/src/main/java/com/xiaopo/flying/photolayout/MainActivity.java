package com.xiaopo.flying.photolayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.poiphoto.GetAllPhotoTask;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.photolayout.layout.PuzzleLayoutHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private RecyclerView mPhotoList;
  private RecyclerView mPuzzleList;

  private PuzzleAdapter mPuzzleAdapter;
  private PhotoAdapter mPhotoAdapter;

  private List<Bitmap> mBitmaps = new ArrayList<>();
  private ArrayMap<String, Bitmap> mArrayBitmaps = new ArrayMap<>();
  private ArrayList<String> mSelectedPath = new ArrayList<>();

  private PuzzleHandler mPuzzleHandler;

  private List<Target> mTargets = new ArrayList<>();

  private int mDeviceWidth;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mPuzzleHandler = new PuzzleHandler(this);

    mDeviceWidth = getResources().getDisplayMetrics().widthPixels;

    initView();

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
      }, 110);
    } else {
      loadPhoto();
    }
  }

  private void loadPhoto() {

    new GetAllPhotoTask() {
      @Override protected void onPostExecute(List<Photo> photos) {
        super.onPostExecute(photos);
        mPhotoAdapter.refreshData(photos);
      }
    }.execute(new PhotoManager(this));
  }

  private void initView() {
    mPhotoList = (RecyclerView) findViewById(R.id.photo_list);
    mPuzzleList = (RecyclerView) findViewById(R.id.puzzle_list);

    mPhotoAdapter = new PhotoAdapter();
    mPhotoAdapter.setMaxCount(9);
    mPhotoAdapter.setSelectedResId(R.drawable.photo_selected_shadow);

    mPhotoList.setAdapter(mPhotoAdapter);
    mPhotoList.setLayoutManager(new GridLayoutManager(this, 4));

    mPuzzleAdapter = new PuzzleAdapter();
    mPuzzleList.setAdapter(mPuzzleAdapter);
    mPuzzleList.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mPuzzleList.setHasFixedSize(true);

    mPuzzleAdapter.setOnItemClickListener(new PuzzleAdapter.OnItemClickListener() {
      @Override public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {
        Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
        intent.putStringArrayListExtra("photo_path", mSelectedPath);
        intent.putExtra("piece_size", mSelectedPath.size());
        intent.putExtra("theme_id", themeId);

        startActivity(intent);
      }
    });

    mPhotoAdapter.setOnPhotoSelectedListener(new PhotoAdapter.OnPhotoSelectedListener() {
      @Override public void onPhotoSelected(final Photo photo, int position) {
        Message message = Message.obtain();
        message.what = 120;
        message.obj = photo.getPath();
        mPuzzleHandler.sendMessage(message);

        //prefetch the photo
        Picasso.with(MainActivity.this)
            .load("file:///" + photo.getPath())
            .resize(mDeviceWidth, mDeviceWidth)
            .centerInside()
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .fetch();
      }
    });

    mPhotoAdapter.setOnPhotoUnSelectedListener(new PhotoAdapter.OnPhotoUnSelectedListener() {
      @Override public void onPhotoUnSelected(Photo photo, int position) {
        Bitmap bitmap = mArrayBitmaps.remove(photo.getPath());
        mBitmaps.remove(bitmap);
        mSelectedPath.remove(photo.getPath());

        mPuzzleAdapter.refreshData(PuzzleLayoutHelper.getAllThemeLayout(mBitmaps.size()), mBitmaps);
      }
    });

    mPhotoAdapter.setOnSelectedMaxListener(new PhotoAdapter.OnSelectedMaxListener() {
      @Override public void onSelectedMax() {
        Toast.makeText(MainActivity.this, "装不下了～", Toast.LENGTH_SHORT).show();
      }
    });

    ImageView btnCancel = (ImageView) findViewById(R.id.btn_cancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mBitmaps == null || mBitmaps.size() == 0) {
          onBackPressed();
          return;
        }

        mArrayBitmaps.clear();
        mBitmaps.clear();
        mSelectedPath.clear();

        mPhotoAdapter.reset();
        mPuzzleHandler.sendEmptyMessage(119);
      }
    });

    ImageView btnMore = (ImageView) findViewById(R.id.btn_more);
    btnMore.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showMoreDialog(view);
      }
    });
  }

  private void showMoreDialog(View view) {
    PopupMenu popupMenu = new PopupMenu(this, view, Gravity.BOTTOM);
    popupMenu.inflate(R.menu.menu_main);
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.action_playground:
            Intent intent = new Intent(MainActivity.this, PlaygroundActivity.class);
            startActivity(intent);
            break;
          case R.id.action_about:
            showAboutInfo();
            break;
        }
        return false;
      }
    });
    popupMenu.show();
  }

  private void showAboutInfo() {
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
    bottomSheetDialog.setContentView(R.layout.about_info);
    bottomSheetDialog.show();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 110
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
      loadPhoto();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    mArrayBitmaps.clear();
    mArrayBitmaps = null;

    mBitmaps.clear();
    mBitmaps = null;
  }

  private void refreshLayout() {
    mPuzzleList.post(new Runnable() {
      @Override public void run() {
        mPuzzleAdapter.refreshData(PuzzleLayoutHelper.getAllThemeLayout(mBitmaps.size()), mBitmaps);
      }
    });
  }

  public void fetchBitmap(final String path) {
    Log.d(TAG, "fetchBitmap: ");
    final Target target = new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Log.d(TAG, "onBitmapLoaded: ");

        mArrayBitmaps.put(path, bitmap);
        mBitmaps.add(bitmap);
        mSelectedPath.add(path);

        mPuzzleHandler.sendEmptyMessage(119);
        mTargets.remove(this);
      }

      @Override public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    };

    Picasso.with(this)
        .load("file:///" + path)
        .resize(300, 300)
        .centerInside()
        .config(Bitmap.Config.RGB_565)
        .into(target);

    mTargets.add(target);
  }

  private static class PuzzleHandler extends Handler {
    private WeakReference<MainActivity> mReference;

    PuzzleHandler(MainActivity activity) {
      mReference = new WeakReference<>(activity);
    }

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.what == 119) {
        mReference.get().refreshLayout();
      } else if (msg.what == 120) {
        mReference.get().fetchBitmap((String) msg.obj);
      }
    }
  }
}
