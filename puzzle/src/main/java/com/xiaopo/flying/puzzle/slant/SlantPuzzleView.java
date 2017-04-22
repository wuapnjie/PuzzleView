package com.xiaopo.flying.puzzle.slant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author wupanjie
 */

public class SlantPuzzleView extends View {
  private static final String TAG = "SlantPuzzleView";
  private SlantLayout slantLayout;
  private RectF bounds;

  private Paint linePaint;
  private int lineSize = 4;

  private Line handlingLine;

  private float downX;
  private float downY;

  public SlantPuzzleView(Context context) {
    this(context, null);
  }

  public SlantPuzzleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlantPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    bounds = new RectF();

    linePaint = new Paint();

    linePaint.setAntiAlias(true);
    linePaint.setColor(Color.WHITE);
    linePaint.setStrokeWidth(lineSize);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    bounds.left = getPaddingLeft();
    bounds.top = getPaddingTop();
    bounds.right = w - getPaddingRight();
    bounds.bottom = h - getPaddingBottom();

    if (slantLayout != null) {
      slantLayout.setOuterBorder(bounds);
      slantLayout.layout();
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (slantLayout == null) {
      return;
    }

    // draw area
    //for (SlantArea area : slantLayout.getAreas()) {
    //  canvas.save();
    //  canvas.clipPath(area.getAreaPath());
    //  canvas.drawColor(colors[random.nextInt(colors.length)]);
    //  canvas.restore();
    //}

    // draw outer bounds
    for (SlantLine outerLine : slantLayout.getOuterLines()) {
      drawLine(canvas, outerLine);
    }

    // draw slant lines
    for (SlantLine line : slantLayout.getLines()) {
      drawLine(canvas, line);
    }
  }

  private void drawLine(Canvas canvas, SlantLine line) {
    canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, linePaint);
  }

  public void setSlantLayout(SlantLayout slantLayout) {
    this.slantLayout = slantLayout;

    this.slantLayout.setOuterBorder(bounds);
    this.slantLayout.layout();

    invalidate();
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        downX = event.getX();
        downY = event.getY();

        handlingLine = findHandlingLine();
        if (handlingLine != null) {
          handlingLine.prepareMove();
        }

        for (int i = 0; i < slantLayout.getAreas().size(); i++) {
          SlantArea slantArea = slantLayout.getArea(i);
          if (slantArea.contains(downX, downY)) {
            Log.d(TAG, "onTouchEvent: position --> " + i);
          }
        }
        break;

      case MotionEvent.ACTION_MOVE:
        if (handlingLine != null) {
          if (handlingLine.direction() == Line.Direction.HORIZONTAL) {
            handlingLine.move(event.getY() - downY, 40f);
          } else {
            handlingLine.move(event.getX() - downX, 40f);
          }

          slantLayout.update();
        }

        invalidate();
        break;
    }

    return true;
  }

  private SlantLine findHandlingLine() {
    for (SlantLine line : slantLayout.getLines()) {
      if (line.contains(downX, downY, 20)) {
        Log.d(TAG, "findHandlingLine: --> " + line.toString());
        return line;
      }
    }
    return null;
  }
}
