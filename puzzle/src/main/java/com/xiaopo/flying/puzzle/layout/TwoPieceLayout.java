package com.xiaopo.flying.puzzle.layout;

import android.os.Parcel;
import android.util.Log;

import com.xiaopo.flying.puzzle.Line;

/**
 * Created by snowbean on 16-8-17.
 */
public class TwoPieceLayout extends NumberPieceLayout {
    private float mRadio = 1f / 2;

    public TwoPieceLayout(int theme) {
        super(theme);
    }

    public TwoPieceLayout(float radio, int theme) {
        super(theme);
        if (mRadio > 1) {
            Log.e(TAG, "CrossLayout: the radio can not greater than 1f");
            mRadio = 1f;
        }
        mRadio = radio;
    }

    @Override
    public int getThemeCount() {
        return 2;
    }

    @Override
    public void layout() {
        switch (mTheme) {
            case 0:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, mRadio);
                break;
            case 1:
                addLine(getOuterBorder(), Line.Direction.VERTICAL, mRadio);
                break;
            default:
                addLine(getOuterBorder(), Line.Direction.HORIZONTAL, mRadio);
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
        dest.writeFloat(this.mRadio);
    }

    protected TwoPieceLayout(Parcel in) {
        super(in);
        this.mRadio = in.readFloat();
    }

    public static final Creator<TwoPieceLayout> CREATOR = new Creator<TwoPieceLayout>() {
        @Override
        public TwoPieceLayout createFromParcel(Parcel source) {
            return new TwoPieceLayout(source);
        }

        @Override
        public TwoPieceLayout[] newArray(int size) {
            return new TwoPieceLayout[size];
        }
    };
}
