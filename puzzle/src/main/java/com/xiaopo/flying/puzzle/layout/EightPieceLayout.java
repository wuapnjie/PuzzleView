package com.xiaopo.flying.puzzle.layout;

import android.os.Parcel;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class EightPieceLayout extends NumberPieceLayout {
    public EightPieceLayout(int theme) {
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
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.VERTICAL);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 4f / 5);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 2f / 5);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 5);
                break;
            case 1:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.HORIZONTAL);
                addLine(getBorder(3), Line.Direction.VERTICAL, 4f / 5);
                addLine(getBorder(2), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(1), Line.Direction.VERTICAL, 2f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 5);
                break;
            case 2:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.VERTICAL);
                addLine(getBorder(3), Line.Direction.HORIZONTAL, 1f / 5);
                addLine(getBorder(2), Line.Direction.HORIZONTAL, 2f / 5);
                addLine(getBorder(1), Line.Direction.HORIZONTAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 4f / 5);
                break;
            case 3:
                cutBorderEqualPart(getOuterBorder(), 4, Line.Direction.HORIZONTAL);
                addLine(getBorder(3), Line.Direction.VERTICAL, 1f / 5);
                addLine(getBorder(2), Line.Direction.VERTICAL, 2f / 5);
                addLine(getBorder(1), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 4f / 5);
                break;
            case 4:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(1), 2, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.VERTICAL);
                break;
            case 5:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(2), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(1), 2, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(0), 3, Line.Direction.HORIZONTAL);
                break;
            case 6:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 4f / 5);
                cutBorderEqualPart(getBorder(1), 5, Line.Direction.VERTICAL);
                addLine(getBorder(0), Line.Direction.HORIZONTAL, 1f / 2);
                addLine(getBorder(1), Line.Direction.VERTICAL, 1f / 2);
                break;
            case 7:
                cutBorderEqualPart(getOuterBorder(), 3, Line.Direction.HORIZONTAL);
                cutBorderEqualPart(getBorder(2), 2, Line.Direction.VERTICAL);
                cutBorderEqualPart(getBorder(1), 3, Line.Direction.VERTICAL);
                addLine(getBorder(0), Line.Direction.VERTICAL, 3f / 5);
                addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 3);
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

    protected EightPieceLayout(Parcel in) {
        super(in);
    }

    public static final Creator<EightPieceLayout> CREATOR = new Creator<EightPieceLayout>() {
        @Override
        public EightPieceLayout createFromParcel(Parcel source) {
            return new EightPieceLayout(source);
        }

        @Override
        public EightPieceLayout[] newArray(int size) {
            return new EightPieceLayout[size];
        }
    };
}
