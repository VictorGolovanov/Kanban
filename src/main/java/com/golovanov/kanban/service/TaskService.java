package com.golovanov.kanban.service;

import com.golovanov.kanban.model.AssigneeEntity;
import com.golovanov.kanban.model.TaskEntity;
import com.golovanov.kanban.model.TaskStatus;
import com.golovanov.kanban.repository.AssigneeRepository;
import com.golovanov.kanban.repository.TaskRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final String NULL_PERSON = "Null-Person";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Transactional
    public List<TaskEntity> list(){
        List<TaskEntity> taskEntities = new ArrayList<>();
        taskRepository.findAll().forEach(taskEntities::add);
        return taskEntities;
    }

    @Transactional
    public Optional<TaskEntity> getTaskEntityById(Integer id) {
        return taskRepository.findById(id);
    }

    @SneakyThrows
    @Transactional
    public int addNewTask(TaskEntity taskEntity) {
        if (taskEntity.getStatus() == null) {
            taskEntity.setStatus(TaskStatus.TODO);
        }
        Optional<AssigneeEntity> nullPersonOptional = assigneeRepository.findByName(NULL_PERSON);
        AssigneeEntity nullPerson;
        if (nullPersonOptional.isPresent()) {
            nullPerson = nullPersonOptional.get();
        } else {
            nullPerson = new AssigneeEntity();
            nullPerson.setName(NULL_PERSON);
        }
        TaskEntity newTaskEntity = taskRepository.save(taskEntity);
        nullPerson.addTask(newTaskEntity);
        assigneeRepository.save(nullPerson);
        return newTaskEntity.getId();
    }

    @Transactional
    public ResponseEntity<Object> updateTaskById(Integer id, TaskEntity newTaskEntity) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()){
            newTaskEntity.setId(id);
            taskRepository.save(newTaskEntity);
            return new ResponseEntity<>(taskOptional, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Transactional
    public void delete(Integer id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public ResponseEntity<Object> get(Integer id) {
        Optional<TaskEntity> taskOptional = taskRepository.findById(id);
        if(taskOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(taskOptional, HttpStatus.OK);
    }
}
