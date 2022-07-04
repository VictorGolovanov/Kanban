package com.golovanov.kanban.controller;

import com.golovanov.kanban.model.TaskEntity;
import com.golovanov.kanban.model.TaskStatus;
import com.golovanov.kanban.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.golovanov.kanban.common.MessageHelper.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://http://localhost:8282")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTasks() {
        try {
            return new ResponseEntity<>(taskService.list(), HttpStatus.OK);
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Integer id) {
        try {
            Optional<TaskEntity> taskOptional = taskService.getTaskEntityById(id);
            if (taskOptional.isPresent()) {
                return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
            } else {
                return notFound();
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewTask(TaskEntity taskEntity) {
        try {
            return new ResponseEntity<>(taskService.addNewTask(taskEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        try {
            if (taskService.getTaskEntityById(id).isPresent()) {
                String name = taskService.getTaskEntityById(id).get().getTaskName();
                taskService.delete(id);
                return new ResponseEntity<>(name + " deleted", HttpStatus.OK);
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> changeTask(@PathVariable Integer id,
                                        @RequestParam String taskName,
                                        @RequestParam String taskDescription,
                                        @RequestParam String taskStatus) {
        try {
            if (taskService.getTaskEntityById(id).isPresent()) {
                TaskEntity newTaskEntity = taskService.getTaskEntityById(id).get();
                newTaskEntity.setTaskName(!taskName.isEmpty() ? taskName : newTaskEntity.getTaskName());
                newTaskEntity.setTaskDescription(!taskDescription.isEmpty() ? taskDescription : newTaskEntity.getTaskDescription());
                newTaskEntity.setStatus(!taskStatus.isEmpty() ? TaskStatus.valueOf(taskStatus) : newTaskEntity.getStatus());
                taskService.updateTaskById(id, newTaskEntity);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            if (taskService.getTaskEntityById(id).isPresent()) {
                TaskEntity newTaskEntity = taskService.getTaskEntityById(id).get();
                newTaskEntity.setStatus(TaskStatus.valueOf(status));
                taskService.updateTaskById(id, newTaskEntity);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/{id}/name")
    public ResponseEntity<?> changeName(@PathVariable Integer id, @RequestParam String taskName) {
        try {
            if (taskService.getTaskEntityById(id).isPresent()) {
                TaskEntity newTaskEntity = taskService.getTaskEntityById(id).get();
                newTaskEntity.setTaskName(taskName);
                taskService.updateTaskById(id, newTaskEntity);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/{id}/description")
    public ResponseEntity<?> changeDescription(@PathVariable Integer id, @RequestParam String taskDescription) {
        try {
            if (taskService.getTaskEntityById(id).isPresent()) {
                TaskEntity newTaskEntity = taskService.getTaskEntityById(id).get();
                newTaskEntity.setTaskDescription(taskDescription);
                taskService.updateTaskById(id, newTaskEntity);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }
}
