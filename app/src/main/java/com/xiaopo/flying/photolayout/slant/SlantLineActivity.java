package com.xiaopo.flying.photolayout.slant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.photolayout.PlaygroundActivity;
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
  private View btnMore;

  private List<Target> targets = new ArrayList<>();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slant_line);

    puzzleView = (SquareSlantPuzzleView) findViewById(R.id.puzzle_view);
    puzzleLayout = new SlantSampleLayout();
    puzzleView.reset();
    puzzleView.setPuzzleLayout(puzzleLayout);
    puzzleView.setNeedDrawLine(true);
    puzzleView.setNeedDrawOuterLine(true);

    btnMore = findViewById(R.id.btn_more);
    btnMore.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showMoreDialog(v);
      }
    });

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

  private void showMoreDialog(View view) {
    PopupMenu popupMenu = new PopupMenu(this, view, Gravity.BOTTOM);
    popupMenu.inflate(R.menu.menu_main);
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.action_playground:
            Intent intent = new Intent(SlantLineActivity.this, PlaygroundActivity.class);
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
}
