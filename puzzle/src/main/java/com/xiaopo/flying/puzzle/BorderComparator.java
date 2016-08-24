package com.xiaopo.flying.puzzle;

import java.util.Comparator;

/**
 * Created by snowbean on 16-8-18.
 */
class BorderComparator implements Comparator<Border> {
    private static final String TAG = "BorderComparator";

    @Override
    public int compare(Border lhs, Border rhs) {
        if (lhs.getRect().top < rhs.getRect().top) {
            return -1;
        } else if (lhs.getRect().top == rhs.getRect().top) {
            if (lhs.getRect().left < rhs.left()) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
