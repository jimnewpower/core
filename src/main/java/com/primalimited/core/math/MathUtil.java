package com.primalimited.core.math;

import com.primalimited.core.bounds.Bounds;
import com.primalimited.core.dval.Dval;

public final class MathUtil {
  public static boolean floatsEqual(float a, float b) {
    if (Float.isNaN(a) || Float.isNaN(b))
      return false;

    if (Dval.isDval(a) && Dval.isDval(b))
      return true;

    if (Dval.isDval(a) || Dval.isDval(b))
      return false;

    if (a == b) // shortcut, handles infinities
      return true;

    return
        Bounds.of(Math.nextDown(a), Math.nextUp(a)).contains(b)
        && Bounds.of(Math.nextDown(b), Math.nextUp(b)).contains(a);
  }

  public static boolean doublesEqual(double a, double b) {
    if (Double.isNaN(a) || Double.isNaN(b))
      return false;
    
    if (Dval.isDval(a) && Dval.isDval(b))
      return true;

    if (Dval.isDval(a) || Dval.isDval(b))
      return false;

    if (a == b) // shortcut, handles infinities
      return true;
    
    return
        Bounds.of(Math.nextDown(a), Math.nextUp(a)).contains(b)
        && Bounds.of(Math.nextDown(b), Math.nextUp(b)).contains(a);
  }
}
