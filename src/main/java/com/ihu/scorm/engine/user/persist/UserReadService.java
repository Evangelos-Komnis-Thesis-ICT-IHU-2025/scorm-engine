package com.ihu.scorm.engine.user.persist;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.NotFoundException;
import com.ihu.scorm.engine.user.crud.UserCrudService;
import com.ihu.scorm.engine.user.crud.UserEntity;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserReadService {

  private final UserCrudService userCrudService;

  public UserReadService(UserCrudService userCrudService) {
    this.userCrudService = userCrudService;
  }

  public UserEntity getRequired(UUID userId) {
    UserEntity entity = userCrudService.findById(userId);
    if (entity == null) {
      throw new NotFoundException(
          ErrorCode.USER_NOT_FOUND,
          ErrorMessages.USER_NOT_FOUND,
          Map.of(DetailKey.USER_ID, userId));
    }
    return entity;
  }

  public Page<UserEntity> list(GetUsersQuery query) {
    return userCrudService.findPage(query);
  }
}
