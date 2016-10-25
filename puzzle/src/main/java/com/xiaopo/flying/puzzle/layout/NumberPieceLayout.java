package com.xiaopo.flying.puzzle.layout;

import android.util.Log;

import com.xiaopo.flying.puzzle.PuzzleLayout;

/**
 * Created by snowbean on 16-8-17.
 */
public abstract class NumberPieceLayout extends PuzzleLayout {
    public NumberPieceLayout(int theme) {
        if (theme >= getThemeCount()) {
            Log.e(TAG, "NumberPieceLayout: the most theme count is "
                    + getThemeCount() + " ,you should let theme from 0 to "
                    + (getThemeCount() - 1) + " .");
        }
        mTheme = theme;
    }

    public abstract int getThemeCount();

}
