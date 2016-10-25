package com.xiaopo.flying.puzzle;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-13.
 */
class BorderUtil {
    private static final String TAG = "BorderUtil";

    static Line createLine(final Border border, final Line.Direction direction, final float ratio) {
        PointF one = new PointF();
        PointF two = new PointF();
        if (direction == Line.Direction.HORIZONTAL) {
            one.x = border.left();
            one.y = border.height() * ratio + border.top();
            two.x = border.right();
            two.y = border.height() * ratio + border.top();
        } else if (direction == Line.Direction.VERTICAL) {
            one.x = border.width() * ratio + border.left();
            one.y = border.top();
            two.x = border.width() * ratio + border.left();
            two.y = border.bottom();
        }

        Line line = new Line(one, two);

        if (direction == Line.Direction.HORIZONTAL) {
            line.setAttachLineStart(border.lineLeft);
            line.setAttachLineEnd(border.lineRight);

            line.setUpperLine(border.lineBottom);
            line.setLowerLine(border.lineTop);

        } else if (direction == Line.Direction.VERTICAL) {
            line.setAttachLineStart(border.lineTop);
            line.setAttachLineEnd(border.lineBottom);

            line.setUpperLine(border.lineRight);
            line.setLowerLine(border.lineLeft);
        }

        return line;
    }

    static List<Border> cutBorder(final Border border, final Line line) {
        List<Border> list = new ArrayList<>();
        if (line.getDirection() == Line.Direction.HORIZONTAL) {
            Border one = new Border(border);
            one.lineBottom = line;
            list.add(one);

            Border two = new Border(border);
            two.lineTop = line;
            list.add(two);
        } else if (line.getDirection() == Line.Direction.VERTICAL) {
            Border one = new Border(border);
            one.lineRight = line;
            list.add(one);

            Border two = new Border(border);
            two.lineLeft = line;
            list.add(two);
        }

        return list;
    }

    static List<Border> cutBorder(final Border border, final Line l1, final Line l2, final Line l3, Line.Direction direction) {
        List<Border> list = new ArrayList<>();
        if (direction == Line.Direction.HORIZONTAL) {
            Border one = new Border(border);
            one.lineRight = l3;
            one.lineBottom = l1;
            list.add(one);

            Border two = new Border(border);
            two.lineLeft = l3;
            two.lineBottom = l1;
            list.add(two);

            Border three = new Border(border);
            three.lineRight = l3;
            three.lineTop = l1;
            three.lineBottom = l2;
            list.add(three);

            Border four = new Border(border);
            four.lineLeft = l3;
            four.lineTop = l1;
            four.lineBottom = l2;
            list.add(four);

            Border five = new Border(border);
            five.lineRight = l3;
            five.lineTop = l2;
            list.add(five);

            Border six = new Border(border);
            six.lineLeft = l3;
            six.lineTop = l2;
            list.add(six);
        } else if (direction == Line.Direction.VERTICAL) {

            Border one = new Border(border);
            one.lineRight = l1;
            one.lineBottom = l3;
            list.add(one);

            Border two = new Border(border);
            two.lineLeft = l1;
            two.lineBottom = l3;
            two.lineRight = l2;
            list.add(two);

            Border three = new Border(border);
            three.lineLeft = l2;
            three.lineBottom = l3;
            list.add(three);

            Border four = new Border(border);
            four.lineRight = l1;
            four.lineTop = l3;
            list.add(four);

            Border five = new Border(border);
            five.lineLeft = l1;
            five.lineRight = l2;
            five.lineTop = l3;
            list.add(five);

            Border six = new Border(border);
            six.lineLeft = l2;
            six.lineTop = l3;
            list.add(six);
        }

        return list;
    }

