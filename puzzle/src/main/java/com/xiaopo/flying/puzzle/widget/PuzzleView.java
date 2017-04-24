package com.xiaopo.flying.puzzle.widget;

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
import com.xiaopo.flying.puzzle.base.Area;
import com.xiaopo.flying.puzzle.base.AreaUtils;
import com.xiaopo.flying.puzzle.base.Line;
import com.xiaopo.flying.puzzle.base.PuzzleLayout;
import com.xiaopo.flying.puzzle.base.PuzzlePiece;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class PuzzleView extends View {
  private static final String TAG = "SlantPuzzleView";

  private enum ActionMode {
    NONE, DRAG, ZOOM, MOVE, SWAP
  }

  private ActionMode currentMode = ActionMode.NONE;

  private List<PuzzlePiece> puzzlePieces = new ArrayList<>();
  private List<PuzzlePiece> needChangePieces = new ArrayList<>();

  private PuzzleLayout puzzleLayout;
  private RectF bounds;

  private int lineSize = 4;
  private Line handlingLine;

  private PuzzlePiece handlingPiece;
  private PuzzlePiece replacePiece;

  private Paint linePaint;
  private Paint selectedAreaPaint;
  private Paint handleBarPaint;

  private float downX;
  private float downY;
  private float previousDistance;
  private PointF midPoint;

  private boolean needDrawLine;
  private boolean needDrawOuterLine;

  private int lineColor = Color.WHITE;
  private int selectedLineColor = Color.parseColor("#99BBFB");
  private int handleBarColor = selectedLineColor;

  private Handler handler;
  private Runnable switchToSwapAction = new Runnable() {
    @Override public void run() {
      currentMode = ActionMode.SWAP;
      invalidate();
    }
  };

  public PuzzleView(Context context) {
    this(context, null);
  }

  public PuzzleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    bounds = new RectF();

    // init some paint
    linePaint = new Paint();

    linePaint.setAntiAlias(true);
    linePaint.setColor(lineColor);
    linePaint.setStrokeWidth(lineSize);

    selectedAreaPaint = new Paint();
    selectedAreaPaint.setAntiAlias(true);
    selectedAreaPaint.setStyle(Paint.Style.STROKE);
    selectedAreaPaint.setColor(selectedLineColor);
    selectedAreaPaint.setStrokeWidth(lineSize);

    handleBarPaint = new Paint();
    handleBarPaint.setAntiAlias(true);
    handleBarPaint.setStyle(Paint.Style.FILL);
    handleBarPaint.setColor(handleBarColor);
    handleBarPaint.setStrokeWidth(lineSize * 3);

    handler = new Handler();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    bounds.left = getPaddingLeft();
    bounds.top = getPaddingTop();
    bounds.right = w - getPaddingRight();
    bounds.bottom = h - getPaddingBottom();

    if (puzzleLayout != null) {
      puzzleLayout.reset();
      puzzleLayout.setOuterBounds(bounds);
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

      PuzzlePiece piece = puzzlePieces.get(i);

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

  private void drawSelectedArea(Canvas canvas, PuzzlePiece piece) {
    final Area area = piece.getArea();
    // draw select area
    canvas.drawPath(area.getAreaPath(), selectedAreaPaint);

    // draw handle bar
    for (Line line : area.getLines()) {
      if (puzzleLayout.getLines().contains(line)) {
        handleBarPaint.setStrokeWidth(lineSize * 3);
        PointF[] handleBarPoints = area.getHandleBarPoints(line);
        canvas.drawLine(handleBarPoints[0].x, handleBarPoints[0].y, handleBarPoints[1].x,
            handleBarPoints[1].y, handleBarPaint);
        canvas.drawCircle(handleBarPoints[0].x, handleBarPoints[0].y, lineSize * 3 / 2,
            handleBarPaint);
        canvas.drawCircle(handleBarPoints[1].x, handleBarPoints[1].y, lineSize * 3 / 2,
            handleBarPaint);
      }
    }
  }

  private void drawLine(Canvas canvas, Line line) {
    canvas.drawLine(line.startPoint().x, line.startPoint().y, line.endPoint().x, line.endPoint().y,
        linePaint);
  }

  public void setPuzzleLayout(PuzzleLayout puzzleLayout) {
    this.puzzleLayout = puzzleLayout;

    this.puzzleLayout.setOuterBounds(bounds);
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
        if (!handlingPiece.isFilledArea()) {
          moveToFillArea(handlingPiece, 300);
        }
        break;
      case ZOOM:
        if (!handlingPiece.isFilledArea()) {
          if (handlingPiece.canFilledArea()) {
            moveToFillArea(handlingPiece, 300);
          } else {
            // TODO 过于强硬
            fillArea(handlingPiece);
          }
        }
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

  private void moveToFillArea(PuzzlePiece piece, int duration) {
    piece.prepare();

    Area area = piece.getArea();
    RectF rectF = piece.getMappedBounds();
    float offsetX = 0f;
    float offsetY = 0f;

    if (rectF.left > area.left()) {
      offsetX = area.left() - rectF.left;
    }

    if (rectF.top > area.top()) {
      offsetY = area.top() - rectF.top;
    }

    if (rectF.right < area.right()) {
      offsetX = area.right() - rectF.right;
    }

    if (rectF.bottom < area.bottom()) {
      offsetY = area.bottom() - rectF.bottom;
    }

    if (duration == 0) {
      piece.translate(offsetX, offsetY);
    } else {
      piece.translate(this, offsetX, offsetY, duration);
    }

    if (!piece.isFilledArea()) {
      fillArea(piece);
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
      line.move(event.getY() - downY, 80);
    } else {
      line.move(event.getX() - downX, 80);
    }

    puzzleLayout.update();
    updatePiecesInArea(line, event);
  }

  // TODO
  private void updatePiecesInArea(Line line, MotionEvent event) {
    for (PuzzlePiece piece : needChangePieces) {

    }
  }

  // TODO
  private void fillArea(PuzzlePiece piece) {
    piece.set(AreaUtils.generateMatrix(piece, 0f));
  }

  private void zoomPiece(PuzzlePiece piece, MotionEvent event) {
    if (piece == null || event == null || event.getPointerCount() < 2) return;
    float scale = calculateDistance(event) / previousDistance;
    piece.zoomAndTranslate(scale, scale, midPoint, event.getX() - downX, event.getY() - downY);
  }

  private void dragPiece(PuzzlePiece piece, MotionEvent event) {
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
        for (PuzzlePiece piece : needChangePieces) {
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

  private PuzzlePiece findHandlingPiece() {
    for (PuzzlePiece piece : puzzlePieces) {
      if (piece.contains(downX, downY)) {
        return piece;
      }
    }
    return null;
  }

  private Line findHandlingLine() {
    for (Line line : puzzleLayout.getLines()) {
      if (line.contains(downX, downY, 40)) {
        Log.d(TAG, "findHandlingLine: --> " + line.toString());
        return line;
      }
    }
    return null;
  }

  private PuzzlePiece findReplacePiece(MotionEvent event) {
    for (PuzzlePiece piece : puzzlePieces) {
      if (piece.contains(event.getX(), event.getY())) {
        return piece;
      }
    }
    return null;
  }

  private List<PuzzlePiece> findNeedChangedPieces() {
    if (handlingLine == null) return new ArrayList<>();

    List<PuzzlePiece> needChanged = new ArrayList<>();

    for (PuzzlePiece piece : puzzlePieces) {
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
    addPiece(new BitmapDrawable(getResources(), bitmap));
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

    final Matrix matrix = AreaUtils.generateMatrix(area, drawable, 100f);

    PuzzlePiece piece = new PuzzlePiece(drawable, area, matrix);

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

  public void setLineColor(int lineColor) {
    this.lineColor = lineColor;
    invalidate();
  }

  public void setSelectedLineColor(int selectedLineColor) {
    this.selectedLineColor = selectedLineColor;
    invalidate();
  }

  public void setHandleBarColor(int handleBarColor) {
    this.handleBarColor = handleBarColor;
    invalidate();
  }
}
