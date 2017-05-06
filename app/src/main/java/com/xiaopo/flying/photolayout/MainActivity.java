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
import com.xiaopo.flying.photolayout.layout.straight.StraightLayoutHelper;
import com.xiaopo.flying.poiphoto.GetAllPhotoTask;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private RecyclerView photoList;
  private RecyclerView puzzleList;

  private PuzzleAdapter puzzleAdapter;
  private PhotoAdapter photoAdapter;

  private List<Bitmap> bitmaps = new ArrayList<>();
  private ArrayMap<String, Bitmap> arrayBitmaps = new ArrayMap<>();
  private ArrayList<String> selectedPath = new ArrayList<>();

  private PuzzleHandler puzzleHandler;

  private List<Target> targets = new ArrayList<>();

  private int deviceWidth;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    puzzleHandler = new PuzzleHandler(this);

    deviceWidth = getResources().getDisplayMetrics().widthPixels;

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
        photoAdapter.refreshData(photos);
      }
    }.execute(new PhotoManager(this));
  }

  private void initView() {
    photoList = (RecyclerView) findViewById(R.id.photo_list);
    puzzleList = (RecyclerView) findViewById(R.id.puzzle_list);

    photoAdapter = new PhotoAdapter();
    photoAdapter.setMaxCount(9);
    photoAdapter.setSelectedResId(R.drawable.photo_selected_shadow);

    photoList.setAdapter(photoAdapter);
    photoList.setLayoutManager(new GridLayoutManager(this, 4));

    puzzleAdapter = new PuzzleAdapter();
    puzzleList.setAdapter(puzzleAdapter);
    puzzleList.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    puzzleList.setHasFixedSize(true);

    puzzleAdapter.setOnItemClickListener(new PuzzleAdapter.OnItemClickListener() {
      @Override public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {
        Intent intent = new Intent(MainActivity.this, ProcessActivity.class);
        intent.putStringArrayListExtra("photo_path", selectedPath);
        if (puzzleLayout instanceof SlantPuzzleLayout) {
          intent.putExtra("type", 0);
        } else {
          intent.putExtra("type", 1);
        }
        intent.putExtra("piece_size", selectedPath.size());
        intent.putExtra("theme_id", themeId);

        startActivity(intent);
      }
    });

    photoAdapter.setOnPhotoSelectedListener(new PhotoAdapter.OnPhotoSelectedListener() {
      @Override public void onPhotoSelected(final Photo photo, int position) {
        Message message = Message.obtain();
        message.what = 120;
        message.obj = photo.getPath();
        puzzleHandler.sendMessage(message);

        //prefetch the photo
        Picasso.with(MainActivity.this)
            .load("file:///" + photo.getPath())
            .resize(deviceWidth, deviceWidth)
            .centerInside()
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .fetch();
      }
    });

    photoAdapter.setOnPhotoUnSelectedListener(new PhotoAdapter.OnPhotoUnSelectedListener() {
      @Override public void onPhotoUnSelected(Photo photo, int position) {
        Bitmap bitmap = arrayBitmaps.remove(photo.getPath());
        bitmaps.remove(bitmap);
        selectedPath.remove(photo.getPath());

        puzzleAdapter.refreshData(StraightLayoutHelper.getAllThemeLayout(bitmaps.size()), bitmaps);
      }
    });

    photoAdapter.setOnSelectedMaxListener(new PhotoAdapter.OnSelectedMaxListener() {
      @Override public void onSelectedMax() {
        Toast.makeText(MainActivity.this, "装不下了～", Toast.LENGTH_SHORT).show();
      }
    });

    ImageView btnCancel = (ImageView) findViewById(R.id.btn_cancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (bitmaps == null || bitmaps.size() == 0) {
          onBackPressed();
          return;
        }

        arrayBitmaps.clear();
        bitmaps.clear();
        selectedPath.clear();

        photoAdapter.reset();
        puzzleHandler.sendEmptyMessage(119);
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

    arrayBitmaps.clear();
    arrayBitmaps = null;

    bitmaps.clear();
    bitmaps = null;
  }

  private void refreshLayout() {
    puzzleList.post(new Runnable() {
      @Override public void run() {
        puzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(bitmaps.size()), bitmaps);
      }
    });
  }

  public void fetchBitmap(final String path) {
    Log.d(TAG, "fetchBitmap: ");
    final Target target = new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Log.d(TAG, "onBitmapLoaded: ");

        arrayBitmaps.put(path, bitmap);
        bitmaps.add(bitmap);
        selectedPath.add(path);

        puzzleHandler.sendEmptyMessage(119);
        targets.remove(this);
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

    targets.add(target);
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
