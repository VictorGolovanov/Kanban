package com.golovanov.kanban.repository;

import com.golovanov.kanban.model.AssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssigneeRepository extends JpaRepository<AssigneeEntity, Integer> {
    Optional<AssigneeEntity> findByName(String name);
}
