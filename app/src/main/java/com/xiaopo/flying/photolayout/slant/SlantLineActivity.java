package com.xiaopo.flying.photolayout.slant;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.photolayout.R;
import com.xiaopo.flying.puzzle.slant.SlantLayout;
import com.xiaopo.flying.puzzle.slant.SquareSlantPuzzleView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class SlantLineActivity extends AppCompatActivity {
  private SquareSlantPuzzleView puzzleView;
  private SlantLayout puzzleLayout;

  private List<Target> targets = new ArrayList<>();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slant_line);

    puzzleView = (SquareSlantPuzzleView) findViewById(R.id.puzzle_view);
    puzzleLayout = new SlantSampleLayout();
    puzzleView.reset();
    puzzleView.setPuzzleLayout(puzzleLayout);

    loadPhotoFromRes();
  }

  private void loadPhotoFromRes() {
    final List<Bitmap> pieces = new ArrayList<>();

    final int[] resIds = new int[] {
        R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5,
        R.drawable.demo6, R.drawable.demo7, R.drawable.demo8, R.drawable.demo9,
    };

    final int count = resIds.length > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount()
        : resIds.length;

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
}
