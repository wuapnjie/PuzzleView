package com.xiaopo.flying.photolayout.layout;

import android.util.Log;
import com.xiaopo.flying.puzzle.straight.StraightPuzzleLayout;

/**
 * Created by snowbean on 16-8-17.
 */
public abstract class NumberPieceLayout extends StraightPuzzleLayout {
  protected static final String TAG = "NumberPieceLayout";
  protected int mTheme;

  public NumberPieceLayout(int theme) {
    if (theme >= getThemeCount()) {
      Log.e(TAG, "NumberPieceLayout: the most theme count is "
          + getThemeCount()
          + " ,you should let theme from 0 to "
          + (getThemeCount() - 1)
          + " .");
    }
    this.mTheme = theme;
  }

  public abstract int getThemeCount();

  public int getTheme() {
    return mTheme;
  }
}
