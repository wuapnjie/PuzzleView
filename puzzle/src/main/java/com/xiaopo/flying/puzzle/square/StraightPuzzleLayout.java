package com.xiaopo.flying.puzzle.square;

import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.base.Area;
import com.xiaopo.flying.puzzle.base.Line;
import com.xiaopo.flying.puzzle.base.PuzzleLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wupanjie
 */

public abstract class StraightPuzzleLayout implements PuzzleLayout {
  private SquareArea outerArea;

  private List<SquareArea> areas = new ArrayList<>();
  private List<Line> lines = new ArrayList<>();
  private List<Line> outerLines = new ArrayList<>(4);

  private Comparator<SquareArea> areaComparator = new StraightAreaComparator();

  public StraightPuzzleLayout() {

  }

  public StraightPuzzleLayout(RectF baseRect) {
    setOuterBounds(baseRect);
  }

  @Override public void setOuterBounds(RectF bounds) {
    float width = bounds.width();
    float height = bounds.height();

    PointF one = new PointF(bounds.left, bounds.top);
    PointF two = new PointF(bounds.right, bounds.top);
    PointF three = new PointF(bounds.left, bounds.bottom);
    PointF four = new PointF(bounds.right, bounds.bottom);

    StraightLine lineLeft = new StraightLine(one, three);
    StraightLine lineTop = new StraightLine(one, two);
    StraightLine lineRight = new StraightLine(two, four);
    StraightLine lineBottom = new StraightLine(three, four);

    outerLines.clear();

    outerLines.add(lineLeft);
    outerLines.add(lineTop);
    outerLines.add(lineRight);
    outerLines.add(lineBottom);

    outerArea = new SquareArea(bounds);

    areas.clear();
    areas.add(outerArea);
  }

  @Override public abstract void layout();

  @Override public int getAreaCount() {
    return areas.size();
  }

  @Override public List<Line> getOuterLines() {
    return outerLines;
  }

  @Override public List<Line> getLines() {
    return lines;
  }

  @Override public void update() {
    for (Line line : lines) {
      line.update();
    }
  }

  @Override public void reset() {
    lines.clear();
    areas.clear();
    areas.add(outerArea);
  }

  @Override public Area getArea(int position) {
    return areas.get(position);
  }

  protected List<SquareArea> addLine(int position, Line.Direction direction, float ratio) {
    SquareArea area = areas.get(position);
    areas.remove(area);
    StraightLine line = StraightUtils.createLine(area, direction, ratio);
    lines.add(line);

    List<SquareArea> increasedArea = StraightUtils.cutArea(area, line);
    areas.addAll(increasedArea);

    updateLineLimit();
    sortAreas();

    return increasedArea;
  }

  private void sortAreas() {
    Collections.sort(areas, areaComparator);
  }

  private void updateLineLimit() {
    for (Line line : lines) {
      updateUpperLine(line);
      updateLowerLine(line);
    }
  }

  private void updateLowerLine(final Line line) {
    for (Line l : lines) {
      if (l == line) {
        continue;
      }

      if (l.direction() != line.direction()) {
        continue;
      }

      if (l.attachStartLine() != line.attachStartLine()
          || l.attachEndLine() != line.attachEndLine()) {
        continue;
      }

      if (l.direction() == Line.Direction.HORIZONTAL) {
        if (l.minY() > line.lowerLine().maxY() && l.maxY() < line.minY()) {
          line.setLowerLine(l);
        }
      } else {
        if (l.minX() > line.lowerLine().maxX() && l.maxX() < line.minX()) {
          line.setLowerLine(l);
        }
      }
    }
  }

  private void updateUpperLine(final Line line) {
    for (Line l : lines) {
      if (l == line) {
        continue;
      }

      if (l.direction() != line.direction()) {
        continue;
      }

      if (l.attachStartLine() != line.attachStartLine()
          || l.attachEndLine() != line.attachEndLine()) {
        continue;
      }

      if (l.direction() == Line.Direction.HORIZONTAL) {
        if (l.maxY() < line.upperLine().minY() && l.minY() > line.maxY()) {
          line.setUpperLine(l);
        }
      } else {
        if (l.maxX() < line.upperLine().minX() && l.minX() > line.maxX()) {
          line.setUpperLine(l);
        }
      }
    }
  }
}
