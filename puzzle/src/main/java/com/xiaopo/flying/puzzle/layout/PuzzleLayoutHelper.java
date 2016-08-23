package com.xiaopo.flying.puzzle.layout;

import com.xiaopo.flying.puzzle.PuzzleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-8-17.
 */
public class PuzzleLayoutHelper {
    private PuzzleLayoutHelper() {

    }

    public static List<PuzzleLayout> getAllThemeLayout(int pieceCount) {
        List<PuzzleLayout> puzzleLayouts = new ArrayList<>();
        switch (pieceCount) {
            case 1:
                for (int i = 0; i < 6; i++) {
                    puzzleLayouts.add(new OnePieceLayout(i));
                }
                break;
            case 2:
                for (int i = 0; i < 7; i++) {
                    puzzleLayouts.add(new TwoPieceLayout(i));
                }
                break;
            case 3:
                for (int i = 0; i < 6; i++) {
                    puzzleLayouts.add(new ThreePieceLayout(i));
                }
                break;
            case 4:
                for (int i = 0; i < 8; i++) {
                    puzzleLayouts.add(new FourPieceLayout(i));
                }
                break;
            case 5:
                for (int i = 0; i < 17; i++) {
                    puzzleLayouts.add(new FivePieceLayout(i));
                }
                break;
            case 6:
                for (int i = 0; i < 12; i++) {
                    puzzleLayouts.add(new SixPieceLayout(i));
                }
                break;
            case 7:
                for (int i = 0; i < 9; i++) {
                    puzzleLayouts.add(new SevenPieceLayout(i));
                }
                break;
            case 8:
                for (int i = 0; i < 11; i++) {
                    puzzleLayouts.add(new EightPieceLayout(i));
                }
                break;
            case 9:
                for (int i = 0; i < 8; i++) {
                    puzzleLayouts.add(new NinePieceLayout(i));
                }
                break;

        }

        return puzzleLayouts;
    }

}
