package com.skillmate.skillmate.modules.roles;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
  Optional<RoleEntity> findByAcronym(String acronym);

  Optional<RoleEntity> findByName(String name);

  Page<RoleEntity> findAll(Pageable pageable);
}
