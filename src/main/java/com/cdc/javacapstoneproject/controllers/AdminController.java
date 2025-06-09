package com.cdc.javacapstoneproject.controllers;

import com.cdc.javacapstoneproject.models.Admin;
import com.cdc.javacapstoneproject.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin") // Example: /api/admin
public class AdminController {

    @Autowired
    private ClinicService clinicService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin receivedAdmin) {
        return clinicService.validateAdmin(receivedAdmin);
    }
}
