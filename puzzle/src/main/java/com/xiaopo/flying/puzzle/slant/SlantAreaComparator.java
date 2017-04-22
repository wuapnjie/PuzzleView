package com.xiaopo.flying.puzzle.slant;

import java.util.Comparator;

/**
 * @author wupanjie
 */

public class SlantAreaComparator implements Comparator<SlantArea> {

  @Override public int compare(SlantArea one, SlantArea two) {
    if (one.leftTop.y < two.leftTop.y) {
      return -1;
    } else if (one.leftTop.y == two.leftTop.y) {
      if (one.leftTop.x < two.leftTop.x) {
        return -1;
      } else {
        return 1;
      }
    } else {
      return 1;
    }
  }
}
