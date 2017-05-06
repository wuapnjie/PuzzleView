package com.xiaopo.flying.puzzle;

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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
  private int duration = 300;

  private Line handlingLine;
  private PuzzlePiece handlingPiece;
  private PuzzlePiece replacePiece;
  private PuzzlePiece previousHandlingPiece;

  private Paint linePaint;
  private Paint selectedAreaPaint;
  private Paint handleBarPaint;

  private float downX;
  private float downY;
  private float previousDistance;
  private PointF midPoint;

  private boolean needDrawLine;
  private boolean needDrawOuterLine;
  private boolean touchEnable = true;

  private int lineColor = Color.WHITE;
  private int selectedLineColor = Color.parseColor("#99BBFB");
  private int handleBarColor = selectedLineColor;
  private float piecePadding;

  private OnPieceSelectedListener onPieceSelectedListener;

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

    midPoint = new PointF();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    bounds.left = getPaddingLeft() + piecePadding;
    bounds.top = getPaddingTop() + piecePadding;
    bounds.right = w - getPaddingRight() - piecePadding;
    bounds.bottom = h - getPaddingBottom() - piecePadding;

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

    linePaint.setStrokeWidth(lineSize);
    selectedAreaPaint.setStrokeWidth(lineSize);
    handleBarPaint.setStrokeWidth(lineSize * 3);

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
    if (!touchEnable) {
      return super.onTouchEvent(event);
    }
    switch (event.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        downX = event.getX();
        downY = event.getY();

        decideActionMode(event);
        prepareAction(event);
        break;

      case MotionEvent.ACTION_POINTER_DOWN:
        previousDistance = calculateDistance(event);
        calculateMidPoint(event, midPoint);

        decideActionMode(event);
        break;

      case MotionEvent.ACTION_MOVE:
        performAction(event);

        if ((Math.abs(event.getX() - downX) > 10 || Math.abs(event.getY() - downY) > 10)
            && currentMode != ActionMode.SWAP) {
          removeCallbacks(switchToSwapAction);
        }

        break;

      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        finishAction(event);
        currentMode = ActionMode.NONE;
        removeCallbacks(switchToSwapAction);
        break;
    }

    invalidate();
    return true;
  }

  // 决定应该执行什么Action
  private void decideActionMode(MotionEvent event) {
    for (PuzzlePiece piece : puzzlePieces) {
      if (piece.isAnimateRunning()) {
        currentMode = ActionMode.NONE;
        return;
      }
    }

    if (event.getPointerCount() == 1) {
      handlingLine = findHandlingLine();
      if (handlingLine != null) {
        currentMode = ActionMode.MOVE;
      } else {
        handlingPiece = findHandlingPiece();
        // trigger listener
        if (handlingPiece != null && onPieceSelectedListener != null) {
          onPieceSelectedListener.onPieceSelected(handlingPiece,
              puzzlePieces.indexOf(handlingPiece));
        }

        if (handlingPiece != null) {
          currentMode = ActionMode.DRAG;

          postDelayed(switchToSwapAction, 500);
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

  // 执行Action前的准备工作
  private void prepareAction(MotionEvent event) {
    switch (currentMode) {
      case NONE:
        break;
      case DRAG:
        handlingPiece.record();
        break;
      case ZOOM:
        handlingPiece.record();
        break;
      case MOVE:
        handlingLine.prepareMove();
        needChangePieces.clear();
        needChangePieces.addAll(findNeedChangedPieces());
        for (PuzzlePiece piece : puzzlePieces) {
          piece.record();
          piece.setPreviousMoveX(downX);
          piece.setPreviousMoveY(downY);
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

  // 结束Action
  private void finishAction(MotionEvent event) {
    switch (currentMode) {
      case NONE:
        break;
      case DRAG:
        if (handlingPiece != null && !handlingPiece.isFilledArea()) {
          handlingPiece.moveToFillArea(this);
        }

        if (previousHandlingPiece == handlingPiece
            && Math.abs(downX - event.getX()) < 3
            && Math.abs(downY - event.getY()) < 3) {
          handlingPiece = null;
        }

        previousHandlingPiece = handlingPiece;
        break;
      case ZOOM:
        if (handlingPiece != null && !handlingPiece.isFilledArea()) {
          if (handlingPiece.canFilledArea()) {
            handlingPiece.moveToFillArea(this);
          } else {
            handlingPiece.fillArea(this, false);
          }
        }
        previousHandlingPiece = handlingPiece;
        break;
      case MOVE:
        break;
      case SWAP:
        if (handlingPiece != null && replacePiece != null) {
          Drawable temp = handlingPiece.getDrawable();

          handlingPiece.setDrawable(replacePiece.getDrawable());
          replacePiece.setDrawable(temp);

          handlingPiece.fillArea(this, true);
          replacePiece.fillArea(this, true);

          handlingPiece = null;
          replacePiece = null;
          previousHandlingPiece = null;
        }
        break;
    }

    handlingLine = null;
    needChangePieces.clear();
  }

  private void moveLine(Line line, MotionEvent event) {
    if (line == null || event == null) return;

    boolean needUpdate = true;
    if (line.direction() == Line.Direction.HORIZONTAL) {
      needUpdate = line.move(event.getY() - downY, 80);
    } else {
      needUpdate = line.move(event.getX() - downX, 80);
    }

    puzzleLayout.update();

    if (needUpdate) {
      updatePiecesInArea(line, event);
    }
  }

  // TODO simplify
  private void updatePiecesInArea(Line line, MotionEvent event) {
    for (PuzzlePiece piece : needChangePieces) {

      float offsetX = (event.getX() - piece.getPreviousMoveX()) / 2;
      float offsetY = (event.getY() - piece.getPreviousMoveY()) / 2;

      if (!piece.canFilledArea()) {
        final Area area = piece.getArea();
        float deltaScale = MatrixUtils.getMinMatrixScale(piece) / piece.getMatrixScale();
        piece.postScale(deltaScale, deltaScale, area.getCenterPoint());
        piece.record();

        piece.setPreviousMoveY(event.getY());
        piece.setPreviousMoveX(event.getX());
      }

      if (line.direction() == Line.Direction.HORIZONTAL) {
        piece.translate(0, offsetY);
      } else if (line.direction() == Line.Direction.VERTICAL) {
        piece.translate(offsetX, 0);
      }

      final RectF rectF = piece.getCurrentDrawableBounds();
      final Area area = piece.getArea();
      float moveY = 0f;

      if (rectF.top > area.top()) {
        moveY = area.top() - rectF.top;
      }

      if (rectF.bottom < area.bottom()) {
        moveY = area.bottom() - rectF.bottom;
      }

      float moveX = 0f;

      if (rectF.left > area.left()) {
        moveX = area.left() - rectF.left;
      }

      if (rectF.right < area.right()) {
        moveX = area.right() - rectF.right;
      }

      if (moveX != 0 || moveY != 0) {
        piece.setPreviousMoveY(event.getY());
        piece.setPreviousMoveX(event.getX());
      }

      if (moveY != 0) {
        piece.setPreviousMoveY(event.getY());
        piece.setPreviousMoveX(event.getX());
      }

      piece.postTranslate(moveX, moveY);
    }
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

  public void replace(Bitmap bitmap) {
    replace(new BitmapDrawable(getResources(), bitmap));
  }

  public void replace(final Drawable bitmapDrawable) {
    post(new Runnable() {
      @Override public void run() {
        if (handlingPiece == null) {
          return;
        }

        handlingPiece.setDrawable(bitmapDrawable);
        handlingPiece.set(MatrixUtils.generateMatrix(handlingPiece, 0f));

        postInvalidate();
      }
    });
  }

  public void flipVertically() {
    if (handlingPiece == null) {
      return;
    }

    handlingPiece.postFlipVertically();
    handlingPiece.record();

    invalidate();
  }

  public void flipHorizontally() {
    if (handlingPiece == null) {
      return;
    }

    handlingPiece.postFlipHorizontally();
    handlingPiece.record();

    invalidate();
  }

  public void rotate(float degree) {
    if (handlingPiece == null) {
      return;
    }

    handlingPiece.postRotate(degree);
    handlingPiece.record();
    //handlingPiece.fillArea(this, 0);

    invalidate();
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

  private void calculateMidPoint(MotionEvent event, PointF point) {
    point.x = (event.getX(0) + event.getX(1)) / 2;
    point.y = (event.getY(0) + event.getY(1)) / 2;
  }

  public void reset() {
    handlingLine = null;
    handlingPiece = null;
    replacePiece = null;
    needChangePieces.clear();

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

  public void addPiece(Drawable drawable) {
    int position = puzzlePieces.size();

    if (position >= puzzleLayout.getAreaCount()) {
      Log.e(TAG, "addPiece: can not add more. the current puzzle layout can contains "
          + puzzleLayout.getAreaCount()
          + " puzzle piece.");
      return;
    }

    final Area area = puzzleLayout.getArea(position);
    area.setPadding(piecePadding);

    final Matrix matrix = MatrixUtils.generateMatrix(area, drawable, 0f);

    PuzzlePiece piece = new PuzzlePiece(drawable, area, matrix);
    piece.setAnimateDuration(duration);

    puzzlePieces.add(piece);

    invalidate();
  }

  public void setAnimateDuration(int duration) {
    this.duration = duration;
    for (PuzzlePiece piece : puzzlePieces) {
      piece.setAnimateDuration(duration);
    }
  }

  public boolean isNeedDrawLine() {
    return needDrawLine;
  }

  public void setNeedDrawLine(boolean needDrawLine) {
    this.needDrawLine = needDrawLine;
    handlingPiece = null;
    previousHandlingPiece = null;
    invalidate();
  }

  public boolean isNeedDrawOuterLine() {
    return needDrawOuterLine;
  }

  public void setNeedDrawOuterLine(boolean needDrawOuterLine) {
    this.needDrawOuterLine = needDrawOuterLine;
    invalidate();
  }

  public int getLineColor() {
    return lineColor;
  }

  public void setLineColor(int lineColor) {
    this.lineColor = lineColor;
    this.linePaint.setColor(lineColor);
    invalidate();
  }

  public int getLineSize() {
    return lineSize;
  }

  public void setLineSize(int lineSize) {
    this.lineSize = lineSize;
    invalidate();
  }

  public int getSelectedLineColor() {
    return selectedLineColor;
  }

  public void setSelectedLineColor(int selectedLineColor) {
    this.selectedLineColor = selectedLineColor;
    this.selectedAreaPaint.setColor(selectedLineColor);
    invalidate();
  }

  public int getHandleBarColor() {
    return handleBarColor;
  }

  public void setHandleBarColor(int handleBarColor) {
    this.handleBarColor = handleBarColor;
    this.handleBarPaint.setColor(handleBarColor);
    invalidate();
  }

  public boolean isTouchEnable() {
    return touchEnable;
  }

  public void setTouchEnable(boolean touchEnable) {
    this.touchEnable = touchEnable;
  }

  public void clearHandling() {
    handlingPiece = null;
    handlingLine = null;
    replacePiece = null;
    previousHandlingPiece = null;
    needChangePieces.clear();
  }

  public void setPiecePadding(float padding) {
    this.piecePadding = padding;
    for (PuzzlePiece piece : puzzlePieces) {
      piece.getArea().setPadding(padding);
    }
  }

  public void setOnPieceSelectedListener(OnPieceSelectedListener onPieceSelectedListener) {
    this.onPieceSelectedListener = onPieceSelectedListener;
  }

  public interface OnPieceSelectedListener {
    void onPieceSelected(PuzzlePiece piece, int position);
  }
}
