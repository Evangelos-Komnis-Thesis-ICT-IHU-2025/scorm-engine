package com.ihu.scorm.engine.runtime.state;

import java.math.BigDecimal;

public class ScoreInfo {

  private BigDecimal raw;
  private BigDecimal scaled;
  private BigDecimal min;
  private BigDecimal max;

  public BigDecimal getRaw() {
    return raw;
  }

  public void setRaw(BigDecimal raw) {
    this.raw = raw;
  }

  public BigDecimal getScaled() {
    return scaled;
  }

  public void setScaled(BigDecimal scaled) {
    this.scaled = scaled;
  }

  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }

  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }
}
