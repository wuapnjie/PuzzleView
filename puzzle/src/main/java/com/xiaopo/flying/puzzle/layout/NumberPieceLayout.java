package com.xiaopo.flying.puzzle.layout;

import android.os.Parcel;
import android.util.Log;

import com.xiaopo.flying.puzzle.PuzzleLayout;

/**
 * Created by snowbean on 16-8-17.
 */
public abstract class NumberPieceLayout extends PuzzleLayout {
    int mTheme;

    public NumberPieceLayout(int theme) {
        if (theme >= getThemeCount()) {
            Log.e(TAG, "NumberPieceLayout: the most theme count is "
                    + getThemeCount() + " ,you should let theme from 0 to "
                    + (getThemeCount() - 1) + " .");
        }
        mTheme = theme;
    }

    public abstract int getThemeCount();

    public int getTheme() {
        return mTheme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mTheme);
    }

    protected NumberPieceLayout(Parcel in) {
        super(in);
        this.mTheme = in.readInt();
    }

}
