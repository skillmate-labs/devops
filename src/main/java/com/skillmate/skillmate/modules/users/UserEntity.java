package com.skillmate.skillmate.modules.users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.skillmate.skillmate.modules.goals.GoalEntity;
import com.skillmate.skillmate.modules.roles.RoleEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @Column(name = "id", length = 24)
  private String id;

  @Column(name = "name", nullable = false, length = 150)
  private String name;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  private String email;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", nullable = false)
  private RoleEntity role;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GoalEntity> goals;

  @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
  private List<GoalEntity> createdGoals;

  @OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
  private List<GoalEntity> updatedGoals;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
    createdAt = LocalDateTime.now();
  }
}
