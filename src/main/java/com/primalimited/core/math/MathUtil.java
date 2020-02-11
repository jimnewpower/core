package com.primalimited.core.math;

import com.primalimited.core.bounds.Bounds;
import com.primalimited.core.dval.Dval;

public final class MathUtil {
  // declared default constructor to pass code coverage
  MathUtil() {
    throw new IllegalStateException(getClass().getName() + " is a utility class.");
  }
  
  public static boolean floatsEqual(float a, float b) {
    if (Float.isNaN(a) || Float.isNaN(b))
      return false;

    if (Dval.isDval(a) && Dval.isDval(b))
      return true;

    if (Dval.isDval(a) || Dval.isDval(b))
      return false;

    if (a == b) // shortcut, handles infinities
      return true;

    // after the == test, if either is infinite, bail out now to 
    // avoid creating Bounds instances below with infinite values 
    if (Float.isInfinite(a) || Float.isInfinite(b))
      return false;

    Bounds aBounds = Bounds.of(Math.nextDown(a), Math.nextUp(a));
    return aBounds.contains(b);
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

    // after the == test, if either is infinite, bail out now to 
    // avoid creating Bounds instances below with infinite values 
    if (Double.isInfinite(a) || Double.isInfinite(b))
      return false;

    Bounds aBounds = Bounds.of(Math.nextDown(a), Math.nextUp(a));
    return aBounds.contains(b);
  }
}
