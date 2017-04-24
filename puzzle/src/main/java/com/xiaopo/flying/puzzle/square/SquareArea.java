package com.xiaopo.flying.puzzle.square;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.base.Area;
import com.xiaopo.flying.puzzle.base.Line;
import java.util.Arrays;
import java.util.List;

/**
 * @author wupanjie
 */

public class SquareArea implements Area {
  StraightLine lineLeft;
  StraightLine lineTop;
  StraightLine lineRight;
  StraightLine lineBottom;

  private Path areaPath = new Path();
  private RectF areaRect = new RectF();

  public SquareArea(RectF baseRect) {
    setBaseRect(baseRect);
  }

  private void setBaseRect(RectF baseRect) {
    float width = baseRect.width();
    float height = baseRect.height();

    PointF one = new PointF(baseRect.left, baseRect.top);
    PointF two = new PointF(baseRect.right, baseRect.top);
    PointF three = new PointF(baseRect.left, baseRect.bottom);
    PointF four = new PointF(baseRect.right, baseRect.bottom);

    lineLeft = new StraightLine(one, three);
    lineTop = new StraightLine(one, two);
    lineRight = new StraightLine(two, four);
    lineBottom = new StraightLine(three, four);
  }

  public SquareArea(SquareArea src) {
    this.lineLeft = src.lineLeft;
    this.lineTop = src.lineTop;
    this.lineRight = src.lineRight;
    this.lineBottom = src.lineBottom;
  }

  @Override public float left() {
    return lineLeft.minX();
  }

  @Override public float top() {
    return lineTop.minY();
  }

  @Override public float right() {
    return lineRight.maxX();
  }

  @Override public float bottom() {
    return lineBottom.maxY();
  }

  @Override public float centerX() {
    return (left() + right()) / 2;
  }

  @Override public float centerY() {
    return (top() + bottom()) / 2;
  }

  @Override public float width() {
    return right() - left();
  }

  @Override public float height() {
    return bottom() - top();
  }

  @Override public PointF getCenterPoint() {
    return new PointF(centerX(), centerY());
  }

  @Override public boolean contains(PointF point) {
    return false;
  }

  @Override public boolean contains(float x, float y) {
    return getAreaRect().contains(x, y);
  }

  @Override public boolean contains(Line line) {
    return lineLeft == line || lineTop == line || lineRight == line || lineBottom == line;
  }

  @Override public Path getAreaPath() {
    areaPath.reset();
    areaPath.addRect(getAreaRect(), Path.Direction.CCW);
    return areaPath;
  }

  @Override public RectF getAreaRect() {
    areaRect.set(left(), top(), right(), bottom());
    return areaRect;
  }

  @Override public List<Line> getLines() {
    return Arrays.asList((Line) lineLeft, lineTop, lineRight, lineBottom);
  }

  // TODO
  @Override public PointF[] getHandleBarPoints(Line line) {
    PointF[] points = new PointF[2];
    points[0] = new PointF();
    points[1] = new PointF();
    return points;
  }
}
