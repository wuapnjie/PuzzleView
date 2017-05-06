package com.xiaopo.flying.puzzle.slant;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.Area;
import com.xiaopo.flying.puzzle.Line;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

  private float paddingLeft;
  private float paddingTop;
  private float paddingRight;
  private float paddingBottom;

  private Path areaPath = new Path();
  private RectF areaRect = new RectF();
  private PointF[] handleBarPoints = new PointF[2];

  public SlantArea() {
    handleBarPoints[0] = new PointF();
    handleBarPoints[1] = new PointF();
  }

  public SlantArea(SlantArea src) {
    this();
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

  @Override public PointF getCenterPoint() {
    return new PointF(centerX(), centerY());
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
    return SlantUtils.contains(this, x, y);
  }

  @Override public boolean contains(Line line) {
    return lineLeft == line || lineTop == line || lineRight == line || lineBottom == line;
  }

  @Override public boolean contains(PointF point) {
    return contains(point.x, point.y);
  }

  @Override public List<Line> getLines() {
    return Arrays.asList((Line) lineLeft, lineTop, lineRight, lineBottom);
  }

  @Override public PointF[] getHandleBarPoints(Line line) {
    if (line == lineLeft) {
      SlantUtils.getPoint(handleBarPoints[0], leftTop, leftBottom, line.direction(), 0.25f);
      SlantUtils.getPoint(handleBarPoints[1], leftTop, leftBottom, line.direction(), 0.75f);
    } else if (line == lineTop) {
      SlantUtils.getPoint(handleBarPoints[0], leftTop, rightTop, line.direction(), 0.25f);
      SlantUtils.getPoint(handleBarPoints[1], leftTop, rightTop, line.direction(), 0.75f);
    } else if (line == lineRight) {
      SlantUtils.getPoint(handleBarPoints[0], rightTop, rightBottom, line.direction(), 0.25f);
      SlantUtils.getPoint(handleBarPoints[1], rightTop, rightBottom, line.direction(), 0.75f);
    } else if (line == lineBottom) {
      SlantUtils.getPoint(handleBarPoints[0], leftBottom, rightBottom, line.direction(), 0.25f);
      SlantUtils.getPoint(handleBarPoints[1], leftBottom, rightBottom, line.direction(), 0.75f);
    }
    return handleBarPoints;
  }

  // currently not support padding
  @Override public float getPaddingLeft() {
    return paddingLeft;
  }

  @Override public float getPaddingTop() {
    return paddingTop;
  }

  @Override public float getPaddingRight() {
    return paddingRight;
  }

  @Override public float getPaddingBottom() {
    return paddingBottom;
  }

  @Override public void setPadding(float padding) {
  }

  @Override public void setPadding(float paddingLeft, float paddingTop, float paddingRight,
      float paddingBottom) {
    this.paddingLeft = paddingLeft;
    this.paddingTop = paddingTop;
    this.paddingRight = paddingRight;
    this.paddingBottom = paddingBottom;
  }

  public static class AreaComparator implements Comparator<SlantArea> {

    @Override public int compare(SlantArea one, SlantArea two) {
      if (one.leftTop.y < two.leftTop.y) {
        return -1;
      } else if (one.leftTop.y == two.leftTop.y) {
        if (one.leftTop.x < two.leftTop.x) {
          return -1;
        } else {
          return 1;
        }
      } else {
        return 1;
      }
    }
  }
}
