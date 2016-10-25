package com.xiaopo.flying.puzzle;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Arrays;
import java.util.List;

/**
 * the border to layout puzzle piece
 *
 * @see PuzzlePiece
 * each border consist of four lines : left,top,right,bottom
 * @see Line
 * <p>
 * Created by snowbean on 16-8-13.
 */
class Border {
    Line lineLeft;
    Line lineTop;
    Line lineRight;
    Line lineBottom;

    Border(Border src) {
        lineLeft = src.lineLeft;
        lineTop = src.lineTop;
        lineRight = src.lineRight;
        lineBottom = src.lineBottom;
    }

    Border(RectF baseRect) {
        setBaseRect(baseRect);
    }

    private void setBaseRect(RectF baseRect) {
        float width = baseRect.width();
        float height = baseRect.height();

        PointF one = new PointF(baseRect.left, baseRect.top);
        PointF two = new PointF(baseRect.right, baseRect.top);
        PointF three = new PointF(baseRect.left, baseRect.bottom);
        PointF four = new PointF(baseRect.right, baseRect.bottom);

        lineLeft = new Line(one, three);
        lineTop = new Line(one, two);
        lineRight = new Line(two, four);
        lineBottom = new Line(three, four);
    }

    float width() {
        return lineRight.start.x - lineLeft.start.x;
    }


    float height() {
        return lineBottom.start.y - lineTop.start.y;
    }

    float left() {
        return lineLeft.start.x;
    }

    float top() {
        return lineTop.start.y;
    }

    float right() {
        return lineRight.start.x;
    }

    float bottom() {
        return lineBottom.start.y;
    }

    float centerX() {
        return (right() + left()) * 0.5f;
    }

    float centerY() {
        return (bottom() + top()) * 0.5f;
    }

    List<Line> getLines() {
        return Arrays.asList(lineLeft, lineTop, lineRight, lineBottom);
    }

    RectF getRect() {
        return new RectF(
                left(),
                top(),
                right(),
                bottom());
    }

    boolean contains(Line line) {
        return lineLeft == line || lineTop == line || lineRight == line || lineBottom == line;
    }


    @Override
    public String toString() {
        return "left line:\n" +
                lineLeft.toString() +
                "\ntop line:\n" +
                lineTop.toString() +
                "\nright line:\n" +
                lineRight.toString() +
                "\nbottom line:\n" +
                lineBottom.toString() +
                "\nthe rect is \n" +
                getRect().toString();
    }
}
