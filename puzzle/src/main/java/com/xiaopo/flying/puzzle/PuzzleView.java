package com.xiaopo.flying.puzzle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wupanjie
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class PuzzleView extends View {
  private static final String TAG = "PuzzleView";

  private enum ActionMode {
    NONE, DRAG, ZOOM, MOVE, SWAP
  }

  private ActionMode currentMode = ActionMode.NONE;

  private List<PuzzlePiece> puzzlePieces = new ArrayList<>();
  private List<PuzzlePiece> needChangePieces = new ArrayList<>();
  private Map<Area, PuzzlePiece> areaPieceMap = new HashMap<>();

  private PuzzleLayout puzzleLayout;
  private PuzzleLayout.Info initialInfo;

  private RectF bounds;
  private int lineSize;

  private int duration;
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
  private int lineColor;

  private int selectedLineColor;
  private int handleBarColor;
  private float piecePadding;
  private float pieceRadian;

  private boolean needResetPieceMatrix = true;
  private boolean quickMode = false;

  private boolean canDrag = true;
  private boolean canMoveLine = true;
  private boolean canZoom = true;
  private boolean canSwap = true;

  private OnPieceSelectedListener onPieceSelectedListener;

  private Runnable switchToSwapAction = new Runnable() {
    @Override public void run() {
      if (!canSwap) return;
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
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PuzzleView);
    lineSize = ta.getInt(R.styleable.PuzzleView_line_size, 4);
    lineColor = ta.getColor(R.styleable.PuzzleView_line_color, Color.WHITE);
    selectedLineColor =
        ta.getColor(R.styleable.PuzzleView_selected_line_color, Color.parseColor("#99BBFB"));
    handleBarColor =
        ta.getColor(R.styleable.PuzzleView_handle_bar_color, Color.parseColor("#99BBFB"));
    piecePadding = ta.getDimensionPixelSize(R.styleable.PuzzleView_piece_padding, 0);
    needDrawLine = ta.getBoolean(R.styleable.PuzzleView_need_draw_line, false);
    needDrawOuterLine = ta.getBoolean(R.styleable.PuzzleView_need_draw_outer_line, false);
    duration = ta.getInt(R.styleable.PuzzleView_animation_duration, 300);
    pieceRadian = ta.getFloat(R.styleable.PuzzleView_radian, 0f);
    ta.recycle();

    bounds = new RectF();

    // init some paint
    linePaint = new Paint();
    linePaint.setAntiAlias(true);
    linePaint.setColor(lineColor);
    linePaint.setStrokeWidth(lineSize);
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setStrokeJoin(Paint.Join.ROUND);
    linePaint.setStrokeCap(Paint.Cap.SQUARE);

    selectedAreaPaint = new Paint();
    selectedAreaPaint.setAntiAlias(true);
    selectedAreaPaint.setStyle(Paint.Style.STROKE);
    selectedAreaPaint.setStrokeJoin(Paint.Join.ROUND);
    selectedAreaPaint.setStrokeCap(Paint.Cap.ROUND);
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
    resetPuzzleBounds();

    areaPieceMap.clear();
    if (puzzlePieces.size() != 0) {
      for (int i = 0; i < puzzlePieces.size(); i++) {
        PuzzlePiece piece = puzzlePieces.get(i);
        Area area = puzzleLayout.getArea(i);
        piece.setArea(area);
        areaPieceMap.put(area, piece);
        if (needResetPieceMatrix) {
          piece.set(MatrixUtils.generateMatrix(piece, 0f));
        } else {
          piece.fillArea(this, true);
        }
      }
    }
    invalidate();
  }

  private void resetPuzzleBounds() {
    bounds.left = getPaddingLeft();
    bounds.top = getPaddingTop();
    bounds.right = getWidth() - getPaddingRight();
    bounds.bottom = getHeight() - getPaddingBottom();

    if (puzzleLayout != null) {
      puzzleLayout.reset();
      puzzleLayout.setOuterBounds(bounds);
      puzzleLayout.layout();
      puzzleLayout.setPadding(piecePadding);
      puzzleLayout.setRadian(pieceRadian);

      if (initialInfo != null) {
        final int size = initialInfo.lineInfos.size();
        for (int i = 0; i < size; i++) {
          PuzzleLayout.LineInfo lineInfo = initialInfo.lineInfos.get(i);
          Line line = puzzleLayout.getLines().get(i);
          line.startPoint().x = lineInfo.startX;
          line.startPoint().y = lineInfo.startY;
          line.endPoint().x = lineInfo.endX;
          line.endPoint().y = lineInfo.endY;
        }
      }

      puzzleLayout.sortAreas();
      puzzleLayout.update();
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
        piece.draw(canvas, quickMode);
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
      handlingPiece.draw(canvas, 128, quickMode);
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
    clearPieces();

    this.puzzleLayout = puzzleLayout;

    puzzleLayout.setOuterBounds(bounds);
    puzzleLayout.layout();

    invalidate();
  }

  public void setPuzzleLayout(PuzzleLayout.Info info) {
    this.initialInfo = info;
    clearPieces();

    this.puzzleLayout = PuzzleLayoutParser.parse(info);

    this.piecePadding = info.padding;
    this.pieceRadian = info.radian;
    setBackgroundColor(info.color);

    invalidate();
  }

  public PuzzleLayout getPuzzleLayout() {
    return puzzleLayout;
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
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
      if (handlingLine != null && canMoveLine) {
        currentMode = ActionMode.MOVE;
      } else {
        handlingPiece = findHandlingPiece();

        if (handlingPiece != null && canDrag) {
          currentMode = ActionMode.DRAG;

          postDelayed(switchToSwapAction, 500);
        }
      }
    } else if (event.getPointerCount() > 1) {
      if (handlingPiece != null
          && handlingPiece.contains(event.getX(1), event.getY(1))
          && currentMode == ActionMode.DRAG
          && canZoom) {
        currentMode = ActionMode.ZOOM;
      }
    }
  }

  // 执行Action前的准备工作
  @SuppressWarnings("unused")
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
        for (PuzzlePiece piece : needChangePieces) {
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
          swapPiece();

          handlingPiece = null;
          replacePiece = null;
          previousHandlingPiece = null;
        }
        break;
    }

    // trigger listener
    if (handlingPiece != null && onPieceSelectedListener != null) {
      onPieceSelectedListener.onPieceSelected(handlingPiece,
          puzzlePieces.indexOf(handlingPiece));
    }

    handlingLine = null;
    needChangePieces.clear();
  }

  private void swapPiece() {
    Drawable temp = handlingPiece.getDrawable();
    String tempPath = handlingPiece.getPath();

    handlingPiece.setDrawable(replacePiece.getDrawable());
    handlingPiece.setPath(replacePiece.getPath());
    replacePiece.setDrawable(temp);
    replacePiece.setPath(tempPath);

    handlingPiece.fillArea(this, true);
    replacePiece.fillArea(this, true);
  }

  private void moveLine(Line line, MotionEvent event) {
    if (line == null || event == null) return;

    boolean needUpdate;
    if (line.direction() == Line.Direction.HORIZONTAL) {
      needUpdate = line.move(event.getY() - downY, 80);
    } else {
      needUpdate = line.move(event.getX() - downX, 80);
    }

    if (needUpdate) {
      puzzleLayout.update();
      puzzleLayout.sortAreas();
      updatePiecesInArea(line, event);
    }
  }

  private void updatePiecesInArea(Line line, MotionEvent event) {
    for (int i = 0; i < needChangePieces.size(); i++) {
      needChangePieces.get(i).updateWith(event, line);
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

  public void replace(Bitmap bitmap, String path) {
    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
    bitmapDrawable.setAntiAlias(true);
    bitmapDrawable.setFilterBitmap(true);

    replace(bitmapDrawable, path);
  }

  public void replace(final Drawable bitmapDrawable, String path) {
    if (handlingPiece == null) {
      return;
    }

    handlingPiece.setPath(path);
    handlingPiece.setDrawable(bitmapDrawable);
    handlingPiece.set(MatrixUtils.generateMatrix(handlingPiece, 0f));

    invalidate();
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
    clearPieces();
    if (puzzleLayout != null) {
      puzzleLayout.reset();
    }
  }

  public void clearPieces() {
    clearHandlingPieces();
    puzzlePieces.clear();

    invalidate();
  }

  public void clearHandlingPieces() {
    handlingLine = null;
    handlingPiece = null;
    replacePiece = null;
    needChangePieces.clear();

    invalidate();
  }

  public void addPieces(List<Bitmap> bitmaps) {
    for (Bitmap bitmap : bitmaps) {
      addPiece(bitmap);
    }

    postInvalidate();
  }

  public void addDrawablePieces(List<Drawable> drawables) {
    for (Drawable drawable : drawables) {
      addPiece(drawable);
    }

    postInvalidate();
  }

  public void addPiece(Bitmap bitmap) {
    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
    bitmapDrawable.setAntiAlias(true);
    bitmapDrawable.setFilterBitmap(true);

    addPiece(bitmapDrawable, null);
  }

  public void addPiece(Bitmap bitmap, Matrix initialMatrix) {
    addPiece(bitmap, initialMatrix, "");
  }

  public void addPiece(Bitmap bitmap, Matrix initialMatrix, String path) {
    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
    bitmapDrawable.setAntiAlias(true);
    bitmapDrawable.setFilterBitmap(true);

    addPiece(bitmapDrawable, initialMatrix, path);
  }


  public void addPiece(Drawable drawable) {
    addPiece(drawable, null);
  }

  public void addPiece(Drawable drawable, Matrix initialMatrix) {
    addPiece(drawable, initialMatrix, "");
  }

  public void addPiece(Drawable drawable, Matrix initialMatrix, String path) {
    int position = puzzlePieces.size();

    if (position >= puzzleLayout.getAreaCount()) {
      Log.e(TAG, "addPiece: can not add more. the current puzzle layout can contains "
          + puzzleLayout.getAreaCount()
          + " puzzle piece.");
      return;
    }

    final Area area = puzzleLayout.getArea(position);
    area.setPadding(piecePadding);

    PuzzlePiece piece = new PuzzlePiece(drawable, area, new Matrix());

    final Matrix matrix = initialMatrix != null
        ? new Matrix(initialMatrix) : MatrixUtils.generateMatrix(area, drawable, 0f);
    piece.set(matrix);

    piece.setAnimateDuration(duration);
    piece.setPath(path);

    puzzlePieces.add(piece);
    areaPieceMap.put(area, piece);

    setPiecePadding(piecePadding);
    setPieceRadian(pieceRadian);

    invalidate();
  }

  public void setSelected(final int position) {
    post(new Runnable() {
      @Override public void run() {
        if (position >= puzzlePieces.size()) return;
        previousHandlingPiece = handlingPiece = puzzlePieces.get(position);

        if (onPieceSelectedListener != null) {
          onPieceSelectedListener.onPieceSelected(handlingPiece, position);
        }
        invalidate();
      }
    });
  }

  public PuzzlePiece getHandlingPiece() {
    return handlingPiece;
  }

  public int getHandlingPiecePosition() {
    if (handlingPiece == null) {
      return -1;
    }
    return puzzlePieces.indexOf(handlingPiece);
  }

  // can be null
  public boolean hasPieceSelected() {
    return handlingPiece != null;
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
    if (puzzleLayout != null) {
      puzzleLayout.setPadding(padding);
      final int size = puzzlePieces.size();
      for (int i = 0; i < size; i++) {
        PuzzlePiece puzzlePiece = puzzlePieces.get(i);
        if (puzzlePiece.canFilledArea()) {
          puzzlePiece.moveToFillArea(null);
        } else {
          puzzlePiece.fillArea(this, true);
        }
      }
    }

    invalidate();
  }

  public void setPieceRadian(float radian) {
    this.pieceRadian = radian;
    if (puzzleLayout != null) {
      puzzleLayout.setRadian(radian);
    }

    invalidate();
  }

  public void setQuickMode(boolean quickMode) {
    this.quickMode = quickMode;
    invalidate();
  }

  @Override public void setBackgroundColor(int color) {
    super.setBackgroundColor(color);
    if (puzzleLayout != null) {
      puzzleLayout.setColor(color);
    }
  }

  public void setNeedResetPieceMatrix(boolean needResetPieceMatrix) {
    this.needResetPieceMatrix = needResetPieceMatrix;
  }

  public float getPiecePadding() {
    return piecePadding;
  }

  public float getPieceRadian() {
    return pieceRadian;
  }

  public List<PuzzlePiece> getPuzzlePieces() {
    final int size = puzzlePieces.size();
    final List<PuzzlePiece> pieces = new ArrayList<>(size);
    puzzleLayout.sortAreas();
    for (int i = 0; i < size; i++) {
      Area area = puzzleLayout.getArea(i);
      PuzzlePiece piece = areaPieceMap.get(area);
      pieces.add(piece);
    }

    return pieces;
  }

  public boolean canDrag() {
    return canDrag;
  }

  public void setCanDrag(boolean canDrag) {
    this.canDrag = canDrag;
  }

  public boolean canMoveLine() {
    return canMoveLine;
  }

  public void setCanMoveLine(boolean canMoveLine) {
    this.canMoveLine = canMoveLine;
  }

  public boolean canZoom() {
    return canZoom;
  }

  public void setCanZoom(boolean canZoom) {
    this.canZoom = canZoom;
  }

  public boolean canSwap() {
    return canSwap;
  }

  public void setCanSwap(boolean canSwap) {
    this.canSwap = canSwap;
  }

  public void setOnPieceSelectedListener(OnPieceSelectedListener onPieceSelectedListener) {
    this.onPieceSelectedListener = onPieceSelectedListener;
  }

  public interface OnPieceSelectedListener {
    void onPieceSelected(PuzzlePiece piece, int position);
  }
}
