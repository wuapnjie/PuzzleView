package com.xiaopo.flying.puzzle.straight;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;
import com.xiaopo.flying.puzzle.Area;
import com.xiaopo.flying.puzzle.Line;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.xiaopo.flying.puzzle.straight.StraightUtils.createLine;
import static com.xiaopo.flying.puzzle.straight.StraightUtils.cutAreaCross;
import static com.xiaopo.flying.puzzle.straight.StraightUtils.cutAreaSpiral;

/**
 * @author wupanjie
 */
public abstract class StraightPuzzleLayout implements PuzzleLayout {
  private StraightArea outerArea;

  private List<StraightArea> areas = new ArrayList<>();
  private List<Line> lines = new ArrayList<>();
  private List<Line> outerLines = new ArrayList<>(4);

  private Comparator<StraightArea> areaComparator = new StraightArea.AreaComparator();

  protected StraightPuzzleLayout() {

  }

  @Override public void setOuterBounds(RectF bounds) {
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

    outerArea = new StraightArea(bounds);

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
      line.update(width(), height());
    }
  }

  @Override public float width() {
    return outerArea == null ? 0 : outerArea.width();
  }

  @Override public float height() {
    return outerArea == null ? 0 : outerArea.height();
  }

  @Override public void reset() {
    lines.clear();
    areas.clear();
    areas.add(outerArea);
  }

  @Override public Area getArea(int position) {
    return areas.get(position);
  }

  @Override public StraightArea getOuterArea() {
    return outerArea;
  }

  protected List<StraightArea> addLine(int position, Line.Direction direction, float ratio) {
    StraightArea area = areas.get(position);
    return addLine(area, direction, ratio);
  }

  private List<StraightArea> addLine(StraightArea area, Line.Direction direction, float ratio) {
    areas.remove(area);
    StraightLine line = createLine(area, direction, ratio);
    lines.add(line);

    List<StraightArea> increasedArea = StraightUtils.cutArea(area, line);
    areas.addAll(increasedArea);

    updateLineLimit();
    sortAreas();

    return increasedArea;
  }

  protected void cutAreaEqualPart(int position, int part, Line.Direction direction) {
    StraightArea temp = areas.get(position);
    for (int i = part; i > 1; i--) {
      temp = addLine(temp, direction, (float) (i - 1) / i).get(0);
    }
  }

  protected List<StraightArea> addCross(int position, float radio) {
    return addCross(position, radio, radio);
  }

  protected List<StraightArea> addCross(int position, float horizontalRadio, float verticalRadio) {
    StraightArea area = areas.get(position);
    areas.remove(area);
    StraightLine horizontal = createLine(area, Line.Direction.HORIZONTAL, horizontalRadio);
    StraightLine vertical = createLine(area, Line.Direction.VERTICAL, verticalRadio);
    lines.add(horizontal);
    lines.add(vertical);

    List<StraightArea> newAreas = cutAreaCross(area, horizontal, vertical);
    areas.addAll(newAreas);

    updateLineLimit();
    sortAreas();

    return newAreas;
  }

  protected List<StraightArea> cutAreaEqualPart(int position, int hSize, int vSize) {
    StraightArea area = areas.get(position);
    areas.remove(area);
    Pair<List<StraightLine>, List<StraightArea>> increased =
        StraightUtils.cutArea(area, hSize, vSize);
    List<StraightLine> newLines = increased.first;
    List<StraightArea> newAreas = increased.second;

    lines.addAll(newLines);
    areas.addAll(newAreas);

    updateLineLimit();
    sortAreas();

    return newAreas;
  }

  protected List<StraightArea> cutSpiral(int position) {
    StraightArea area = areas.get(position);
    areas.remove(area);
    Pair<List<StraightLine>, List<StraightArea>> spilt = cutAreaSpiral(area);

    lines.addAll(spilt.first);
    areas.addAll(spilt.second);

    updateLineLimit();
    sortAreas();

    return spilt.second;
  }

  private void sortAreas() {
    Collections.sort(areas, areaComparator);
  }

  private void updateLineLimit() {
    for (int i = 0; i < lines.size(); i++) {
      Line line = lines.get(i);
      updateUpperLine(line);
      updateLowerLine(line);
    }
  }

  private void updateLowerLine(final Line line) {
    for (int i = 0; i < lines.size(); i++) {
      Line l = lines.get(i);
      if (l == line) {
        continue;
      }

      if (l.direction() != line.direction()) {
        continue;
      }

      if (l.direction() == Line.Direction.HORIZONTAL) {
        if (l.maxX() <= line.minX() || line.maxX() <= l.minX()) continue;
        if (l.minY() > line.lowerLine().maxY() && l.maxY() < line.minY()) {
          line.setLowerLine(l);
        }
      } else {
        if (l.maxY() <= line.minY() || line.maxY() <= l.minY()) continue;
        if (l.minX() > line.lowerLine().maxX() && l.maxX() < line.minX()) {
          line.setLowerLine(l);
        }
      }
    }
  }

  private void updateUpperLine(final Line line) {
    for (int i = 0; i < lines.size(); i++) {
      Line l = lines.get(i);
      if (l == line) {
        continue;
      }

      if (l.direction() != line.direction()) {
        continue;
      }

      if (l.direction() == Line.Direction.HORIZONTAL) {
        if (l.maxX() <= line.minX() || line.maxX() <= l.minX()) continue;
        if (l.maxY() < line.upperLine().minY() && l.minY() > line.maxY()) {
          line.setUpperLine(l);
        }
      } else {
        if (l.maxY() <= line.minY() || line.maxY() <= l.minY()) continue;
        if (l.maxX() < line.upperLine().minX() && l.minX() > line.maxX()) {
          line.setUpperLine(l);
        }
      }
    }
  }
}
