package com.xiaopo.flying.puzzle.straight;

import android.graphics.PointF;
import android.util.Pair;
import com.xiaopo.flying.puzzle.Line;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
class StraightUtils {
  static StraightLine createLine(final StraightArea area, final Line.Direction direction,
      final float ratio) {
    PointF one = new PointF();
    PointF two = new PointF();
    if (direction == Line.Direction.HORIZONTAL) {
      one.x = area.left();
      one.y = area.height() * ratio + area.top();
      two.x = area.right();
      two.y = area.height() * ratio + area.top();
    } else if (direction == Line.Direction.VERTICAL) {
      one.x = area.width() * ratio + area.left();
      one.y = area.top();
      two.x = area.width() * ratio + area.left();
      two.y = area.bottom();
    }

    StraightLine line = new StraightLine(one, two);

    if (direction == Line.Direction.HORIZONTAL) {
      line.attachLineStart = area.lineLeft;
      line.attachLineEnd = area.lineRight;

      line.setUpperLine(area.lineBottom);
      line.setLowerLine(area.lineTop);
    } else if (direction == Line.Direction.VERTICAL) {
      line.attachLineStart = area.lineTop;
      line.attachLineEnd = area.lineBottom;

      line.setUpperLine(area.lineRight);
      line.setLowerLine(area.lineLeft);
    }

    return line;
  }

  static List<StraightArea> cutArea(final StraightArea area, final StraightLine line) {
    List<StraightArea> list = new ArrayList<>();
    if (line.direction() == Line.Direction.HORIZONTAL) {
      StraightArea one = new StraightArea(area);
      one.lineBottom = line;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineTop = line;
      list.add(two);
    } else if (line.direction() == Line.Direction.VERTICAL) {
      StraightArea one = new StraightArea(area);
      one.lineRight = line;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineLeft = line;
      list.add(two);
    }

    return list;
  }

  static Pair<List<StraightLine>, List<StraightArea>> cutArea(final StraightArea area,
      final int horizontalSize, final int verticalSize) {
    List<StraightArea> areaList = new ArrayList<>();
    List<StraightLine> horizontalLines = new ArrayList<>(horizontalSize);

    StraightArea restArea = new StraightArea(area);
    for (int i = horizontalSize + 1; i > 1; i--) {
      StraightLine horizontalLine =
          createLine(restArea, Line.Direction.HORIZONTAL, (float) (i - 1) / i);
      horizontalLines.add(horizontalLine);
      restArea.lineBottom = horizontalLine;
    }
    List<StraightLine> verticalLines = new ArrayList<>();

    restArea = new StraightArea(area);
    for (int i = verticalSize + 1; i > 1; i--) {
      StraightLine verticalLine =
          createLine(restArea, Line.Direction.VERTICAL, (float) (i - 1) / i);
      verticalLines.add(verticalLine);
      StraightArea spiltArea = new StraightArea(restArea);
      spiltArea.lineLeft = verticalLine;

      for (int j = 0; j <= horizontalLines.size(); j++) {
        StraightArea blockArea = new StraightArea(spiltArea);
        if (j == 0) {
          blockArea.lineTop = horizontalLines.get(j);
        } else if (j == horizontalLines.size()) {
          blockArea.lineBottom = horizontalLines.get(j - 1);
        } else {
          blockArea.lineTop = horizontalLines.get(j);
          blockArea.lineBottom = horizontalLines.get(j - 1);
        }
        areaList.add(blockArea);
      }
      restArea.lineRight = verticalLine;
    }

    for (int j = 0; j <= horizontalLines.size(); j++) {
      StraightArea blockArea = new StraightArea(restArea);
      if (j == 0) {
        blockArea.lineTop = horizontalLines.get(j);
      } else if (j == horizontalLines.size()) {
        blockArea.lineBottom = horizontalLines.get(j - 1);
      } else {
        blockArea.lineTop = horizontalLines.get(j);
        blockArea.lineBottom = horizontalLines.get(j - 1);
      }
      areaList.add(blockArea);
    }

    List<StraightLine> lines = new ArrayList<>();
    lines.addAll(horizontalLines);
    lines.addAll(verticalLines);
    return new Pair<>(lines, areaList);
  }

  static List<StraightArea> cutAreaCross(final StraightArea area, final StraightLine horizontal,
      final StraightLine vertical) {
    List<StraightArea> list = new ArrayList<>();

    StraightArea one = new StraightArea(area);
    one.lineBottom = horizontal;
    one.lineRight = vertical;
    list.add(one);

    StraightArea two = new StraightArea(area);
    two.lineBottom = horizontal;
    two.lineLeft = vertical;
    list.add(two);

    StraightArea three = new StraightArea(area);
    three.lineTop = horizontal;
    three.lineRight = vertical;
    list.add(three);

    StraightArea four = new StraightArea(area);
    four.lineTop = horizontal;
    four.lineLeft = vertical;
    list.add(four);

    return list;
  }
}
