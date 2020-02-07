package com.primalimited.core.math;

public class MathUtil {
  static final double DEFAULT_EPSILON = 1e-12;

  public static boolean floatsEqual(float a, float b, float epsilon) {
    final float absA = Math.abs(a);
    final float absB = Math.abs(b);
    final float diff = Math.abs(a - b);

    if (a == b) { // shortcut, handles infinities
      return true;
    } else if (a == 0 || b == 0 || (absA + absB < Float.MIN_NORMAL)) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Float.MIN_NORMAL);
    } else { // use relative error
      return diff / Math.min((absA + absB), Float.MAX_VALUE) < epsilon;
    }
  }

  public static boolean doublesEqual(double a, double b) {
    return doublesEqual(a, b, DEFAULT_EPSILON);
  }
  
  public static boolean doublesEqual(double a, double b, double epsilon) {
    final double absA = Math.abs(a);
    final double absB = Math.abs(b);
    final double diff = Math.abs(a - b);

    if (a == b) { // shortcut, handles infinities
      return true;
    } else if (a == 0 || b == 0 || (absA + absB < Double.MIN_NORMAL)) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Double.MIN_NORMAL);
    } else { // use relative error
      return diff / Math.min((absA + absB), Double.MAX_VALUE) < epsilon;
    }
  }
}
