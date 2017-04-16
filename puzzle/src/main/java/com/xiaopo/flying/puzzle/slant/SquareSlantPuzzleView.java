package com.xiaopo.flying.puzzle.slant;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author wupanjie
 */

public class SquareSlantPuzzleView extends SlantPuzzleView {
  public SquareSlantPuzzleView(Context context) {
    super(context);
  }

  public SquareSlantPuzzleView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareSlantPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    int length = width > height ? height : width;

    setMeasuredDimension(length, length);
  }
}
