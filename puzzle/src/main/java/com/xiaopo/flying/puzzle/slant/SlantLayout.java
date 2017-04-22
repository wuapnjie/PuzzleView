package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.Line;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.xiaopo.flying.puzzle.slant.SlantUtil.*;

/**
 * 斜线布局，外围区域为一矩形
 *
 * @author wupanjie
 */

public abstract class SlantLayout {
  private SlantArea outerArea;

  private List<SlantLine> outerLines = new ArrayList<>();
  private List<SlantArea> areas = new ArrayList<>();
  private List<SlantLine> lines = new ArrayList<>();

  private Comparator<SlantArea> slantAreaComparator = new SlantAreaComparator();

  public SlantLayout() {

  }

  public SlantLayout(RectF outerRect) {
    setOuterBorder(outerRect);
  }

  public void setOuterBorder(RectF baseRect) {
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

  protected void addLine(int position, Line.Direction direction, float startRadio, float endRadio) {
    SlantArea area = areas.get(position);
    areas.remove(area);
    SlantLine line = createSlantLine(area, direction, startRadio, endRadio);
    lines.add(line);

    List<SlantArea> increasedArea = cutArea(area, line);

    areas.addAll(increasedArea);

    // TODO 增加一些边界判断

    sortArea();
  }

  public List<SlantLine> getOuterLines() {
    return outerLines;
  }

  public SlantArea getOuterArea() {
    return outerArea;
  }

  public List<SlantArea> getAreas() {
    return areas;
  }

  public SlantArea getArea(int position) {
    return areas.get(position);
  }

  public List<SlantLine> getLines() {
    return lines;
  }

  public void reset() {
    outerArea = null;
    outerLines.clear();
    areas.clear();
    lines.clear();
  }

  public void update(){
    for (SlantLine line : lines){
      line.update();
    }
  }

  private void sortArea(){
    Collections.sort(areas, slantAreaComparator);
  }
}
