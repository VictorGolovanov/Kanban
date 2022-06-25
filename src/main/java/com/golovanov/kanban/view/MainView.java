package com.golovanov.kanban.view;

import com.golovanov.kanban.model.TaskEntity;
import com.golovanov.kanban.repository.TaskRepository;
import com.golovanov.kanban.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends HorizontalLayout {

//    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final Grid<TaskEntity> grid = new Grid<>(TaskEntity.class);

    @Autowired
    public MainView(/*TaskRepository taskRepository, */TaskService taskService) {
        this.taskService = taskService;
//        this.taskRepository = taskRepository;
        add(grid);
        grid.setItems(taskService.list());
        add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));
    }
}