    static List<Border> cutBorder(final Border border, final Line l1, final Line l2, final Line l3, final Line l4, Line.Direction direction) {
        List<Border> list = new ArrayList<>();
        if (direction == Line.Direction.HORIZONTAL) {

            Border one = new Border(border);
            one.lineRight = l4;
            one.lineBottom = l1;
            list.add(one);

            Border two = new Border(border);
            two.lineLeft = l4;
            two.lineBottom = l1;
            list.add(two);

            Border three = new Border(border);
            three.lineRight = l4;
            three.lineTop = l1;
            three.lineBottom = l2;
            list.add(three);

            Border four = new Border(border);
            four.lineLeft = l4;
            four.lineTop = l1;
            four.lineBottom = l2;
            list.add(four);

            Border five = new Border(border);
            five.lineRight = l4;
            five.lineTop = l2;
            five.lineBottom = l3;
            list.add(five);

            Border six = new Border(border);
            six.lineLeft = l4;
            six.lineTop = l2;
            six.lineBottom = l3;
            list.add(six);

            Border seven = new Border(border);
            seven.lineRight = l4;
            seven.lineTop = l3;
            list.add(seven);

            Border eight = new Border(border);
            eight.lineLeft = l4;
            eight.lineTop = l3;
            list.add(eight);

        } else if (direction == Line.Direction.VERTICAL) {

            Border one = new Border(border);
            one.lineRight = l1;
            one.lineBottom = l4;
            list.add(one);

            Border two = new Border(border);
            two.lineLeft = l1;
            two.lineBottom = l4;
            two.lineRight = l2;
            list.add(two);

            Border three = new Border(border);
            three.lineLeft = l2;
            three.lineRight = l3;
            three.lineBottom = l4;
            list.add(three);

            Border four = new Border(border);
            four.lineLeft = l3;
            four.lineBottom = l4;
            list.add(four);

            Border five = new Border(border);
            five.lineRight = l1;
            five.lineTop = l4;
            list.add(five);

            Border six = new Border(border);
            six.lineLeft = l1;
            six.lineRight = l2;
            six.lineTop = l4;
            list.add(six);

            Border seven = new Border(border);
            seven.lineLeft = l2;
            seven.lineRight = l3;
            seven.lineTop = l4;
            list.add(seven);

            Border eight = new Border(border);
            eight.lineLeft = l3;
            eight.lineTop = l4;
            list.add(eight);
        }

        return list;
    }

    static List<Border> cutBorder(final Border border, final Line l1, final Line l2, final Line l3, final Line l4) {
        List<Border> list = new ArrayList<>();

        Border one = new Border(border);
        one.lineRight = l3;
        one.lineBottom = l1;
        list.add(one);

        Border two = new Border(border);
        two.lineLeft = l3;
        two.lineRight = l4;
        two.lineBottom = l1;
        list.add(two);

        Border three = new Border(border);
        three.lineLeft = l4;
        three.lineBottom = l1;
        list.add(three);

        Border four = new Border(border);
        four.lineRight = l3;
        four.lineTop = l1;
        four.lineBottom = l2;
        list.add(four);

        Border five = new Border(border);
        five.lineRight = l4;
        five.lineLeft = l3;
        five.lineTop = l1;
        five.lineBottom = l2;
        list.add(five);

        Border six = new Border(border);
        six.lineLeft = l4;
        six.lineTop = l1;
        six.lineBottom = l2;
        list.add(six);

        Border seven = new Border(border);
        seven.lineRight = l3;
        seven.lineTop = l2;
        list.add(seven);

        Border eight = new Border(border);
        eight.lineRight = l4;
        eight.lineLeft = l3;
        eight.lineTop = l2;
        list.add(eight);

        Border nine = new Border(border);
        nine.lineLeft = l4;
        nine.lineTop = l2;
        list.add(nine);

        return list;
    }

    static List<Border> cutBorderCross(final Border border, final Line horizontal, final Line vertical) {
        List<Border> list = new ArrayList<>();

        Border one = new Border(border);
        one.lineBottom = horizontal;
        one.lineRight = vertical;
        list.add(one);

        Border two = new Border(border);
        two.lineBottom = horizontal;
        two.lineLeft = vertical;
        list.add(two);

        Border three = new Border(border);
        three.lineTop = horizontal;
        three.lineRight = vertical;
        list.add(three);

        Border four = new Border(border);
        four.lineTop = horizontal;
        four.lineLeft = vertical;
        list.add(four);

        return list;
    }

    /**
     * create a matrix which let bitmap centerCrop in the border rect
     */
    static Matrix createMatrix(Border border, Bitmap bitmap, float extraSize) {
        return createMatrix(border, bitmap.getWidth(), bitmap.getHeight(), extraSize);
    }

    static Matrix createMatrix(Border border, Drawable drawable, float extraSize) {
        return createMatrix(border, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), extraSize);
    }


    static Matrix createMatrix(Border border, int width, int height, float extraSize) {
        final RectF rectF = border.getRect();

        Matrix matrix = new Matrix();

        float offsetX = rectF.centerX() - width / 2;
        float offsetY = rectF.centerY() - height / 2;

        matrix.postTranslate(offsetX, offsetY);

        float scale;

        if (width * rectF.height() > rectF.width() * height) {
            scale = (rectF.height() + extraSize) / height;
        } else {
            scale = (rectF.width() + extraSize) / width;
        }

        matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY());

        return matrix;
    }
}
