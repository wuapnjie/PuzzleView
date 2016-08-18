package com.xiaopo.flying.puzzle;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * the border to layout puzzle piece
 *
 * @see PuzzlePiece
 * each border consist of four lines : left,top,right,bottom
 * @see Line
 * <p>
 * Created by snowbean on 16-8-13.
 */
class Border implements Parcelable{
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

        PointF one = new PointF(0, 0);
        PointF two = new PointF(width, 0);
        PointF three = new PointF(0, height);
        PointF four = new PointF(width, height);

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
        return right() - left();
    }

    float centerY() {
        return bottom() - top();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.lineLeft, flags);
        dest.writeParcelable(this.lineTop, flags);
        dest.writeParcelable(this.lineRight, flags);
        dest.writeParcelable(this.lineBottom, flags);
    }

    protected Border(Parcel in) {
        this.lineLeft = in.readParcelable(Line.class.getClassLoader());
        this.lineTop = in.readParcelable(Line.class.getClassLoader());
        this.lineRight = in.readParcelable(Line.class.getClassLoader());
        this.lineBottom = in.readParcelable(Line.class.getClassLoader());
    }

    public static final Creator<Border> CREATOR = new Creator<Border>() {
        @Override
        public Border createFromParcel(Parcel source) {
            return new Border(source);
        }

        @Override
        public Border[] newArray(int size) {
            return new Border[size];
        }
    };
}
