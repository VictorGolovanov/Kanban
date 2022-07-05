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
    private final String NULL_PERSON = "Null-Person";

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

    @GetMapping("/names/{name}")
    public ResponseEntity<?> getAssigneeByName(@PathVariable String name) {
        try {
            Optional<AssigneeEntity> assigneeOptional = assigneeService.getAssigneeEntityByName(name);
            if (assigneeOptional.isPresent()) {
                return new ResponseEntity<>(assigneeOptional.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewAssignee(AssigneeEntity assignee) {
        try {
            if (assigneeService.getAssigneeEntityByName(assignee.getName()).isPresent()) {
                return new ResponseEntity<>("person with name " + assignee.getName() + " is already exists",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            if (assignee.getName().equals(NULL_PERSON)) {
                return new ResponseEntity<>("This name is forbidden", HttpStatus.FORBIDDEN);
            }
            if (assigneeService.getAssigneeEntityByName(NULL_PERSON).isEmpty()) {
                getNullPersonEntity();
            }
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
                if (name.equals(NULL_PERSON)) {
                    return new ResponseEntity<>("You are not allowed to delete this person", HttpStatus.FORBIDDEN);
                }
                if (assigneeService.getAssigneeEntityById(id).get().getTasks().size() > 0) {
                    if (assigneeService.getAssigneeEntityByName(NULL_PERSON).isPresent()) {
                        AssigneeEntity nullPersonEntity = assigneeService.getAssigneeEntityByName(NULL_PERSON).get();
                        assigneeService.getAssigneeEntityById(id).get().getTasks()
                                .forEach(nullPersonEntity.getTasks()::add);
                        assigneeService.updateAssigneeById(nullPersonEntity.getId(), nullPersonEntity);
                    }
                }
                assigneeService.delete(id);
                return new ResponseEntity<>(name +" deleted", HttpStatus.OK);
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    @PostMapping("/name/{id}")
    public ResponseEntity<?> changeName(@PathVariable Integer id, @RequestParam String assigneeName) {
        try {
            if (assigneeService.getAssigneeEntityById(id).isPresent()
                    && assigneeService.getAssigneeEntityByName(assigneeName).isEmpty()) {
                AssigneeEntity newAssigneeEntity = assigneeService.getAssigneeEntityById(id).get();
                newAssigneeEntity.setName(assigneeName);
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
                AssigneeEntity nullPerson = getNullPersonEntity();
                nullPerson.addTask(taskService.getTaskEntityById(taskId).get());
                assigneeService.updateAssigneeById(nullPerson.getId(), nullPerson);
                return okResponse();
            }
        } catch (Exception e) {
            return internalServerError();
        }
        return notFound();
    }

    private AssigneeEntity getNullPersonEntity() {
        if (assigneeService.getAssigneeEntityByName(NULL_PERSON).isEmpty()) {
            AssigneeEntity nullPerson = new AssigneeEntity();
            nullPerson.setName(NULL_PERSON);
            assigneeService.addNewAssigneeFlush(nullPerson);
        }
        return assigneeService.getAssigneeEntityByName(NULL_PERSON).get();
    }
}
