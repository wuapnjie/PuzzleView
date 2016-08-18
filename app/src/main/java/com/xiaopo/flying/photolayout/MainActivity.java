package com.xiaopo.flying.photolayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.poiphoto.PhotoPicker;
import com.xiaopo.flying.poiphoto.datatype.Photo;
import com.xiaopo.flying.poiphoto.ui.adapter.PhotoAdapter;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.layout.PuzzleLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPhotoList;
    private RecyclerView mPuzzleList;

    private PuzzleAdapter mPuzzleAdapter;

    private List<Bitmap> mBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    110);
        } else {
            loadPhoto();
        }

    }

    private void loadPhoto() {
        PhotoPicker.newInstance()
                .inflate(mPhotoList, new GridLayoutManager(this, 4));

        final PhotoAdapter photoAdapter = (PhotoAdapter) mPhotoList.getAdapter();
        photoAdapter.setMaxCount(9);

        photoAdapter.setOnPhotoSelectedListener(new PhotoAdapter.OnPhotoSelectedListener() {
            @Override
            public void onPhotoSelected(Photo photo, int position) {
                Picasso.with(MainActivity.this)
                        .load("file:///" + photo.getPath())
                        .resize(300, 300)
                        .config(Bitmap.Config.RGB_565)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mBitmaps.add(bitmap);
                                mPuzzleAdapter.refreshData(PuzzleLayoutHelper.getAllThemeLayout(mBitmaps.size()), mBitmaps);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
        });

        photoAdapter.setOnPhotoUnSelectedListener(new PhotoAdapter.OnPhotoUnSelectedListener() {
            @Override
            public void onPhotoUnSelected(Photo photo, int position) {

            }
        });
    }

    private void initView() {
        mPhotoList = (RecyclerView) findViewById(R.id.photo_list);
        mPuzzleList = (RecyclerView) findViewById(R.id.puzzle_list);

        mPuzzleAdapter = new PuzzleAdapter();
        mPuzzleList.setAdapter(mPuzzleAdapter);
        mPuzzleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPuzzleList.setHasFixedSize(true);


        mPuzzleAdapter.setOnItemClickListener(new PuzzleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            loadPhoto();
        }
    }
}
