package com.skillmate.skillmate.modules.goals;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillmate.skillmate.modules.users.UserEntity;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, String> {
  Page<GoalEntity> findByUser(UserEntity user, Pageable pageable);

  Page<GoalEntity> findAll(Pageable pageable);

  long countByUser_Id(String userId);
}
