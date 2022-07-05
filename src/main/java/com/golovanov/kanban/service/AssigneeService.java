package com.golovanov.kanban.service;

import com.golovanov.kanban.model.AssigneeEntity;
import com.golovanov.kanban.model.TaskEntity;
import com.golovanov.kanban.repository.AssigneeRepository;
import com.golovanov.kanban.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssigneeService {

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public List<AssigneeEntity> list() {
        List<AssigneeEntity> assigneeEntities = new ArrayList<>();
//        assigneeRepository.findAll().forEach(assigneeEntities::add);
        assigneeRepository.findAll().forEach(assigneeEntities::add);
        return assigneeEntities;
    }

    @Transactional
    public Optional<AssigneeEntity> getAssigneeEntityById(Integer id) {
        return assigneeRepository.findById(id);
    }

    @Transactional
    public Optional<AssigneeEntity> getAssigneeEntityByName(String name) {
        return assigneeRepository.findByName(name);
    }

    @Transactional
    public int addNewAssignee(AssigneeEntity assigneeEntity) {
        AssigneeEntity newAssigneeEntity = assigneeRepository.save(assigneeEntity);
        return newAssigneeEntity.getId();
    }

    @Transactional
    public int addNewAssigneeFlush(AssigneeEntity assigneeEntity) {
        AssigneeEntity newAssigneeEntity = assigneeRepository.saveAndFlush(assigneeEntity);
        return newAssigneeEntity.getId();
    }

    @Transactional
    public ResponseEntity<Object> updateAssigneeById(Integer id, AssigneeEntity newAssigneeEntity) {
        Optional<AssigneeEntity> assigneeOptional = assigneeRepository.findById(id);
        if(assigneeOptional.isPresent()){
            newAssigneeEntity.setId(id);
            assigneeRepository.save(newAssigneeEntity);
            return new ResponseEntity<>(assigneeOptional, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Transactional
    public void delete(Integer id) {
        assigneeRepository.deleteById(id);
    }

    @Transactional
    public ResponseEntity<Object> get(Integer id) {
        Optional<AssigneeEntity> assigneeOptional = assigneeRepository.findById(id);
        if (assigneeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(assigneeOptional, HttpStatus.OK);
    }

    @Transactional
    public int addNewTaskToAssignee(Integer assigneeId, Integer taskId) {
        Optional<AssigneeEntity> assigneeOptional = assigneeRepository.findById(assigneeId);
        if (assigneeOptional.isPresent()) {
            Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
            if (taskOptional.isPresent()) {
                AssigneeEntity assigneeEntity = assigneeOptional.get();
                TaskEntity taskEntity = taskOptional.get();
                assigneeEntity.addTask(taskEntity);
                AssigneeEntity modified = assigneeRepository.save(assigneeEntity);
                taskRepository.save(taskEntity);
                return modified.getId();
            }
        }
        return -1; //todo
    }

    @Transactional
    public int removeTaskFromAssignee(Integer assigneeId, Integer taskId) {
        Optional<AssigneeEntity> assigneeOptional = assigneeRepository.findById(assigneeId);
        if (assigneeOptional.isPresent()) {
            Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
            if (taskOptional.isPresent()) {
                AssigneeEntity assigneeEntity = assigneeOptional.get();
                TaskEntity taskEntity = taskOptional.get();
                assigneeEntity.removeTask(taskEntity);
                AssigneeEntity modified = assigneeRepository.save(assigneeEntity);
                taskRepository.save(taskEntity);
                return modified.getId();
            }
        }
        return -1; //todo
    }
}
