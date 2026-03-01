package com.ihu.scorm.engine.common.command;

public interface CommandHandler<C, R> {

  R handle(C command);
}
