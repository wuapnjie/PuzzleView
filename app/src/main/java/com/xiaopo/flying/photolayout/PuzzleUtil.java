package com.xiaopo.flying.photolayout;

import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.layout.EightPieceLayout;
import com.xiaopo.flying.puzzle.layout.FivePieceLayout;
import com.xiaopo.flying.puzzle.layout.FourPieceLayout;
import com.xiaopo.flying.puzzle.layout.NinePieceLayout;
import com.xiaopo.flying.puzzle.layout.OnePieceLayout;
import com.xiaopo.flying.puzzle.layout.SevenPieceLayout;
import com.xiaopo.flying.puzzle.layout.SixPieceLayout;
import com.xiaopo.flying.puzzle.layout.ThreePieceLayout;
import com.xiaopo.flying.puzzle.layout.TwoPieceLayout;

/**
 * Created by snowbean on 16-8-18.
 */
public class PuzzleUtil {
    private static final String TAG = "PuzzleUtil";

    private PuzzleUtil() {

    }

    public static PuzzleLayout getPuzzleLayout(int borderSize, int themeId) {
        switch (borderSize) {
            case 1:
                return new OnePieceLayout(themeId);
            case 2:
                return new TwoPieceLayout(themeId);
            case 3:
                return new ThreePieceLayout(themeId);
            case 4:
                return new FourPieceLayout(themeId);
            case 5:
                return new FivePieceLayout(themeId);
            case 6:
                return new SixPieceLayout(themeId);
            case 7:
                return new SevenPieceLayout(themeId);
            case 8:
                return new EightPieceLayout(themeId);
            case 9:
                return new NinePieceLayout(themeId);
            default:
                return new OnePieceLayout(themeId);
        }
    }
}
