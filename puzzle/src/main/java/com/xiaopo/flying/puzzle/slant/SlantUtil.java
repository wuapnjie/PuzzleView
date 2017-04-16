package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;
import com.xiaopo.flying.puzzle.Line;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */

public class SlantUtil {
  private SlantUtil() {
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
  public static float crossProduct(PointF a,PointF b){
    return a.x * b.y - b.x * a.y;
  }
}
