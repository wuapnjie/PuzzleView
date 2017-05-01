package com.xiaopo.flying.puzzle.straight;

import android.graphics.PointF;
import com.xiaopo.flying.puzzle.Line;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-13.
 */
class StraightUtils {
  private static final String TAG = "SquareAreaUtils";

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

  static List<StraightArea> cutArea(final StraightArea area, final StraightLine l1,
      final StraightLine l2, final StraightLine l3, Line.Direction direction) {
    List<StraightArea> list = new ArrayList<>();
    if (direction == Line.Direction.HORIZONTAL) {
      StraightArea one = new StraightArea(area);
      one.lineRight = l3;
      one.lineBottom = l1;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineLeft = l3;
      two.lineBottom = l1;
      list.add(two);

      StraightArea three = new StraightArea(area);
      three.lineRight = l3;
      three.lineTop = l1;
      three.lineBottom = l2;
      list.add(three);

      StraightArea four = new StraightArea(area);
      four.lineLeft = l3;
      four.lineTop = l1;
      four.lineBottom = l2;
      list.add(four);

      StraightArea five = new StraightArea(area);
      five.lineRight = l3;
      five.lineTop = l2;
      list.add(five);

      StraightArea six = new StraightArea(area);
      six.lineLeft = l3;
      six.lineTop = l2;
      list.add(six);
    } else if (direction == Line.Direction.VERTICAL) {

      StraightArea one = new StraightArea(area);
      one.lineRight = l1;
      one.lineBottom = l3;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineLeft = l1;
      two.lineBottom = l3;
      two.lineRight = l2;
      list.add(two);

      StraightArea three = new StraightArea(area);
      three.lineLeft = l2;
      three.lineBottom = l3;
      list.add(three);

      StraightArea four = new StraightArea(area);
      four.lineRight = l1;
      four.lineTop = l3;
      list.add(four);

      StraightArea five = new StraightArea(area);
      five.lineLeft = l1;
      five.lineRight = l2;
      five.lineTop = l3;
      list.add(five);

      StraightArea six = new StraightArea(area);
      six.lineLeft = l2;
      six.lineTop = l3;
      list.add(six);
    }

    return list;
  }

  static List<StraightArea> cutArea(final StraightArea area, final StraightLine l1,
      final StraightLine l2, final StraightLine l3, final StraightLine l4,
      Line.Direction direction) {
    List<StraightArea> list = new ArrayList<>();
    if (direction == Line.Direction.HORIZONTAL) {

      StraightArea one = new StraightArea(area);
      one.lineRight = l4;
      one.lineBottom = l1;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineLeft = l4;
      two.lineBottom = l1;
      list.add(two);

      StraightArea three = new StraightArea(area);
      three.lineRight = l4;
      three.lineTop = l1;
      three.lineBottom = l2;
      list.add(three);

      StraightArea four = new StraightArea(area);
      four.lineLeft = l4;
      four.lineTop = l1;
      four.lineBottom = l2;
      list.add(four);

      StraightArea five = new StraightArea(area);
      five.lineRight = l4;
      five.lineTop = l2;
      five.lineBottom = l3;
      list.add(five);

      StraightArea six = new StraightArea(area);
      six.lineLeft = l4;
      six.lineTop = l2;
      six.lineBottom = l3;
      list.add(six);

      StraightArea seven = new StraightArea(area);
      seven.lineRight = l4;
      seven.lineTop = l3;
      list.add(seven);

      StraightArea eight = new StraightArea(area);
      eight.lineLeft = l4;
      eight.lineTop = l3;
      list.add(eight);
    } else if (direction == Line.Direction.VERTICAL) {

      StraightArea one = new StraightArea(area);
      one.lineRight = l1;
      one.lineBottom = l4;
      list.add(one);

      StraightArea two = new StraightArea(area);
      two.lineLeft = l1;
      two.lineBottom = l4;
      two.lineRight = l2;
      list.add(two);

      StraightArea three = new StraightArea(area);
      three.lineLeft = l2;
      three.lineRight = l3;
      three.lineBottom = l4;
      list.add(three);

      StraightArea four = new StraightArea(area);
      four.lineLeft = l3;
      four.lineBottom = l4;
      list.add(four);

      StraightArea five = new StraightArea(area);
      five.lineRight = l1;
      five.lineTop = l4;
      list.add(five);

      StraightArea six = new StraightArea(area);
      six.lineLeft = l1;
      six.lineRight = l2;
      six.lineTop = l4;
      list.add(six);

      StraightArea seven = new StraightArea(area);
      seven.lineLeft = l2;
      seven.lineRight = l3;
      seven.lineTop = l4;
      list.add(seven);

      StraightArea eight = new StraightArea(area);
      eight.lineLeft = l3;
      eight.lineTop = l4;
      list.add(eight);
    }

    return list;
  }

  static List<StraightArea> cutArea(final StraightArea area, final StraightLine l1,
      final StraightLine l2, final StraightLine l3, final StraightLine l4) {
    List<StraightArea> list = new ArrayList<>();

    StraightArea one = new StraightArea(area);
    one.lineRight = l3;
    one.lineBottom = l1;
    list.add(one);

    StraightArea two = new StraightArea(area);
    two.lineLeft = l3;
    two.lineRight = l4;
    two.lineBottom = l1;
    list.add(two);

    StraightArea three = new StraightArea(area);
    three.lineLeft = l4;
    three.lineBottom = l1;
    list.add(three);

    StraightArea four = new StraightArea(area);
    four.lineRight = l3;
    four.lineTop = l1;
    four.lineBottom = l2;
    list.add(four);

    StraightArea five = new StraightArea(area);
    five.lineRight = l4;
    five.lineLeft = l3;
    five.lineTop = l1;
    five.lineBottom = l2;
    list.add(five);

    StraightArea six = new StraightArea(area);
    six.lineLeft = l4;
    six.lineTop = l1;
    six.lineBottom = l2;
    list.add(six);

    StraightArea seven = new StraightArea(area);
    seven.lineRight = l3;
    seven.lineTop = l2;
    list.add(seven);

    StraightArea eight = new StraightArea(area);
    eight.lineRight = l4;
    eight.lineLeft = l3;
    eight.lineTop = l2;
    list.add(eight);

    StraightArea nine = new StraightArea(area);
    nine.lineLeft = l4;
    nine.lineTop = l2;
    list.add(nine);

    return list;
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
