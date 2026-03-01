package com.ihu.scorm.engine.user.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ConflictException;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.user.crud.UserCrudService;
import com.ihu.scorm.engine.user.crud.UserEntity;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserPersistService {

  private final UserCrudService userCrudService;

  public UserPersistService(UserCrudService userCrudService) {
    this.userCrudService = userCrudService;
  }

  public UserEntity create(UserEntity entity) {
    if (userCrudService.usernameExists(entity.getUsername())) {
      throw new ConflictException(
          ErrorCode.USERNAME_EXISTS,
          ErrorMessages.USERNAME_ALREADY_EXISTS,
          Map.of(DetailKey.USERNAME, entity.getUsername()));
    }
    if (userCrudService.emailExists(entity.getEmail())) {
      throw new ConflictException(
          ErrorCode.EMAIL_EXISTS,
          ErrorMessages.EMAIL_ALREADY_EXISTS,
          Map.of(DetailKey.EMAIL, entity.getEmail()));
    }
    return userCrudService.save(entity);
  }
}
