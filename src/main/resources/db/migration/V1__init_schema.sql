CREATE TABLE IF NOT EXISTS courses (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  code VARCHAR(255) UNIQUE,
  title VARCHAR(512) NOT NULL,
  description TEXT,
  standard VARCHAR(64) NOT NULL,
  version_label VARCHAR(255),
  entrypoint_path VARCHAR(1024) NOT NULL,
  manifest_hash VARCHAR(128) NOT NULL,
  metadata_json TEXT,
  storage_bucket VARCHAR(255) NOT NULL,
  storage_object_key_zip VARCHAR(1024) NOT NULL,
  storage_object_key_extracted_prefix VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  external_ref VARCHAR(255) UNIQUE,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  locale VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS enrollments (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  user_id UUID NOT NULL REFERENCES users(id),
  course_id UUID NOT NULL REFERENCES courses(id),
  status VARCHAR(32) NOT NULL,
  CONSTRAINT uk_enrollment_user_course UNIQUE (user_id, course_id)
);

CREATE TABLE IF NOT EXISTS attempts (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  user_id UUID NOT NULL REFERENCES users(id),
  course_id UUID NOT NULL REFERENCES courses(id),
  enrollment_id UUID NOT NULL REFERENCES enrollments(id),
  attempt_no INT NOT NULL,
  status VARCHAR(32) NOT NULL,
  started_at TIMESTAMPTZ NOT NULL,
  ended_at TIMESTAMPTZ,
  completion_status VARCHAR(32) NOT NULL,
  success_status VARCHAR(32) NOT NULL,
  score_raw NUMERIC(10,4),
  score_scaled NUMERIC(10,4),
  total_time_seconds BIGINT NOT NULL DEFAULT 0,
  last_location VARCHAR(1024),
  last_suspend_data TEXT,
  last_committed_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_attempts_user_course ON attempts(user_id, course_id);
CREATE INDEX IF NOT EXISTS idx_attempts_enrollment_status ON attempts(enrollment_id, status);

CREATE TABLE IF NOT EXISTS launches (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  attempt_id UUID NOT NULL REFERENCES attempts(id),
  standard VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  launch_token_hash VARCHAR(256) NOT NULL,
  last_seen_at TIMESTAMPTZ NOT NULL,
  expires_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_launches_attempt ON launches(attempt_id);
CREATE INDEX IF NOT EXISTS idx_launches_status_seen ON launches(status, last_seen_at);

CREATE TABLE IF NOT EXISTS attempt_runtime_snapshots (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  attempt_id UUID NOT NULL REFERENCES attempts(id),
  launch_id UUID NOT NULL REFERENCES launches(id),
  captured_at TIMESTAMPTZ NOT NULL,
  raw_runtime_json TEXT NOT NULL,
  normalized_progress_json TEXT NOT NULL,
  reason VARCHAR(32) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_snapshots_attempt_capture ON attempt_runtime_snapshots(attempt_id, captured_at DESC);
