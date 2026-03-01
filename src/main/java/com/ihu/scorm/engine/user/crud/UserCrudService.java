package com.ihu.scorm.engine.user.crud;

import com.ihu.scorm.engine.user.UserDatabaseFieldConstants;
import com.ihu.scorm.engine.user.persist.GetUsersQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserCrudService {

  private final UserCrudRepository repository;

  public UserCrudService(UserCrudRepository repository) {
    this.repository = repository;
  }

  public UserEntity save(UserEntity user) {
    return repository.save(user);
  }

  public UserEntity findById(java.util.UUID userId) {
    return repository.findById(userId).orElse(null);
  }

  public boolean usernameExists(String username) {
    return repository.existsByUsernameIgnoreCase(username);
  }

  public boolean emailExists(String email) {
    return repository.existsByEmailIgnoreCase(email);
  }

  public Page<UserEntity> findPage(GetUsersQuery query) {
    Sort sort = Sort.by(Sort.Direction.DESC, UserDatabaseFieldConstants.CREATED_AT);
    String sortRaw = query.pagination().sort();
    if (sortRaw != null && !sortRaw.isBlank()) {
      String[] parts = sortRaw.split(",");
      if (parts.length == 2) {
        sort = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
      }
    }
    Pageable pageable = PageRequest.of(query.pagination().page(), query.pagination().size(), sort);

    Specification<UserEntity> spec = Specification.where(null);
    if (query.search() != null && !query.search().isBlank()) {
      String search = "%" + query.search().toLowerCase() + "%";
      spec = spec.and((root, cq, cb) -> cb.or(
          cb.like(cb.lower(root.get(UserDatabaseFieldConstants.USERNAME)), search),
          cb.like(cb.lower(root.get(UserDatabaseFieldConstants.EMAIL)), search),
          cb.like(cb.lower(root.get(UserDatabaseFieldConstants.FIRST_NAME)), search),
          cb.like(cb.lower(root.get(UserDatabaseFieldConstants.LAST_NAME)), search)));
    }

    return repository.findAll(spec, pageable);
  }
}
