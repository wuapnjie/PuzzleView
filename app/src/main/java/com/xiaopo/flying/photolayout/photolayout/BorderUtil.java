package com.xiaopo.flying.photolayout.photolayout;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-13.
 */
public class BorderUtil {
    private static final String TAG = "BorderUtil";

    public static Line createLine(final Border border, final Line.Direction direction, final float ratio) {
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

    public static List<Border> cutBorder(final Border border, final Line line) {
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


    public static List<Border> cutBorderCross(final Border border, final Line horizontal, final Line vertical) {
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
    public static Matrix createMatrix(Border border, Bitmap bitmap) {
        final RectF rectF = border.getRect();

        Matrix matrix = new Matrix();

        float offsetX = rectF.centerX() - bitmap.getWidth() / 2;
        float offsetY = rectF.centerY() - bitmap.getHeight() / 2;

        matrix.postTranslate(offsetX, offsetY);

        float scale;

        if (bitmap.getWidth() * rectF.height() > rectF.width() * bitmap.getHeight()) {
            scale = rectF.height() / bitmap.getHeight();
        } else {
            scale = rectF.width() / bitmap.getWidth();
        }

        matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY());

        return matrix;
    }
}
