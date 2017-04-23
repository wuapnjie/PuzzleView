package com.xiaopo.flying.puzzle.slant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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

  private enum ActionMode {
    NONE, DRAG, ZOOM, MOVE, SWAP
  }

  private ActionMode currentMode = ActionMode.NONE;

  private List<SlantPuzzlePiece> puzzlePieces = new ArrayList<>();
  private List<SlantPuzzlePiece> needChangePieces = new ArrayList<>();

  private SlantLayout puzzleLayout;
  private RectF bounds;

  private int lineSize = 4;
  private Line handlingLine;

  private SlantPuzzlePiece handlingPiece;
  private SlantPuzzlePiece replacePiece;

  private Paint linePaint;
  private Paint selectedAreaPaint;

  private float downX;
  private float downY;
  private float previousDistance;
  private PointF midPoint;

  private boolean needDrawLine;
  private boolean needDrawOuterLine;

  private Handler handler;
  private Runnable switchToSwapAction = new Runnable() {
    @Override public void run() {
      currentMode = ActionMode.SWAP;
      invalidate();
    }
  };

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

    selectedAreaPaint = new Paint();
    selectedAreaPaint.setAntiAlias(true);
    selectedAreaPaint.setStyle(Paint.Style.STROKE);
    selectedAreaPaint.setColor(Color.parseColor("#99BBFB"));
    selectedAreaPaint.setStrokeWidth(lineSize);

    handler = new Handler();
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

      if (piece == handlingPiece && currentMode == ActionMode.SWAP) {
        continue;
      }

      if (puzzlePieces.size() > i) {
        piece.draw(canvas);
      }
    }

    // draw outer bounds
    if (needDrawOuterLine) {
      for (Line outerLine : puzzleLayout.getOuterLines()) {
        drawLine(canvas, outerLine);
      }
    }

    // draw slant lines
    if (needDrawLine) {
      for (Line line : puzzleLayout.getLines()) {
        drawLine(canvas, line);
      }
    }

    // draw selected area
    if (handlingPiece != null && currentMode != ActionMode.SWAP) {
      drawSelectedArea(canvas, handlingPiece);
    }

    // draw swap piece
    if (handlingPiece != null && currentMode == ActionMode.SWAP) {
      handlingPiece.draw(canvas, 128);
      if (replacePiece != null) {
        drawSelectedArea(canvas, replacePiece);
      }
    }
  }

  // TODO handle bar draw
  private void drawSelectedArea(Canvas canvas, SlantPuzzlePiece piece) {
    canvas.drawPath(piece.getArea().getAreaPath(), selectedAreaPaint);
    for (Line line : piece.getArea().getLines()) {
      if (puzzleLayout.getLines().contains(line)) {

      }
    }
  }

  private void drawLine(Canvas canvas, Line line) {
    canvas.drawLine(line.startPoint().x, line.startPoint().y, line.endPoint().x, line.endPoint().y,
        linePaint);
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

        decideActionMode(event);
        prepareAction(event);

        break;

      case MotionEvent.ACTION_POINTER_DOWN:
        previousDistance = calculateDistance(event);
        midPoint = calculateMidPoint(event);

        decideActionMode(event);
        break;

      case MotionEvent.ACTION_MOVE:
        performAction(event);

        if ((Math.abs(event.getX() - downX) > 10 || Math.abs(event.getY() - downY) > 10)
            && currentMode != ActionMode.SWAP) {
          handler.removeCallbacks(switchToSwapAction);
        }

        break;

      case MotionEvent.ACTION_UP:
        finishAction(event);
        currentMode = ActionMode.NONE;
        handler.removeCallbacks(switchToSwapAction);
        break;
    }

    invalidate();
    return true;
  }

  // 结束Action
  private void finishAction(MotionEvent event) {
    switch (currentMode) {
      case NONE:
        break;
      case DRAG:
        break;
      case ZOOM:
        break;
      case MOVE:
        break;
      case SWAP:
        if (handlingPiece != null && replacePiece != null) {
          Drawable temp = handlingPiece.getDrawable();

          handlingPiece.setDrawable(replacePiece.getDrawable());
          replacePiece.setDrawable(temp);

          fillArea(handlingPiece);
          fillArea(replacePiece);

          handlingPiece = null;
          replacePiece = null;
        }
        break;
    }
  }

  // 执行Action
  private void performAction(MotionEvent event) {
    switch (currentMode) {
      case NONE:
        break;
      case DRAG:
        dragPiece(handlingPiece, event);
        break;
      case ZOOM:
        zoomPiece(handlingPiece, event);
        break;
      case SWAP:
        dragPiece(handlingPiece, event);
        replacePiece = findReplacePiece(event);

        break;
      case MOVE:
        moveLine(handlingLine, event);
        break;
    }
  }

  private void moveLine(Line line, MotionEvent event) {
    if (line == null || event == null) return;

    if (line.direction() == Line.Direction.HORIZONTAL) {
      line.move(event.getY() - downY, 20);
    } else {
      line.move(event.getX() - downX, 20);
    }

    puzzleLayout.update();
    updatePiecesInArea();
  }

  // TODO
  private void updatePiecesInArea() {
    for (SlantPuzzlePiece piece : needChangePieces) {
      fillArea(piece);
    }
  }

  // TODO
  private void fillArea(SlantPuzzlePiece piece) {
    piece.set(AreaUtils.generateMatrix(piece, 100f));
  }

  private void zoomPiece(SlantPuzzlePiece piece, MotionEvent event) {
    if (piece == null || event == null || event.getPointerCount() < 2) return;
    float scale = calculateDistance(event) / previousDistance;
    piece.zoom(scale, scale, midPoint);
  }

  private void dragPiece(SlantPuzzlePiece piece, MotionEvent event) {
    if (piece == null || event == null) return;
    piece.translate(event.getX() - downX, event.getY() - downY);
  }

  // 执行Action前的准备工作
  private void prepareAction(MotionEvent event) {
    switch (currentMode) {
      case NONE:
        break;
      case DRAG:
        handlingPiece.prepare();
        break;
      case ZOOM:
        break;
      case MOVE:
        handlingLine.prepareMove();
        needChangePieces.clear();
        needChangePieces.addAll(findNeedChangedPieces());
        for (SlantPuzzlePiece piece : needChangePieces) {
          piece.prepare();
        }
        break;
    }
  }

  // 决定应该执行什么Action
  private void decideActionMode(MotionEvent event) {
    if (event.getPointerCount() == 1) {
      handlingLine = findHandlingLine();
      if (handlingLine != null) {
        currentMode = ActionMode.MOVE;
      } else {
        handlingPiece = findHandlingPiece();
        if (handlingPiece != null) {
          currentMode = ActionMode.DRAG;

          handler.postDelayed(switchToSwapAction, 1000);
        }
      }
    } else if (event.getPointerCount() > 1) {
      if (handlingPiece != null
          && handlingPiece.contains(event.getX(1), event.getY(1))
          && currentMode == ActionMode.DRAG) {
        currentMode = ActionMode.ZOOM;
      }
    }
  }

  private SlantPuzzlePiece findHandlingPiece() {
    for (SlantPuzzlePiece piece : puzzlePieces) {
      if (piece.contains(downX, downY)) {
        return piece;
      }
    }
    return null;
  }

  private Line findHandlingLine() {
    for (Line line : puzzleLayout.getLines()) {
      if (line.contains(downX, downY, 20)) {
        Log.d(TAG, "findHandlingLine: --> " + line.toString());
        return line;
      }
    }
    return null;
  }

  private SlantPuzzlePiece findReplacePiece(MotionEvent event) {
    for (SlantPuzzlePiece piece : puzzlePieces) {
      if (piece.contains(event.getX(), event.getY())) {
        return piece;
      }
    }
    return null;
  }


  private List<SlantPuzzlePiece> findNeedChangedPieces() {
    if (handlingPiece == null) return new ArrayList<>();

    List<SlantPuzzlePiece> needChanged = new ArrayList<>();

    for (SlantPuzzlePiece piece : puzzlePieces) {
      if (piece.contains(handlingLine)) {
        needChanged.add(piece);
      }
    }

    return needChanged;
  }

  private float calculateDistance(MotionEvent event) {
    float x = event.getX(0) - event.getX(1);
    float y = event.getY(0) - event.getY(1);

    return (float) Math.sqrt(x * x + y * y);
  }

  private PointF calculateMidPoint(MotionEvent event) {
    float x = (event.getX(0) + event.getX(1)) / 2;
    float y = (event.getY(0) + event.getY(1)) / 2;
    return new PointF(x, y);
  }

  public void reset() {
    handlingLine = null;
    handlingPiece = null;

    if (puzzleLayout != null) {
      puzzleLayout.reset();
    }

    puzzlePieces.clear();
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

  public void setNeedDrawLine(boolean needDrawLine) {
    this.needDrawLine = needDrawLine;
    invalidate();
  }

  public void setNeedDrawOuterLine(boolean needDrawOuterLine) {
    this.needDrawOuterLine = needDrawOuterLine;
    invalidate();
  }
}
