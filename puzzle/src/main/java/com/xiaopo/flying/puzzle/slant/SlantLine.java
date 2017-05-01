package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;
import com.xiaopo.flying.puzzle.Line;

import static com.xiaopo.flying.puzzle.slant.SlantUtils.crossProduct;
import static com.xiaopo.flying.puzzle.slant.SlantUtils.intersectionOfLines;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * 分为两种斜线，横谢线和竖线线
 * 横斜线-->start为左边的点，end为右边的点
 * 竖斜线-->start为上面的点，end为下面的点
 *
 * @author wupanjie
 */

public class SlantLine implements Line {
  private static final String TAG = "SlantLine";

  public PointF start;
  public PointF end;

  // 移动前的点
  public PointF previousStart = new PointF();
  public PointF previousEnd = new PointF();

  public final Line.Direction direction;

  public SlantLine attachLineStart;
  public SlantLine attachLineEnd;

  public Line upperLine;
  public Line lowerLine;

  public SlantLine(Line.Direction direction) {
    this.direction = direction;
  }

  public SlantLine(PointF start, PointF end, Line.Direction direction) {
    this.start = start;
    this.end = end;
    this.direction = direction;
  }

  public float length() {
    return (float) sqrt(pow(end.x - start.x, 2) + pow(end.y - start.y, 2));
  }

  @Override public PointF startPoint() {
    return start;
  }

  @Override public PointF endPoint() {
    return end;
  }

  @Override public Line lowerLine() {
    return lowerLine;
  }

  @Override public Line upperLine() {
    return upperLine;
  }

  @Override public Line attachStartLine() {
    return attachLineStart;
  }

  @Override public Line attachEndLine() {
    return attachLineEnd;
  }

  @Override public void setLowerLine(Line lowerLine) {
    this.lowerLine = lowerLine;
  }

  @Override public void setUpperLine(Line upperLine) {
    this.upperLine = upperLine;
  }

  @Override public Direction direction() {
    return direction;
  }

  @Override public float slope() {
    return SlantUtils.calculateSlope(this);
  }

  public boolean contains(float x, float y, float extra) {
    PointF A, B, C, D;
    if (direction == Line.Direction.VERTICAL) {
      A = new PointF(start.x - extra, start.y);
      B = new PointF(start.x + extra, start.y);
      C = new PointF(end.x + extra, end.y);
      D = new PointF(end.x - extra, end.y);
    } else {
      A = new PointF(start.x, start.y - extra);
      B = new PointF(end.x, end.y - extra);
      C = new PointF(end.x, end.y + extra);
      D = new PointF(start.x, start.y + extra);
    }

    PointF AB = new PointF(B.x - A.x, B.y - A.y);
    PointF AM = new PointF(x - A.x, y - A.y);

    PointF BC = new PointF(C.x - B.x, C.y - B.y);
    PointF BM = new PointF(x - B.x, y - B.y);

    PointF CD = new PointF(D.x - C.x, D.y - C.y);
    PointF CM = new PointF(x - C.x, y - C.y);

    PointF DA = new PointF(A.x - D.x, A.y - D.y);
    PointF DM = new PointF(x - D.x, y - D.y);

    return crossProduct(AB, AM) > 0
        && crossProduct(BC, BM) > 0
        && crossProduct(CD, CM) > 0
        && crossProduct(DA, DM) > 0;
  }

  public void move(float offset, float extra) {
    if (direction == Line.Direction.HORIZONTAL) {
      if (previousStart.y + offset < lowerLine.maxY() + extra
          || previousStart.y + offset > upperLine.minY() - extra
          || previousEnd.y + offset < lowerLine.maxY() + extra
          || previousEnd.y + offset > upperLine.minY() - extra) {
        return;
      }

      start.y = previousStart.y + offset;
      end.y = previousEnd.y + offset;
    } else {
      if (previousStart.x + offset < lowerLine.maxX() + extra
          || previousStart.x + offset > upperLine.minX() - extra
          || previousEnd.x + offset < lowerLine.maxX() + extra
          || previousEnd.x + offset > upperLine.minX() - extra) {
        return;
      }

      start.x = previousStart.x + offset;
      end.x = previousEnd.x + offset;
    }
  }

  public void prepareMove() {
    previousStart.set(start);
    previousEnd.set(end);
  }

  // TODO 需要判断点是否超出总范围
  public void update() {
    intersectionOfLines(start, this, attachLineStart);
    intersectionOfLines(end, this, attachLineEnd);
  }

  @Override public float minX() {
    return min(start.x, end.x);
  }

  @Override public float maxX() {
    return max(start.x, end.x);
  }

  @Override public float minY() {
    return min(start.y, end.y);
  }

  @Override public float maxY() {
    return max(start.y, end.y);
  }

  @Override public String toString() {
    return "start --> " + start.toString() + ",end --> " + end.toString();
  }
}
