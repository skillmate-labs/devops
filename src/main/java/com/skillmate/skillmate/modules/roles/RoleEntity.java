package com.skillmate.skillmate.modules.roles;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "acronym")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

  @Id
  @Column(name = "id", length = 24)
  private String id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "acronym", nullable = false, unique = true, length = 10)
  private String acronym;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
    createdAt = LocalDateTime.now();
  }
}
