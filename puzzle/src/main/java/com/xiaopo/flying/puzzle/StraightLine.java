package com.xiaopo.flying.puzzle;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import com.xiaopo.flying.puzzle.slant.Line;

/**
 * the line to divide the rect border
 * Created by snowbean on 16-8-13.
 */
public class StraightLine {

  /**
   * for horizontal line, start means left, end means right
   * for vertical line, start means top, end means bottom
   */
  final PointF start;
  final PointF end;

  private Line.Direction direction = Line.Direction.HORIZONTAL;

  private StraightLine attachLineStart;
  private StraightLine attachLineEnd;

  private StraightLine mUpperLine;
  private StraightLine mLowerLine;

  private final RectF mBound = new RectF();

  @Override public String toString() {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The line is ")
        .append(direction.name())
        .append(",start point is ")
        .append(start)
        .append(",end point is ")
        .append(end)
        .append(",length is ")
        .append(length())
        .append("\n");

    if (attachLineStart != null) {
      stringBuilder.append("\n").append("attachLineStart is ").append(attachLineStart.toString());
    }

    if (attachLineEnd != null) {
      stringBuilder.append("\n").append("attachLineEnd is ").append(attachLineEnd.toString());
    }

    return stringBuilder.append("\n").toString();
  }

  public StraightLine(PointF start, PointF end) {
    this.start = start;
    this.end = end;

    if (start.x == end.x) {
      direction = Line.Direction.VERTICAL;
    } else if (start.y == end.y) {
      direction = Line.Direction.HORIZONTAL;
    } else {
      Log.d("StraightLine", "StraightLine: current only support two direction");
    }
  }

  public float length() {
    return (float) Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
  }

  public PointF centerPoint() {
    return new PointF((end.x - start.x) / 2, (end.y - start.y) / 2);
  }

  public float getPosition() {
    if (direction == Line.Direction.HORIZONTAL) {
      return start.y;
    } else {
      return start.x;
    }
  }

  public boolean contains(float x, float y, float extra) {
    if (direction == Line.Direction.HORIZONTAL) {
      mBound.left = start.x;
      mBound.right = end.x;
      mBound.top = start.y - extra / 2;
      mBound.bottom = start.y + extra / 2;
    } else if (direction == Line.Direction.VERTICAL) {
      mBound.top = start.y;
      mBound.bottom = end.y;
      mBound.left = start.x - extra / 2;
      mBound.right = start.x + extra / 2;
    }

    return mBound.contains(x, y);
  }

  public RectF getCenterBound(float position, float length, float borderStrokeWidth,
      boolean isStartLine) {
    if (direction == Line.Direction.HORIZONTAL) {
      mBound.left = position - length / 4;
      mBound.right = position + length / 4;
      if (isStartLine) {
        mBound.top = start.y - borderStrokeWidth * 1.5f + borderStrokeWidth / 2;
        mBound.bottom = start.y + borderStrokeWidth * 1.5f + borderStrokeWidth / 2;
      } else {
        mBound.top = start.y - borderStrokeWidth * 1.5f - borderStrokeWidth / 2;
        mBound.bottom = start.y + borderStrokeWidth * 1.5f - borderStrokeWidth / 2;
      }
    } else if (direction == Line.Direction.VERTICAL) {
      mBound.top = position - length / 4;
      mBound.bottom = position + length / 4;
      if (isStartLine) {
        mBound.left = start.x - borderStrokeWidth * 1.5f + borderStrokeWidth / 2;
        mBound.right = start.x + borderStrokeWidth * 1.5f + borderStrokeWidth / 2;
      } else {
        mBound.left = start.x - borderStrokeWidth * 1.5f - borderStrokeWidth / 2;
        mBound.right = start.x + borderStrokeWidth * 1.5f - borderStrokeWidth / 2;
      }
    }

    return mBound;
  }

  public void update() {
    if (direction == Line.Direction.HORIZONTAL) {
      if (attachLineStart != null) {
        start.x = attachLineStart.getPosition();
      }
      if (attachLineEnd != null) {
        end.x = attachLineEnd.getPosition();
      }
    } else if (direction == Line.Direction.VERTICAL) {
      if (attachLineStart != null) {
        start.y = attachLineStart.getPosition();
      }
      if (attachLineEnd != null) {
        end.y = attachLineEnd.getPosition();
      }
    }
  }

  public void moveTo(float position, float extra) {
    if (direction == Line.Direction.HORIZONTAL) {

      if (position < mLowerLine.start.y + extra || position > mUpperLine.start.y - extra) return;

      start.y = position;
      end.y = position;
    } else if (direction == Line.Direction.VERTICAL) {

      if (position < mLowerLine.start.x + extra || position > mUpperLine.start.x - extra) return;

      start.x = position;
      end.x = position;
    }
  }

  public StraightLine getAttachLineStart() {
    return attachLineStart;
  }

  public void setAttachLineStart(StraightLine attachLineStart) {
    this.attachLineStart = attachLineStart;
  }

  public StraightLine getAttachLineEnd() {
    return attachLineEnd;
  }

  public void setAttachLineEnd(StraightLine attachLineEnd) {
    this.attachLineEnd = attachLineEnd;
  }

  public Line.Direction getDirection() {
    return direction;
  }

  public StraightLine getUpperLine() {
    return mUpperLine;
  }

  public void setUpperLine(StraightLine upperLine) {
    mUpperLine = upperLine;
  }

  public StraightLine getLowerLine() {
    return mLowerLine;
  }

  public void setLowerLine(StraightLine lowerLine) {
    mLowerLine = lowerLine;
  }
}
