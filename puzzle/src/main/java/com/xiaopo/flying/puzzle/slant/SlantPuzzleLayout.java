package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.Area;
import com.xiaopo.flying.puzzle.Line;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.xiaopo.flying.puzzle.slant.SlantUtils.createLine;
import static com.xiaopo.flying.puzzle.slant.SlantUtils.cutArea;
import static com.xiaopo.flying.puzzle.slant.SlantUtils.cutAreaCross;

/**
 * 斜线布局，外围区域为一矩形
 *
 * @author wupanjie
 */

public abstract class SlantPuzzleLayout implements PuzzleLayout {
  private SlantArea outerArea;

  private List<Line> outerLines = new ArrayList<>(4);
  private List<SlantArea> areas = new ArrayList<>();
  private List<Line> lines = new ArrayList<>();
  private List<CrossoverPointF> crossoverPoints = new ArrayList<>();

  private Comparator<SlantArea> areaComparator = new SlantArea.AreaComparator();

  public SlantPuzzleLayout() {

  }

  public SlantPuzzleLayout(RectF outerRect) {
    setOuterBounds(outerRect);
  }

  @Override public void setOuterBounds(RectF baseRect) {
    reset();

    PointF leftTop = new PointF(baseRect.left, baseRect.top);
    PointF rightTop = new PointF(baseRect.right, baseRect.top);
    PointF leftBottom = new PointF(baseRect.left, baseRect.bottom);
    PointF rightBottom = new PointF(baseRect.right, baseRect.bottom);

    SlantLine lineLeft = new SlantLine(leftTop, leftBottom, Line.Direction.VERTICAL);
    SlantLine lineTop = new SlantLine(leftTop, rightTop, Line.Direction.HORIZONTAL);
    SlantLine lineRight = new SlantLine(rightTop, rightBottom, Line.Direction.VERTICAL);
    SlantLine lineBottom = new SlantLine(leftBottom, rightBottom, Line.Direction.HORIZONTAL);

    outerLines.clear();

    outerLines.add(lineLeft);
    outerLines.add(lineTop);
    outerLines.add(lineRight);
    outerLines.add(lineBottom);

    outerArea = new SlantArea();
    outerArea.lineLeft = lineLeft;
    outerArea.lineTop = lineTop;
    outerArea.lineRight = lineRight;
    outerArea.lineBottom = lineBottom;

    outerArea.leftTop = leftTop;
    outerArea.leftBottom = leftBottom;
    outerArea.rightTop = rightTop;
    outerArea.rightBottom = rightBottom;

    areas.clear();
    areas.add(outerArea);
  }

  public abstract void layout();

  private void updateLineLimit() {
    for (Line line : lines) {
      updateUpperLine(line);
      updateLowerLine(line);
    }
  }

  private void updateLowerLine(final Line line) {
    for (Line l : lines) {
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

  @Override public int getAreaCount() {
    return areas.size();
  }

  @Override public void reset() {
    lines.clear();
    areas.clear();
    areas.add(outerArea);
    crossoverPoints.clear();
  }

  @Override public void update() {
    for (Line line : lines) {
      line.update(width(), height());
    }

    // after line update
    for (CrossoverPointF point : crossoverPoints) {
      point.update();
    }
  }

  @Override public float width() {
    return outerArea == null ? 0 : outerArea.width();
  }

  @Override public float height() {
    return outerArea == null ? 0 : outerArea.height();
  }

  private void sortAreas() {
    Collections.sort(areas, areaComparator);
  }

  @Override public List<Line> getOuterLines() {
    return outerLines;
  }

  @Override public Area getOuterArea() {
    return outerArea;
  }

  public List<SlantArea> getAreas() {
    return areas;
  }

  @Override public SlantArea getArea(int position) {
    return areas.get(position);
  }

  @Override public List<Line> getLines() {
    return lines;
  }

  protected List<SlantArea> addLine(int position, Line.Direction direction, float radio) {
    return addLine(position, direction, radio, radio);
  }

  protected List<SlantArea> addLine(int position, Line.Direction direction, float startRadio,
      float endRadio) {
    SlantArea area = areas.get(position);
    areas.remove(area);
    SlantLine line = createLine(area, direction, startRadio, endRadio);
    lines.add(line);

    List<SlantArea> increasedAreas = cutArea(area, line);

    areas.addAll(increasedAreas);

    updateLineLimit();
    sortAreas();

    return increasedAreas;
  }

  protected List<SlantArea> addCross(int position, float startRadio1, float endRadio1,
      float startRadio2, float endRadio2) {
    SlantArea area = areas.get(position);
    areas.remove(area);

    SlantLine horizontal = createLine(area, Line.Direction.HORIZONTAL, startRadio1, endRadio1);
    SlantLine vertical = createLine(area, Line.Direction.VERTICAL, startRadio2, endRadio2);
    lines.add(horizontal);
    lines.add(vertical);

    List<SlantArea> increasedAreas = cutAreaCross(area, horizontal, vertical, crossoverPoints);

    areas.addAll(increasedAreas);
    sortAreas();

    return increasedAreas;
  }
}
