package com.trumpecy.tictactoe.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/ui/game/{id}")
    public String gamePage(@PathVariable("id") String id, Model model) {
        model.addAttribute("gameId", id);
        model.addAttribute("player", "X");
        return "game";
    }

    @GetMapping("/ui/user/{id}")
    public String userPage(@PathVariable("id") String id, Model model) {
        model.addAttribute("userId", id);
        return "user";
    }

    @GetMapping("/ui/join")
    public String joinPage() {
        return "join";
    }

    @GetMapping("/ui/auth")
    public String authPage() {
        return "auth";
    }

    @GetMapping("/ui/menu")
    public String menuPage() {
        return "menu";
    }

    @GetMapping("/ui/user")
    public String usersPage() {
        return "users";
    }

    @GetMapping("/ui/leaderboard")
    public String leaderboardPage() {
        return "leaderboard";
    }
} 