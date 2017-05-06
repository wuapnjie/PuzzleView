package com.xiaopo.flying.photolayout.slant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.photolayout.PlaygroundActivity;
import com.xiaopo.flying.photolayout.R;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;
import com.xiaopo.flying.puzzle.SquarePuzzleView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class SlantLineActivity extends AppCompatActivity {
  private static final String TAG = "SlantLineActivity";
  private SquarePuzzleView puzzleView;
  private PuzzleLayout puzzleLayout;
  private View btnMore;

  private List<Target> targets = new ArrayList<>();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slant_line);

    puzzleView = (SquarePuzzleView) findViewById(R.id.puzzle_view);
    puzzleLayout = new SlantSampleLayout();
    puzzleView.reset();
    puzzleView.setPuzzleLayout(puzzleLayout);
    puzzleView.setNeedDrawLine(false);
    puzzleView.setNeedDrawOuterLine(false);
    puzzleView.setAnimateDuration(300);
    puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
      @Override public void onPieceSelected(PuzzlePiece piece, int position) {
        Toast.makeText(SlantLineActivity.this, "Piece " + position + " selected",
            Toast.LENGTH_SHORT).show();
      }
    });

    puzzleView.setPiecePadding(20);

    btnMore = findViewById(R.id.btn_more);
    btnMore.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showMoreDialog(v);
      }
    });

    puzzleView.post(new Runnable() {
      @Override public void run() {
        loadPhotoFromRes();
      }
    });
  }

  private void loadPhotoFromRes() {
    Log.d(TAG, "loadPhotoFromRes: ");
    final List<Bitmap> pieces = new ArrayList<>();

    final int[] resIds = new int[] {
        R.drawable.demo8, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5,
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
