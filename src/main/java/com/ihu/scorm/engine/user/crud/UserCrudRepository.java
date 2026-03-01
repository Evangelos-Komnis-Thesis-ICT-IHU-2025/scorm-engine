package com.ihu.scorm.engine.user.crud;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserCrudRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

  boolean existsByUsernameIgnoreCase(String username);

  boolean existsByEmailIgnoreCase(String email);

  Optional<UserEntity> findByUsernameIgnoreCase(String username);
}
