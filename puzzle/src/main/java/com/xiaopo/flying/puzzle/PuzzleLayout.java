package com.xiaopo.flying.puzzle;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * contains all lines and borders.
 * we can add line to divider a border to number of borders.
 * <p>
 * <p>
 * to determine the which border to layout puzzle piece.
 *
 * @see Border
 * <p>
 * Created by snowbean on 16-8-13.
 */
public abstract class PuzzleLayout implements Parcelable {
    protected static final String TAG = "PuzzleLayout";

    private Border mOuterBorder;

    private List<Border> mBorders = new ArrayList<>();
    private List<Line> mLines = new ArrayList<>();
    private List<Line> mOuterLines = new ArrayList<>(4);

    private Comparator<Border> mBorderComparator = new BorderComparator();

    public PuzzleLayout() {

    }

    public PuzzleLayout(RectF baseRect) {
        setOuterBorder(baseRect);
    }

    public void setOuterBorder(RectF baseRect) {
        float width = baseRect.width();
        float height = baseRect.height();

        PointF one = new PointF(0, 0);
        PointF two = new PointF(width, 0);
        PointF three = new PointF(0, height);
        PointF four = new PointF(width, height);

        Line lineLeft = new Line(one, three);
        Line lineTop = new Line(one, two);
        Line lineRight = new Line(two, four);
        Line lineBottom = new Line(three, four);

        mOuterLines.clear();

        mOuterLines.add(lineLeft);
        mOuterLines.add(lineTop);
        mOuterLines.add(lineRight);
        mOuterLines.add(lineBottom);

        mOuterBorder = new Border(baseRect);

        mBorders.clear();
        mBorders.add(mOuterBorder);
    }

    public abstract void layout();

    public List<Border> addLine(Border border, Line.Direction direction, float ratio) {
        mBorders.remove(border);
        Line line = BorderUtil.createLine(border, direction, ratio);
        mLines.add(line);

        List<Border> borders = BorderUtil.cutBorder(border, line);
        mBorders.addAll(borders);

        updateLineLimit();
        Collections.sort(mBorders, mBorderComparator);

        return borders;
    }

    public void cutBorderEqualPart(Border border, int part, Line.Direction direction) {
        Border temp = border;
        for (int i = part; i > 1; i--) {
            temp = addLine(temp, direction, (float) (i - 1) / i).get(0);
        }
    }

    public List<Border> addCross(Border border, float radio) {
        return addCross(border, radio, radio);
    }

    public List<Border> addCross(Border border, float horizontalRadio, float verticalRadio) {
        mBorders.remove(border);
        Line horizontal = BorderUtil.createLine(border, Line.Direction.HORIZONTAL, horizontalRadio);
        Line vertical = BorderUtil.createLine(border, Line.Direction.VERTICAL, verticalRadio);
        mLines.add(horizontal);
        mLines.add(vertical);

        List<Border> borders = BorderUtil.cutBorderCross(border, horizontal, vertical);
        mBorders.addAll(borders);

        updateLineLimit();

        if (mBorderComparator == null) {
            mBorderComparator = new BorderComparator();
        }
        Collections.sort(mBorders, mBorderComparator);

        return borders;
    }

    private void updateLineLimit() {
        for (Line line : mLines) {
            updateUpperLine(line);
            updateLowerLine(line);
        }
    }

    private void updateLowerLine(final Line line) {
        for (Line l : mLines) {
            if (l.getPosition() > line.getLowerLine().getPosition()
                    && l.getPosition() < line.getPosition()
                    && l.getDirection() == line.getDirection()) {
                line.setLowerLine(l);
            }
        }
    }

    private void updateUpperLine(final Line line) {
        for (Line l : mLines) {
            if (l.getPosition() < line.getUpperLine().getPosition()
                    && l.getPosition() > line.getPosition()
                    && l.getDirection() == line.getDirection()) {
                line.setUpperLine(l);
            }
        }
    }

    public void reset() {
        mLines.clear();
        mBorders.clear();
        mBorders.add(mOuterBorder);
    }

    public int getBorderSize() {
        return mBorders.size();
    }

    public Border getBorder(int index) {
        return mBorders.get(index);
    }

    public List<Line> getLines() {
        return mLines;
    }

    public List<Border> getBorders() {
        return mBorders;
    }

    public Border getOuterBorder() {
        return mOuterBorder;
    }

    public List<Line> getOuterLines() {
        return mOuterLines;
    }

    public void update() {
        for (Line line : mLines) {
            line.update();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mOuterBorder, flags);
        dest.writeTypedList(this.mBorders);
        dest.writeTypedList(this.mLines);
        dest.writeTypedList(this.mOuterLines);
    }

    protected PuzzleLayout(Parcel in) {
        this.mOuterBorder = in.readParcelable(Border.class.getClassLoader());
        this.mBorders = in.createTypedArrayList(Border.CREATOR);
        this.mLines = in.createTypedArrayList(Line.CREATOR);
        this.mOuterLines = in.createTypedArrayList(Line.CREATOR);
    }

}
