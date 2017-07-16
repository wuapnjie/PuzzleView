package com.xiaopo.flying.anotherlayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout;

/**
 * @author wupanjie
 */
public class PlaygroundActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playground);

    initView();

    prefetchResPhoto();
  }

  private void prefetchResPhoto() {
    final int[] resIds = new int[] {
        R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5,
        R.drawable.demo6, R.drawable.demo7, R.drawable.demo8, R.drawable.demo9,
    };

    for (int resId : resIds) {
      Picasso.with(this)
          .load(resId)
          .memoryPolicy(MemoryPolicy.NO_CACHE)
          .config(Bitmap.Config.RGB_565)
          .fetch();
    }
  }

  private void initView() {
    final RecyclerView puzzleList = (RecyclerView) findViewById(R.id.puzzle_list);
    puzzleList.setLayoutManager(new GridLayoutManager(this, 2));

    PuzzleAdapter puzzleAdapter = new PuzzleAdapter();

    puzzleList.setAdapter(puzzleAdapter);

    puzzleAdapter.refreshData(PuzzleUtils.getAllPuzzleLayouts(), null);

    puzzleAdapter.setOnItemClickListener(new PuzzleAdapter.OnItemClickListener() {
      @Override public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {
        Intent intent = new Intent(PlaygroundActivity.this, ProcessActivity.class);
        if (puzzleLayout instanceof SlantPuzzleLayout) {
          intent.putExtra("type", 0);
        } else {
          intent.putExtra("type", 1);
        }
        intent.putExtra("piece_size", puzzleLayout.getAreaCount());
        intent.putExtra("theme_id", themeId);

        startActivity(intent);
      }
    });

    ImageView btnClose = (ImageView) findViewById(R.id.btn_cancel);
    btnClose.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });
  }
}
