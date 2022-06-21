package com.golovanov.kanban.service;

import com.golovanov.kanban.model.TaskEntity;
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
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Transactional
    public List<TaskEntity> list(){
        List<TaskEntity> taskEntities = new ArrayList<>();
        repository.findAll().forEach(taskEntities::add);
        return taskEntities;
    }

    @Transactional
    public Optional<TaskEntity> getTaskEntityById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public int addTask(TaskEntity taskEntity){
        TaskEntity newTaskEntity = repository.save(taskEntity);
        return newTaskEntity.getId();
    }

    @Transactional
    public ResponseEntity<Object> updateTaskById(int id, TaskEntity newTaskEntity){
        Optional<TaskEntity> taskOptional = repository.findById(id);
        if(taskOptional.isPresent()){
            newTaskEntity.setId(id);
            repository.save(newTaskEntity);
            return new ResponseEntity<>(taskOptional, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Transactional
    public void delete(int id){
        repository.deleteById(id);
    }
}
