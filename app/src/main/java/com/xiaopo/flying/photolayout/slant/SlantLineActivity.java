package com.xiaopo.flying.photolayout.slant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.xiaopo.flying.photolayout.R;
import com.xiaopo.flying.puzzle.slant.SquareSlantPuzzleView;

/**
 * @author wupanjie
 */

public class SlantLineActivity extends AppCompatActivity {
  private SquareSlantPuzzleView puzzleView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slant_line);

    Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.demo1);
    Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.demo2);

    puzzleView = (SquareSlantPuzzleView) findViewById(R.id.puzzle_view);
    puzzleView.setSlantLayout(new SlantTwoLayout());
  }
}
