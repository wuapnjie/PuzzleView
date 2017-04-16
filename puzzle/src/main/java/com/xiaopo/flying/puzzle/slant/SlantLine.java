package com.xiaopo.flying.puzzle.slant;

import android.graphics.PointF;
import com.xiaopo.flying.puzzle.Line;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * 分为两种斜线，横谢线和竖线线
 * 横斜线-->start为左边的点，end为右边的点
 * 竖斜线-->start为上面的点，end为下面的点
 * @author wupanjie
 */

public class SlantLine {
  public PointF start;
  public PointF end;

  public final Line.Direction direction;

  public SlantLine attachLineStart;
  public SlantLine attachLineEnd;

  public SlantLine upperLine;
  public SlantLine lowerLine;

  public SlantLine(Line.Direction direction){
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

  //public boolean contains(float x, float y, float extra){
  //
  //}
}
