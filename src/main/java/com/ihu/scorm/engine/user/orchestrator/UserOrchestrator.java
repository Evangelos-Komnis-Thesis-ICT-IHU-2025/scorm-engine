package com.ihu.scorm.engine.user.orchestrator;

import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.user.crud.UserEntity;
import com.ihu.scorm.engine.user.persist.GetUsersQuery;
import com.ihu.scorm.engine.user.persist.UserPersistService;
import com.ihu.scorm.engine.user.persist.UserReadService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserOrchestrator {

  private final UserReadService userReadService;
  private final UserPersistService userPersistService;

  public UserOrchestrator(UserReadService userReadService, UserPersistService userPersistService) {
    this.userReadService = userReadService;
    this.userPersistService = userPersistService;
  }

  public UserEntity createUser(CreateUserCommand command) {
    UserEntity user = new UserEntity();
    user.setExternalRef(command.externalRef());
    user.setUsername(command.username());
    user.setEmail(command.email());
    user.setFirstName(command.firstName());
    user.setLastName(command.lastName());
    user.setLocale(command.locale());
    return userPersistService.create(user);
  }

  public UserEntity getUser(java.util.UUID userId) {
    return userReadService.getRequired(userId);
  }

  public PageResult<UserEntity> listUsers(GetUsersQuery query) {
    Page<UserEntity> page = userReadService.list(query);
    return new PageResult<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
