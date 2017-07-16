package com.xiaopo.flying.poiphoto.ui.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaopo.flying.poiphoto.Define;

/**
 * @author wupanjie
 */
public class DividerLine extends RecyclerView.ItemDecoration {
  private final String TAG = DividerLine.class.getSimpleName();

  private Paint mPaint;

  @ColorInt private int mColor = Define.DEFAULT_DIVIDER_LINE_COLOR;

  private int mSize = Define.DEFAULT_DIVIDER_LINE_SIZE;

  public DividerLine() {
    mPaint = new Paint();
  }

  public void setPaint(Paint paint) {
    mPaint = paint;
  }

  public int getColor() {
    return mColor;
  }

  public void setColor(int color) {
    mColor = color;
  }

  public int getSize() {
    return mSize;
  }

  public void setSize(int size) {
    mSize = size;
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);

    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int top = child.getBottom() + params.bottomMargin;
      final int bottom = top + mSize;

      c.drawRect(left, top, right, bottom, mPaint);
    }
  }
}
