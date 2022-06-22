package com.golovanov.kanban.controller;

import com.golovanov.kanban.model.AssigneeEntity;
import com.golovanov.kanban.service.AssigneeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/assignees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://http://localhost:8282")
public class AssigneeController {

    private final AssigneeService assigneeService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAssignees() {
        try {
            return new ResponseEntity<>(assigneeService.list(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssigneeById(@PathVariable Integer id) {
        try {
            Optional<AssigneeEntity> assigneeOptional = assigneeService.getAssigneeEntityById(id);
            if (assigneeOptional.isPresent()) {
                return new ResponseEntity<>(assigneeOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Assignee not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewAssignee(AssigneeEntity assignee) {
        try {
            return new ResponseEntity<>(assigneeService.addNewAssignee(assignee), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
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

    private ResponseEntity<?> notFound() {
        return new ResponseEntity<>("Assignee not found", HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> internalServerError() {
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
