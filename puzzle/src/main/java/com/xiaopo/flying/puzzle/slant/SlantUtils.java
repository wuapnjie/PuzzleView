package com.xiaopo.flying.puzzle.slant;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class SlantUtils {
  private SlantUtils() {
    //no instance
  }

  public static List<SlantArea> cutArea(SlantArea area, SlantLine line) {
    List<SlantArea> areas = new ArrayList<>();
    SlantArea area1 = new SlantArea(area);
    SlantArea area2 = new SlantArea(area);

    if (line.direction == Line.Direction.HORIZONTAL) {
      area1.lineBottom = line;
      area1.leftBottom = line.start;
      area1.rightBottom = line.end;

      area2.lineTop = line;
      area2.leftTop = line.start;
      area2.rightTop = line.end;
    } else {
      area1.lineRight = line;
      area1.rightTop = line.start;
      area1.rightBottom = line.end;

      area2.lineLeft = line;
      area2.leftTop = line.start;
      area2.leftBottom = line.end;
    }

    areas.add(area1);
    areas.add(area2);

    return areas;
  }

  public static SlantLine createSlantLine(SlantArea area, Line.Direction direction,
      float startRadio, float endRadio) {
    SlantLine line = new SlantLine(direction);

    if (direction == Line.Direction.HORIZONTAL) {
      line.start = getPoint(area.leftTop, area.leftBottom, Line.Direction.VERTICAL, startRadio);
      line.end = getPoint(area.rightTop, area.rightBottom, Line.Direction.VERTICAL, endRadio);

      line.attachLineStart = area.lineLeft;
      line.attachLineEnd = area.lineRight;

      line.upperLine = area.lineBottom;
      line.lowerLine = area.lineTop;
    } else {
      line.start = getPoint(area.leftTop, area.rightTop, Line.Direction.HORIZONTAL, startRadio);
      line.end = getPoint(area.leftBottom, area.rightBottom, Line.Direction.HORIZONTAL, endRadio);

      line.attachLineStart = area.lineTop;
      line.attachLineEnd = area.lineBottom;

      line.upperLine = area.lineRight;
      line.lowerLine = area.lineLeft;
    }

    return line;
  }

  public static PointF getPoint(PointF start, PointF end, Line.Direction direction, float radio) {
    PointF point = new PointF();
    float deltaY = Math.abs(start.y - end.y);
    float deltaX = Math.abs(start.x - end.x);
    float maxY = Math.max(start.y, end.y);
    float minY = Math.min(start.y, end.y);
    float maxX = Math.max(start.x, end.x);
    float minX = Math.min(start.x, end.x);
    if (direction == Line.Direction.HORIZONTAL) {
      point.x = minX + deltaX * radio;
      if (start.y < end.y) {
        point.y = minY + radio * deltaY;
      } else {
        point.y = maxY - radio * deltaY;
      }
    } else {
      point.y = minY + deltaY * radio;
      if (start.x < end.x) {
        point.x = minX + radio * deltaX;
      } else {
        point.x = maxX - radio * deltaX;
      }
    }

    return point;
  }

  // 叉乘
  public static float crossProduct(final PointF a, final PointF b) {
    return a.x * b.y - b.x * a.y;
  }

  /**
   * 计算两线的交点
   *
   * @param lineOne 线一
   * @param lineTwo 线二
   * @return 两条线的交点, 如果两线平行，则返回（0，0）
   */
  public static PointF intersectionOfLines(final SlantLine lineOne, final SlantLine lineTwo) {
    if (isParallel(lineOne, lineTwo)) {
      return new PointF(0, 0);
    }

    if (isHorizontalLine(lineOne) && isVerticalLine(lineTwo)) {
      return new PointF(lineTwo.start.x, lineOne.start.y);
    }

    if (isVerticalLine(lineOne) && isHorizontalLine(lineTwo)) {
      return new PointF(lineOne.start.x, lineTwo.start.y);
    }

    PointF intersection = new PointF();

    if (isHorizontalLine(lineOne) && !isVerticalLine(lineTwo)) {
      float k = calculateSlope(lineTwo);
      float b = calculateVerticalIntercept(lineTwo);

      intersection.y = lineOne.start.y;
      intersection.x = (intersection.y - b) / k;

      return intersection;
    }

    if (isVerticalLine(lineOne) && !isHorizontalLine(lineTwo)) {
      float k = calculateSlope(lineTwo);
      float b = calculateVerticalIntercept(lineTwo);

      intersection.x = lineOne.start.x;
      intersection.y = k * intersection.x + b;

      return intersection;
    }

    if (isHorizontalLine(lineTwo) && !isVerticalLine(lineOne)) {
      float k = calculateSlope(lineOne);
      float b = calculateVerticalIntercept(lineOne);

      intersection.y = lineTwo.start.y;
      intersection.x = (intersection.y - b) / k;

      return intersection;
    }

    if (isVerticalLine(lineTwo) && !isHorizontalLine(lineOne)) {
      float k = calculateSlope(lineOne);
      float b = calculateVerticalIntercept(lineOne);

      intersection.x = lineTwo.start.x;
      intersection.y = k * intersection.x + b;

      return intersection;
    }

    final float k1 = calculateSlope(lineOne);
    final float b1 = calculateVerticalIntercept(lineOne);

    final float k2 = calculateSlope(lineTwo);
    final float b2 = calculateVerticalIntercept(lineTwo);

    intersection.x = (b2 - b1) / (k1 - k2);
    intersection.y = intersection.x * k1 + b1;

    return intersection;
  }

  public static boolean isHorizontalLine(SlantLine line) {
    return line.start.y == line.end.y;
  }

  public static boolean isVerticalLine(SlantLine line) {
    return line.start.x == line.end.x;
  }

  /**
   * 判断两条线是否平行
   *
   * @param lineOne 第一条
   * @param lineTwo 第二条
   * @return 是否平行
   */
  public static boolean isParallel(final SlantLine lineOne, final SlantLine lineTwo) {
    return calculateSlope(lineOne) == calculateSlope(lineTwo);
  }

  /**
   * 计算线的斜率
   *
   * @param line 线
   * @return 线的斜率
   */
  public static float calculateSlope(final SlantLine line) {
    if (isHorizontalLine(line)) {
      return 0f;
    } else if (isVerticalLine(line)) {
      return Float.POSITIVE_INFINITY;
    } else {
      return (line.start.y - line.end.y) / (line.start.x - line.end.x);
    }
  }

  /**
   * 计算纵截距
   *
   * @param line 线
   * @return 纵截距
   */
  public static float calculateVerticalIntercept(final SlantLine line) {
    if (isHorizontalLine(line)) {
      return line.start.y;
    } else if (isVerticalLine(line)) {
      return Float.POSITIVE_INFINITY;
    } else {
      float k = calculateSlope(line);
      return line.start.y - k * line.start.x;
    }
  }

  // TODO
  public Path generateHandleBounds(SlantArea area, Line line){
    Path path = new Path();
    PointF start;
    PointF end;
    if (area.lineLeft == line){

    }else if (area.lineTop == line){

    }else if (area.lineRight == line){

    }else if (area.lineBottom == line){

    }

    return path;
  }
}
