package com.golovanov.kanban.controller;

import com.golovanov.kanban.model.AssigneeEntity;
import com.golovanov.kanban.service.AssigneeService;
import com.golovanov.kanban.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.golovanov.kanban.common.MessageHelper.*;

@RestController
@RequestMapping("/assignees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://http://localhost:8282")
public class AssigneeController {

    private final AssigneeService assigneeService;
    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAssignees() {
        try {
            return new ResponseEntity<>(assigneeService.list(), HttpStatus.OK);
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssigneeById(@PathVariable Integer id) {
        try {
            Optional<AssigneeEntity> assigneeOptional = assigneeService.getAssigneeEntityById(id);
            if (assigneeOptional.isPresent()) {
                return new ResponseEntity<>(assigneeOptional.get(), HttpStatus.OK);
            } else {
                return notFound();
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewAssignee(AssigneeEntity assignee) {
        try {
            return new ResponseEntity<>(assigneeService.addNewAssignee(assignee), HttpStatus.CREATED);
        } catch (Exception e) {
            return internalServerError();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignee(@PathVariable Integer id) {
        try {
            if (assigneeService.getAssigneeEntityById(id).isPresent()) {
                String name = assigneeService.getAssigneeEntityById(id).get().getName();
                assigneeService.delete(id);
                return new ResponseEntity<>(name +" deleted", HttpStatus.OK);
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/{id}/name")
    public ResponseEntity<?> changeName(@PathVariable Integer id, @RequestParam String name) {
        try {
            if (assigneeService.getAssigneeEntityById(id).isPresent()) {
                AssigneeEntity newAssigneeEntity = assigneeService.getAssigneeEntityById(id).get();
                newAssigneeEntity.setName(name);
                assigneeService.updateAssigneeById(id, newAssigneeEntity);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/{assigneeId}/{taskId}")
    public ResponseEntity<?> assignTask(@PathVariable Integer assigneeId, @PathVariable Integer taskId) {
        try {
            if (assigneeService.getAssigneeEntityById(assigneeId).isPresent()
                    && taskService.getTaskEntityById(taskId).isPresent()) {
                assigneeService.addNewTaskToAssignee(assigneeId, taskId);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/{assigneeId}/-{taskId}")
    public ResponseEntity<?> unAssignTask(@PathVariable Integer assigneeId, @PathVariable Integer taskId) {
        try {
            if (assigneeService.getAssigneeEntityById(assigneeId).isPresent()
                    && taskService.getTaskEntityById(taskId).isPresent()) {
                assigneeService.removeTaskFromAssignee(assigneeId, taskId);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }
}
