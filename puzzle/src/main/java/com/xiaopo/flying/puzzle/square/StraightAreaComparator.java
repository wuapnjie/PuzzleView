package com.xiaopo.flying.puzzle.square;

import java.util.Comparator;

/**
 * @author wupanjie
 */

public class StraightAreaComparator implements Comparator<SquareArea> {
  @Override public int compare(SquareArea lhs, SquareArea rhs) {
    if (lhs.top() < rhs.top()) {
      return -1;
    } else if (lhs.top() == rhs.top()) {
      if (lhs.left() < rhs.left()) {
        return -1;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }
}
