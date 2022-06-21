package com.golovanov.kanban.repository;

import com.golovanov.kanban.model.AssigneeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigneeRepository extends CrudRepository<AssigneeEntity, Integer> {
}
