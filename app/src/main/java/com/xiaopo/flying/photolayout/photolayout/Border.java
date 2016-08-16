package com.xiaopo.flying.photolayout.photolayout;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by snowbean on 16-8-13.
 */
public class Border {
    Line lineLeft;
    Line lineTop;
    Line lineRight;
    Line lineBottom;

    public Border(Border src) {
        lineLeft = src.lineLeft;
        lineTop = src.lineTop;
        lineRight = src.lineRight;
        lineBottom = src.lineBottom;
    }

    public Border(RectF baseRect) {
        setBaseRect(baseRect);
    }

    public void setBaseRect(RectF baseRect) {
        float width = baseRect.width();
        float height = baseRect.height();

        PointF one = new PointF(0, 0);
        PointF two = new PointF(width, 0);
        PointF three = new PointF(0, height);
        PointF four = new PointF(width, height);

        lineLeft = new Line(one, three);
        lineTop = new Line(one, two);
        lineRight = new Line(two, four);
        lineBottom = new Line(three, four);
    }

    public float width() {
        return lineRight.start.x - lineLeft.start.x;
    }


    public float height() {
        return lineBottom.start.y - lineTop.start.y;
    }

    public float left() {
        return lineLeft.start.x;
    }

    public float top() {
        return lineTop.start.y;
    }

    public float right() {
        return lineRight.start.x;
    }

    public float bottom() {
        return lineBottom.start.y;
    }

    public RectF getRect() {
        return new RectF(
                left(),
                top(),
                right(),
                bottom());
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


    //    public List<RectF> addLine(RectF rectF, Line.Direction direction, float ratio) {
//        mRectList.remove(rectF);
//        Line line = BorderUtil.createLine(rectF, direction, ratio);
//        List<RectF> rectList = BorderUtil.cutRect(rectF, line);
//        lines.add(line);
//        mRectList.addAll(rectList);
//
//        return rectList;
//    }
//
//
//    public void cutRectEqualPart(RectF rectF, int part, Line.Direction direction) {
//        RectF temp = rectF;
//        for (int i = part; i > 1; i--) {
//            temp = addLine(temp, direction, (float) 1 / i).get(1);
//        }
//
//        System.out.println("hello");
//    }

}
