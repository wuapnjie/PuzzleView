package com.xiaopo.flying.poiphoto.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * square FrameLayout
 * @author wupanjie
 */
public class SquareFrameLayout extends FrameLayout {
  private final String TAG = SquareFrameLayout.class.getSimpleName();

  public SquareFrameLayout(Context context) {
    super(context);
  }

  public SquareFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();

    int size = width > height ? height : width;

    setMeasuredDimension(size, size);
  }
}
