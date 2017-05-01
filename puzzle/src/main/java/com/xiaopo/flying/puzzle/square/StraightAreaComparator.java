package com.xiaopo.flying.puzzle.square;

import java.util.Comparator;

/**
 * @author wupanjie
 */

public class StraightAreaComparator implements Comparator<StraightArea> {
  @Override public int compare(StraightArea lhs, StraightArea rhs) {
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
