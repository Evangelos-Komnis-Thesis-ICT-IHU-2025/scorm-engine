package com.ihu.scorm.engine.runtime.adapter;

public final class ScormRuntimeConstants {

  private ScormRuntimeConstants() {
  }

  public static final class Scorm12 {
    public static final String KEY_LESSON_LOCATION = "cmi.core.lesson_location";
    public static final String KEY_LESSON_STATUS = "cmi.core.lesson_status";
    public static final String KEY_SCORE_RAW = "cmi.core.score.raw";
    public static final String KEY_SESSION_TIME = "cmi.core.session_time";
    public static final String KEY_SUSPEND_DATA = "cmi.suspend_data";
    public static final String KEY_TOTAL_TIME = "cmi.core.total_time";

    public static final String STATUS_BROWSED = "browsed";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_INCOMPLETE = "incomplete";
    public static final String STATUS_NOT_ATTEMPTED = "not attempted";
    public static final String STATUS_PASSED = "passed";

    private Scorm12() {
    }
  }

  public static final class Scorm2004 {
    public static final String KEY_COMPLETION_STATUS = "cmi.completion_status";
    public static final String KEY_LOCATION = "cmi.location";
    public static final String KEY_SCORE_MAX = "cmi.score.max";
    public static final String KEY_SCORE_MIN = "cmi.score.min";
    public static final String KEY_SCORE_RAW = "cmi.score.raw";
    public static final String KEY_SCORE_SCALED = "cmi.score.scaled";
    public static final String KEY_SESSION_TIME = "cmi.session_time";
    public static final String KEY_SUCCESS_STATUS = "cmi.success_status";
    public static final String KEY_SUSPEND_DATA = "cmi.suspend_data";
    public static final String KEY_TOTAL_TIME = "cmi.total_time";

    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_INCOMPLETE = "incomplete";
    public static final String STATUS_NOT_ATTEMPTED = "not attempted";
    public static final String STATUS_PASSED = "passed";
    public static final String STATUS_UNKNOWN = "unknown";

    private Scorm2004() {
    }
  }
}
