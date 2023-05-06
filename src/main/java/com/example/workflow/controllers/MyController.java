package com.example.workflow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class  MyController {
    @GetMapping("/index")
    public String index(){
        return "ticket";
    }
}
