package com.xiaopo.flying.puzzle;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * the line to divide the rect border
 * Created by snowbean on 16-8-13.
 */
public class Line {

    enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * for horizontal line, start means left, end means right
     * for vertical line, start means top, end means bottom
     */
    final PointF start;
    final PointF end;

    private Direction direction = Direction.HORIZONTAL;

    private Line attachLineStart;
    private Line attachLineEnd;

    private Line mUpperLine;
    private Line mLowerLine;

    private RectF rectF = new RectF();

    @Override
    public String toString() {

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
            stringBuilder.append("\n")
                    .append("attachLineStart is ")
                    .append(attachLineStart.toString());
        }

        if (attachLineEnd != null) {
            stringBuilder.append("\n")
                    .append("attachLineEnd is ")
                    .append(attachLineEnd.toString());
        }

        return stringBuilder
                .append("\n")
                .toString();
    }

    public Line(PointF start, PointF end) {
        this.start = start;
        this.end = end;

        if (start.x == end.x) {
            direction = Direction.VERTICAL;
        } else if (start.y == end.y) {
            direction = Direction.HORIZONTAL;
        } else {
            Log.d("Line", "Line: current only support two direction");
        }
    }

    public float length() {
        return (float) Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
    }

    public float getPosition() {
        if (direction == Direction.HORIZONTAL) {
            return start.y;
        } else {
            return start.x;
        }
    }

    public boolean contains(float x, float y, float extra) {
        if (direction == Direction.HORIZONTAL) {
            rectF.left = start.x;
            rectF.right = end.x;
            rectF.top = start.y - extra / 2;
            rectF.bottom = start.y + extra / 2;
        } else if (direction == Direction.VERTICAL) {
            rectF.top = start.y;
            rectF.bottom = end.y;
            rectF.left = start.x - extra / 2;
            rectF.right = start.x + extra / 2;
        }

        return rectF.contains(x, y);
    }

    public void update() {
        if (direction == Direction.HORIZONTAL) {
            if (attachLineStart != null) {
                start.x = attachLineStart.getPosition();
            }
            if (attachLineEnd != null) {
                end.x = attachLineEnd.getPosition();
            }
        } else if (direction == Direction.VERTICAL) {
            if (attachLineStart != null) {
                start.y = attachLineStart.getPosition();
            }
            if (attachLineEnd != null) {
                end.y = attachLineEnd.getPosition();
            }
        }
    }

    public void moveTo(float position, float extra) {
        if (direction == Direction.HORIZONTAL) {

            if (position < mLowerLine.start.y + extra || position > mUpperLine.start.y - extra)
                return;

            start.y = position;
            end.y = position;
        } else if (direction == Direction.VERTICAL) {

            if (position < mLowerLine.start.x + extra || position > mUpperLine.start.x - extra)
                return;

            start.x = position;
            end.x = position;
        }
    }


    public Line getAttachLineStart() {
        return attachLineStart;
    }

    public void setAttachLineStart(Line attachLineStart) {
        this.attachLineStart = attachLineStart;
    }

    public Line getAttachLineEnd() {
        return attachLineEnd;
    }

    public void setAttachLineEnd(Line attachLineEnd) {
        this.attachLineEnd = attachLineEnd;
    }

    public Direction getDirection() {
        return direction;
    }


    public Line getUpperLine() {
        return mUpperLine;
    }

    public void setUpperLine(Line upperLine) {
        mUpperLine = upperLine;
    }

    public Line getLowerLine() {
        return mLowerLine;
    }

    public void setLowerLine(Line lowerLine) {
        mLowerLine = lowerLine;
    }
}
