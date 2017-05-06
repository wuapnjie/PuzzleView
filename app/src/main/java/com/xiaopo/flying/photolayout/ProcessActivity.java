package com.xiaopo.flying.photolayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoPicker;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

  private PuzzleLayout puzzleLayout;
  private List<String> bitmapPaint;
  private PuzzleView puzzleView;
  private DegreeSeekBar degreeSeekBar;

  private List<Target> targets = new ArrayList<>();
  private int deviceWidth = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_process);

    deviceWidth = getResources().getDisplayMetrics().widthPixels;

    int pieceSize = getIntent().getIntExtra("piece_size", 0);
    int themeId = getIntent().getIntExtra("theme_id", 0);
    bitmapPaint = getIntent().getStringArrayListExtra("photo_path");
    puzzleLayout = PuzzleUtil.getPuzzleLayout(pieceSize, themeId);

    initView();

    puzzleView.post(new Runnable() {
      @Override public void run() {
        loadPhoto();
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
  }

  private void loadPhoto() {
    if (bitmapPaint == null) {
      loadPhotoFromRes();
      return;
    }

    final List<Bitmap> pieces = new ArrayList<>();

    final int count = bitmapPaint.size() > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount()
        : bitmapPaint.size();

    for (int i = 0; i < count; i++) {
      final Target target = new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          pieces.add(bitmap);
          if (pieces.size() == count) {
            if (bitmapPaint.size() < puzzleLayout.getAreaCount()) {
              for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                puzzleView.addPiece(pieces.get(i % count));
              }
            } else {
              puzzleView.addPieces(pieces);
            }
          }
          targets.remove(this);
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      Picasso.with(this)
          .load("file:///" + bitmapPaint.get(i))
          .resize(deviceWidth, deviceWidth)
          .centerInside()
          .config(Bitmap.Config.RGB_565)
          .into(target);

      targets.add(target);
    }
  }

  private void loadPhotoFromRes() {
    final List<Bitmap> pieces = new ArrayList<>();

    final int[] resIds = new int[] {
        R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5,
        R.drawable.demo6, R.drawable.demo7, R.drawable.demo8, R.drawable.demo9,
    };

    final int count =
        resIds.length > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount() : resIds.length;

    for (int i = 0; i < count; i++) {
      final Target target = new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          pieces.add(bitmap);
          if (pieces.size() == count) {
            if (resIds.length < puzzleLayout.getAreaCount()) {
              for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                puzzleView.addPiece(pieces.get(i % count));
              }
            } else {
              puzzleView.addPieces(pieces);
            }
          }
          targets.remove(this);
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      Picasso.with(this).load(resIds[i]).config(Bitmap.Config.RGB_565).into(target);

      targets.add(target);
    }
  }

  private void initView() {
    ImageView btnBack = (ImageView) findViewById(R.id.btn_back);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        share();
      }
    });

    puzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
    degreeSeekBar = (DegreeSeekBar) findViewById(R.id.degree_seek_bar);

    //TODO the method we can use to change the puzzle view's properties
    puzzleView.setPuzzleLayout(puzzleLayout);
    puzzleView.setTouchEnable(true);
    puzzleView.setNeedDrawLine(false);
    puzzleView.setNeedDrawOuterLine(false);
    puzzleView.setLineSize(4);
    puzzleView.setLineColor(Color.BLACK);
    puzzleView.setSelectedLineColor(Color.BLACK);
    puzzleView.setHandleBarColor(Color.BLACK);
    puzzleView.setAnimateDuration(300);
    puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
      @Override public void onPieceSelected(PuzzlePiece piece, int position) {
        Toast.makeText(ProcessActivity.this, "Piece " + position + " selected",
            Toast.LENGTH_SHORT).show();
      }
    });
    puzzleView.setPiecePadding(10);

    ImageView btnReplace = (ImageView) findViewById(R.id.btn_replace);
    ImageView btnRotate = (ImageView) findViewById(R.id.btn_rotate);
    ImageView btnFlipHorizontal = (ImageView) findViewById(R.id.btn_flip_horizontal);
    ImageView btnFlipVertical = (ImageView) findViewById(R.id.btn_flip_vertical);
    ImageView btnBorder = (ImageView) findViewById(R.id.btn_border);

    btnReplace.setOnClickListener(this);
    btnRotate.setOnClickListener(this);
    btnFlipHorizontal.setOnClickListener(this);
    btnFlipVertical.setOnClickListener(this);
    btnBorder.setOnClickListener(this);

    TextView btnSave = (TextView) findViewById(R.id.btn_save);
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(final View view) {
        File file = FileUtil.getNewFile(ProcessActivity.this, "Puzzle");
        FileUtil.savePuzzle(puzzleView, file, 100, new Callback() {
          @Override public void onSuccess() {
            Snackbar.make(view, R.string.prompt_save_success, Snackbar.LENGTH_SHORT).show();
          }

          @Override public void onFailed() {
            Snackbar.make(view, R.string.prompt_save_failed, Snackbar.LENGTH_SHORT).show();
          }
        });
      }
    });

    degreeSeekBar.setCurrentDegrees(puzzleView.getLineSize());
    degreeSeekBar.setDegreeRange(0, 30);
    degreeSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
      @Override public void onScrollStart() {

      }

      @Override public void onScroll(int currentDegrees) {
        puzzleView.setLineSize(currentDegrees);
      }

      @Override public void onScrollEnd() {

      }
    });
  }

  private void share() {
    final File file = FileUtil.getNewFile(this, "Puzzle");

    FileUtil.savePuzzle(puzzleView, file, 100, new Callback() {
      @Override public void onSuccess() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(file);

        if (uri != null) {
          shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
          shareIntent.setType("image/*");
          startActivity(Intent.createChooser(shareIntent, getString(R.string.prompt_share)));
        }
      }

      @Override public void onFailed() {
        Snackbar.make(puzzleView, R.string.prompt_share_failed, Snackbar.LENGTH_SHORT).show();
      }
    });
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_replace:
        showSelectedPhotoDialog();
        break;
      case R.id.btn_rotate:
        puzzleView.rotate(90f);
        break;
      case R.id.btn_flip_horizontal:
        puzzleView.flipHorizontally();
        break;
      case R.id.btn_flip_vertical:
        puzzleView.flipVertically();
        break;
      case R.id.btn_border:
        puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine());
        if (puzzleView.isNeedDrawLine()) {
          degreeSeekBar.setVisibility(View.VISIBLE);
        } else {
          degreeSeekBar.setVisibility(View.INVISIBLE);
        }
        break;
    }
  }

  private void showSelectedPhotoDialog() {
    PhotoPicker.newInstance().setMaxCount(1).pick(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Define.DEFAULT_REQUEST_CODE && resultCode == RESULT_OK) {
      List<String> paths = data.getStringArrayListExtra(Define.PATHS);
      String path = paths.get(0);

      final Target target = new Target() {
        @Override public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
          puzzleView.replace(bitmap);
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {
          Snackbar.make(puzzleView, "Replace Failed!", Snackbar.LENGTH_SHORT).show();
        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      Picasso.with(this)
          .load("file:///" + path)
          .resize(deviceWidth, deviceWidth)
          .centerInside()
          .config(Bitmap.Config.RGB_565)
          .into(target);
    }
  }
}
