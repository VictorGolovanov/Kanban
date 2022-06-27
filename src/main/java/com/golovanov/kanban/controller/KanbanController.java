package com.golovanov.kanban.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KanbanController {

    @GetMapping("/")
    public String index(Model model) {
//        model.addAttribute("name", name);
        return "index";
    }
}
