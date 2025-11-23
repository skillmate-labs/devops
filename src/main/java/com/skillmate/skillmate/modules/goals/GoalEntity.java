package com.skillmate.skillmate.modules.goals;

import java.time.LocalDateTime;
import java.util.UUID;

import com.skillmate.skillmate.modules.users.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalEntity {

  @Id
  @Column(name = "id", length = 24)
  private String id;

  @Column(name = "title", nullable = false, length = 500)
  private String title;

  @Column(name = "experience", nullable = false, length = 2000)
  private String experience;

  @Column(name = "hours_per_day", nullable = false)
  private Integer hoursPerDay;

  @Column(name = "days_per_week", nullable = false)
  private Integer daysPerWeek;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private UserEntity createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by")
  private UserEntity updatedBy;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
