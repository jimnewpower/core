package com.primalimited.core.bounds;

import com.primalimited.core.dval.Dval;

class NullBounds implements Bounds {
  public static Bounds create() {
    return new NullBounds();
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + " " + boundsText(); 
  }

  @Override
  public double getMin() {
    return Dval.DVAL_DOUBLE;
  }

  @Override
  public double getMax() {
    return Dval.DVAL_DOUBLE;
  }

  @Override
  public double getRange() {
    return Dval.DVAL_DOUBLE;
  }

  @Override
  public boolean isValid() {
    return false;
  }
}
