package com.xiaopo.flying.puzzle.slant;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import java.util.Arrays;
import java.util.List;

import static com.xiaopo.flying.puzzle.slant.SlantUtils.crossProduct;

/**
 * @author wupanjie
 */

public class SlantArea implements Area {
  SlantLine lineLeft;
  SlantLine lineTop;
  SlantLine lineRight;
  SlantLine lineBottom;

  PointF leftTop;
  PointF leftBottom;
  PointF rightTop;
  PointF rightBottom;

  private Path areaPath = new Path();
  private RectF areaRect = new RectF();

  public SlantArea() {

  }

  public SlantArea(SlantArea src) {
    this.lineLeft = src.lineLeft;
    this.lineTop = src.lineTop;
    this.lineRight = src.lineRight;
    this.lineBottom = src.lineBottom;

    this.leftTop = src.leftTop;
    this.leftBottom = src.leftBottom;
    this.rightTop = src.rightTop;
    this.rightBottom = src.rightBottom;
  }

  @Override public float left() {
    return Math.min(leftTop.x, leftBottom.x);
  }

  @Override public float top() {
    return Math.min(leftTop.y, rightTop.y);
  }

  @Override public float right() {
    return Math.max(rightTop.x, rightBottom.x);
  }

  @Override public float bottom() {
    return Math.max(leftBottom.y, rightBottom.y);
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

  public Path getAreaPath() {
    areaPath.reset();
    areaPath.moveTo(leftTop.x, leftTop.y);
    areaPath.lineTo(rightTop.x, rightTop.y);
    areaPath.lineTo(rightBottom.x, rightBottom.y);
    areaPath.lineTo(leftBottom.x, leftBottom.y);
    areaPath.lineTo(leftTop.x, leftTop.y);

    return areaPath;
  }

  @Override public RectF getAreaRect() {
    areaRect.set(left(), top(), right(), bottom());
    return areaRect;
  }

  public boolean contains(float x, float y) {
    PointF AB = new PointF(rightTop.x - leftTop.x, rightTop.y - leftTop.y);
    PointF AM = new PointF(x - leftTop.x, y - leftTop.y);

    PointF BC = new PointF(rightBottom.x - rightTop.x, rightBottom.y - rightTop.y);
    PointF BM = new PointF(x - rightTop.x, y - rightTop.y);

    PointF CD = new PointF(leftBottom.x - rightBottom.x, leftBottom.y - rightBottom.y);
    PointF CM = new PointF(x - rightBottom.x, y - rightBottom.y);

    PointF DA = new PointF(leftTop.x - leftBottom.x, leftTop.y - leftBottom.y);
    PointF DM = new PointF(x - leftBottom.x, y - leftBottom.y);

    return crossProduct(AB, AM) > 0
        && crossProduct(BC, BM) > 0
        && crossProduct(CD, CM) > 0
        && crossProduct(DA, DM) > 0;
  }

  @Override public boolean contains(PointF point) {
    return contains(point.x, point.y);
  }

  @Override public List<Line> getLines() {
    return Arrays.asList((Line) lineLeft, lineTop, lineRight, lineBottom);
  }
}
