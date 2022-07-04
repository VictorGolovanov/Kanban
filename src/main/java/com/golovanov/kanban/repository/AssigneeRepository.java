package com.golovanov.kanban.repository;

import com.golovanov.kanban.model.AssigneeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssigneeRepository extends CrudRepository<AssigneeEntity, Integer> {
    Optional<AssigneeEntity> findByName(String name);
}
