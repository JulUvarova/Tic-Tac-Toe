package com.school21.Tic_Tac_Toe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable String id, Model model) {
        model.addAttribute("gameId", id);
        return "game";
    }
} 