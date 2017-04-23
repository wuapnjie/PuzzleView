package com.xiaopo.flying.puzzle.slant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class SlantPuzzleView extends View {
  private static final String TAG = "SlantPuzzleView";

  private List<SlantPuzzlePiece> puzzlePieces = new ArrayList<>();

  private SlantLayout puzzleLayout;
  private RectF bounds;

  private Paint linePaint;
  private int lineSize = 4;

  private Line handlingLine;

  private Paint bitmapPaint;

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

    // init some paint
    linePaint = new Paint();

    linePaint.setAntiAlias(true);
    linePaint.setColor(Color.WHITE);
    linePaint.setStrokeWidth(lineSize);

    bitmapPaint = new Paint();
    bitmapPaint.setAntiAlias(true);
    bitmapPaint.setFilterBitmap(true);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    bounds.left = getPaddingLeft();
    bounds.top = getPaddingTop();
    bounds.right = w - getPaddingRight();
    bounds.bottom = h - getPaddingBottom();

    if (puzzleLayout != null) {
      puzzleLayout.setOuterBorder(bounds);
      puzzleLayout.layout();
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (puzzleLayout == null) {
      return;
    }

    // draw pieces
    for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
      if (i >= puzzlePieces.size()) {
        break;
      }

      SlantPuzzlePiece piece = puzzlePieces.get(i);
      if (puzzlePieces.size() > i) {
        piece.draw(canvas);
      }
    }

    // draw outer bounds
    for (SlantLine outerLine : puzzleLayout.getOuterLines()) {
      drawLine(canvas, outerLine);
    }

    // draw slant lines
    for (SlantLine line : puzzleLayout.getLines()) {
      drawLine(canvas, line);
    }
  }

  private void drawLine(Canvas canvas, SlantLine line) {
    canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, linePaint);
  }

  public void setPuzzleLayout(SlantLayout puzzleLayout) {
    this.puzzleLayout = puzzleLayout;

    this.puzzleLayout.setOuterBorder(bounds);
    this.puzzleLayout.layout();

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

        for (int i = 0; i < puzzleLayout.getAreas().size(); i++) {
          SlantArea slantArea = puzzleLayout.getArea(i);
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

          puzzleLayout.update();
        }

        invalidate();
        break;
    }

    return true;
  }

  private SlantLine findHandlingLine() {
    for (SlantLine line : puzzleLayout.getLines()) {
      if (line.contains(downX, downY, 20)) {
        Log.d(TAG, "findHandlingLine: --> " + line.toString());
        return line;
      }
    }
    return null;
  }

  public void addPieces(List<Bitmap> bitmaps) {
    for (Bitmap bitmap : bitmaps) {
      addPiece(bitmap);
    }

    invalidate();
  }

  public void addPiece(Bitmap bitmap) {
    addPiece(new BitmapDrawable(bitmap));
  }

  // TODO 增加Padding，Bitmap等
  public void addPiece(Drawable drawable) {
    int position = puzzlePieces.size();

    if (position >= puzzleLayout.getAreaCount()) {
      Log.e(TAG, "addPiece: can not add more. the current puzzle layout can contains "
          + puzzleLayout.getAreaCount()
          + " puzzle piece.");
      return;
    }

    final Area area = puzzleLayout.getArea(position);

    final Matrix matrix = AreaUtils.generateMatrix(area, drawable, 100);

    SlantPuzzlePiece piece = new SlantPuzzlePiece(drawable, area, matrix);

    puzzlePieces.add(piece);

    invalidate();
  }
}
