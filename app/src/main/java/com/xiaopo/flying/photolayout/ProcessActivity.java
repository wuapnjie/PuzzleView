package com.xiaopo.flying.photolayout;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private PuzzleLayout mPuzzleLayout;
    private List<String> mBitmapPaths;
    private PuzzleView mPuzzleView;

    private List<Target> mTargets = new ArrayList<>();
    private int mDeviceWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        mDeviceWidth = getResources().getDisplayMetrics().widthPixels;

        int borderSize = getIntent().getIntExtra("border_size", 0);
        int themeId = getIntent().getIntExtra("theme_id", 0);
        mBitmapPaths = getIntent().getStringArrayListExtra("photo_path");
        mPuzzleLayout = PuzzleUtil.getPuzzleLayout(borderSize, themeId);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhoto();
    }

    private void loadPhoto() {
        final List<Bitmap> pieces = new ArrayList<>();

        final int count = mBitmapPaths.size() > mPuzzleLayout.getBorderSize() ? mPuzzleLayout.getBorderSize() : mBitmapPaths.size();

        for (int i = 0; i < count; i++) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    pieces.add(bitmap);
                    if (pieces.size() == count) {
                        if (mBitmapPaths.size() == 1) {
                            for (int i = 0; i < mPuzzleLayout.getBorderSize(); i++) {
                                mPuzzleView.addPiece(bitmap);
                            }
                        } else {
                            mPuzzleView.addPieces(pieces);
                        }
                    }
                    mTargets.remove(this);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(this)
                    .load("file:///" + mBitmapPaths.get(i))
                    .resize(mDeviceWidth, mDeviceWidth)
                    .centerInside()
                    .config(Bitmap.Config.RGB_565)
                    .into(target);

            mTargets.add(target);
        }

    }

    private void initView() {
        ImageView btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPuzzleView = (PuzzleView) findViewById(R.id.puzzle_view);

        //the method we can use
        mPuzzleView.setPuzzleLayout(mPuzzleLayout);
        mPuzzleView.setMoveLineEnable(true);
        mPuzzleView.setNeedDrawBorder(false);
        mPuzzleView.setNeedDrawOuterBorder(false);
        mPuzzleView.setExtraSize(100);
        mPuzzleView.setBorderWidth(3);


        ImageView btnReplace = (ImageView) findViewById(R.id.btn_replace);
        ImageView btnFlipHorizontal = (ImageView) findViewById(R.id.btn_flip_horizontal);
        ImageView btnFlipVertical = (ImageView) findViewById(R.id.btn_flip_vertical);
        ImageView btnBorder = (ImageView) findViewById(R.id.btn_border);

        btnReplace.setOnClickListener(this);
        btnFlipHorizontal.setOnClickListener(this);
        btnFlipVertical.setOnClickListener(this);
        btnBorder.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_replace:
                break;
            case R.id.btn_flip_horizontal:
                break;
            case R.id.btn_flip_vertical:
                break;
            case R.id.btn_border:
                mPuzzleView.setNeedDrawBorder(!mPuzzleView.isNeedDrawBorder());
                break;
        }
    }
}
