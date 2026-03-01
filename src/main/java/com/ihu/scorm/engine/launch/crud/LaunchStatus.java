package com.ihu.scorm.engine.launch.crud;

public enum LaunchStatus {
  ACTIVE {
    @Override
    public boolean canTransitionTo(LaunchStatus next) {
      return next == ACTIVE || next == CLOSED;
    }
  },
  CLOSED;

  public boolean canTransitionTo(LaunchStatus next) {
    return this == next;
  }
}
