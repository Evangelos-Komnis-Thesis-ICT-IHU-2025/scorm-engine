package com.ihu.scorm.engine.attempt.crud;

public enum AttemptStatus {
  IN_PROGRESS {
    @Override
    public boolean canTransitionTo(AttemptStatus next) {
      return next == IN_PROGRESS || next == COMPLETED || next == TERMINATED || next == ABANDONED;
    }
  },
  COMPLETED,
  TERMINATED,
  ABANDONED;

  public boolean canTransitionTo(AttemptStatus next) {
    return this == next;
  }
}
