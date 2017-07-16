package com.xiaopo.flying.anotherlayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.anotherlayout.layout.straight.StraightLayoutHelper;
import com.xiaopo.flying.kits.SingleIOSwitch;
import com.xiaopo.flying.kits.WeakHandler;
import com.xiaopo.flying.poiphoto.PhotoManager;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeakHandler.IHandler,
    NavigationView.OnNavigationItemSelectedListener {

  private static final String TAG = "MainActivity";
  public static final int CODE_REQUEST_PERMISSION = 110;

  private RecyclerView photoList;
  private RecyclerView puzzleList;

  private PuzzleAdapter puzzleAdapter;
  private PhotoAdapter photoAdapter;

  private List<Bitmap> bitmaps = new ArrayList<>();
  private ArrayMap<String, Bitmap> arrayBitmaps = new ArrayMap<>();
  private ArrayList<String> selectedPath = new ArrayList<>();

  private WeakHandler puzzleHandler;

  private List<Target> targets = new ArrayList<>();

  private int deviceWidth;

  private CompositeDisposable compositeDisposables = new CompositeDisposable();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    puzzleHandler = new WeakHandler(this);

    deviceWidth = getResources().getDisplayMetrics().widthPixels;

    initView();

    AndPermission.with(this)
        .requestCode(CODE_REQUEST_PERMISSION)
        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        .callback(this)
        .start();
  }

  @PermissionYes(CODE_REQUEST_PERMISSION)
  private void getPermissionYes(List<String> grantedPermissions) {
    loadPhoto();
  }

  @PermissionNo(CODE_REQUEST_PERMISSION)
  private void getPermissionNo(List<String> deniedPermissions) {
    Toast.makeText(this, "必须要权限", Toast.LENGTH_SHORT).show();
  }

  private void loadPhoto() {
    Disposable disposable = Single.just(new PhotoManager(this))
        .map(PhotoManager::getAllPhoto)
        .compose(new SingleIOSwitch<>())
        .subscribe(photos -> {
          photoAdapter.refreshData(photos);
        });

    compositeDisposables.add(disposable);
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
    puzzleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    puzzleList.setHasFixedSize(true);

    puzzleAdapter.setOnItemClickListener(new PuzzleAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {
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

    photoAdapter.setOnPhotoSelectedListener((photo, position) -> {
      Message message = Message.obtain();
      message.what = 120;
      message.obj = photo.getPath();
      puzzleHandler.sendMessage(message);

      //prefetch the photo
      //noinspection SuspiciousNameCombination
      Picasso.with(MainActivity.this)
          .load("file:///" + photo.getPath())
          .resize(deviceWidth, deviceWidth)
          .centerInside()
          .memoryPolicy(MemoryPolicy.NO_CACHE)
          .fetch();
    });

    photoAdapter.setOnPhotoUnSelectedListener((photo, position) -> {
      Bitmap bitmap = arrayBitmaps.remove(photo.getPath());
      bitmaps.remove(bitmap);
      selectedPath.remove(photo.getPath());

      puzzleAdapter.refreshData(StraightLayoutHelper.getAllThemeLayout(bitmaps.size()), bitmaps);
    });

    photoAdapter.setOnSelectedMaxListener(() -> Toast.makeText(MainActivity.this, "装不下了～", Toast.LENGTH_SHORT).show());

    //ImageView btnCancel = (ImageView) findViewById(R.id.btn_cancel);
    //btnCancel.setOnClickListener(view -> {
    //  if (bitmaps == null || bitmaps.size() == 0) {
    //    onBackPressed();
    //    return;
    //  }
    //
    //  arrayBitmaps.clear();
    //  bitmaps.clear();
    //  selectedPath.clear();
    //
    //  photoAdapter.reset();
    //  puzzleHandler.sendEmptyMessage(119);
    //});

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //setSupportActionBar(toolbar);
    toolbar.inflateMenu(R.menu.menu_main);
    toolbar.setOnMenuItemClickListener(item -> {
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
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  private void showMoreDialog(View view) {
    PopupMenu popupMenu = new PopupMenu(this, view, Gravity.BOTTOM);
    popupMenu.inflate(R.menu.menu_main);
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
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

  @Override
  protected void onDestroy() {
    super.onDestroy();

    compositeDisposables.clear();
    compositeDisposables = null;

  }

  private void refreshLayout() {
    puzzleList.post(() -> puzzleAdapter.refreshData(PuzzleUtils.getPuzzleLayouts(bitmaps.size()), bitmaps));
  }

  public void fetchBitmap(final String path) {
    Log.d(TAG, "fetchBitmap: ");
    final Target target = new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Log.d(TAG, "onBitmapLoaded: ");

        arrayBitmaps.put(path, bitmap);
        bitmaps.add(bitmap);
        selectedPath.add(path);

        puzzleHandler.sendEmptyMessage(119);
        targets.remove(this);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {

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

  @Override
  public void handleMsg(Message msg) {
    if (msg.what == 119) {
      refreshLayout();
    } else if (msg.what == 120) {
      fetchBitmap((String) msg.obj);
    }
  }

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    return false;
  }
}
