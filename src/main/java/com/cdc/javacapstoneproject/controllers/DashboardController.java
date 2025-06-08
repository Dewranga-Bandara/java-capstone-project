package com.cdc.javacapstoneproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    // Simulated inline token validation logic
    private boolean isTokenValid(String token, String requiredRole) {
        // Dummy logic — in production, use JWT or DB check
        return token != null && token.equals("valid-" + requiredRole);
    }

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token, Model model) {
        if (isTokenValid(token, "admin")) {
            return "admin/adminDashboard";  // Thymeleaf will resolve to adminDashboard.html
        } else {
            return "redirect:/login";  // Redirect to login if invalid
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token, Model model) {
        if (isTokenValid(token, "doctor")) {
            return "doctor/doctorDashboard";  // Thymeleaf will resolve to doctorDashboard.html
        } else {
            return "redirect:/login";  // Redirect to login if invalid
        }
    }
}

