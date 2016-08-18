package com.xiaopo.flying.puzzle.layout;

import android.os.Parcel;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class SevenPieceLayout extends NumberPieceLayout {
    public SevenPieceLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 8;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 2);
                cutBorderEqualPart(getBorder(1), 4, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            case 1:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f / 2);
                cutBorderEqualPart(getBorder(1), 4, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 2:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 2f / 3);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.VERTICAL);
                addCross(getBorder(0), 1f / 2);
                break;
            case 3:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 4:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.VERTICAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.VERTICAL, 2f / 5);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            case 5:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 5);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 6:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, 1f / 4);
                addLine(getBorder(1), Line.Direction.VERTICAL, 2f / 3);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 3f / 4);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 1f / 3);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                break;
            case 7:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 4);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 3);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            default:
                break;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected SevenPieceLayout(Parcel in) {
        super(in);
    }

    public static final Creator<SevenPieceLayout> CREATOR = new Creator<SevenPieceLayout>() {
        @Override
        public SevenPieceLayout createFromParcel(Parcel source) {
            return new SevenPieceLayout(source);
        }

        @Override
        public SevenPieceLayout[] newArray(int size) {
            return new SevenPieceLayout[size];
        }
    };
}
