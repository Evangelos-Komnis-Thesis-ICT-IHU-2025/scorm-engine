package com.ihu.scorm.engine.user.crud;

import com.ihu.scorm.engine.common.persistence.BaseEntity;
import com.ihu.scorm.engine.user.UserDatabaseFieldConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = UserDatabaseFieldConstants.TABLE_NAME)
public class UserEntity extends BaseEntity {

  @Column(unique = true)
  private String externalRef;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String locale;

  public String getExternalRef() {
    return externalRef;
  }

  public void setExternalRef(String externalRef) {
    this.externalRef = externalRef;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }
}
