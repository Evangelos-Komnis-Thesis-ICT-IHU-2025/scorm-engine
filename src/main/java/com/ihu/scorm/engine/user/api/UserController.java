package com.ihu.scorm.engine.user.api;

import com.ihu.scorm.engine.common.query.BasePaginationQuery;
import com.ihu.scorm.engine.common.query.PageResult;
import com.ihu.scorm.engine.user.mapper.UserApiMapper;
import com.ihu.scorm.engine.user.orchestrator.UserOrchestrator;
import com.ihu.scorm.engine.user.persist.GetUsersQuery;
import com.ihu.scorm.engine.user.persist.UserEmbed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

  private final UserOrchestrator userOrchestrator;
  private final UserApiMapper mapper;

  public UserController(UserOrchestrator userOrchestrator, UserApiMapper mapper) {
    this.userOrchestrator = userOrchestrator;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserForm form) {
    UserDto dto = mapper.toDto(userOrchestrator.createUser(mapper.toCommand(form)));
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(mapper.toDto(userOrchestrator.getUser(userId)));
  }

  @GetMapping
  public ResponseEntity<PageResult<UserDto>> listUsers(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size,
      @RequestParam(defaultValue = "createdAt,desc") String sort,
      @RequestParam(required = false) String search) {
    GetUsersQuery query = new GetUsersQuery(
        new BasePaginationQuery(page, size, sort),
        search,
        EnumSet.noneOf(UserEmbed.class),
        Instant.now());
    return ResponseEntity.ok(mapper.toPageResult(userOrchestrator.listUsers(query)));
  }
}
