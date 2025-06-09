package com.cdc.javacapstoneproject.controllers;


import com.cdc.javacapstoneproject.dto.Login;
import com.cdc.javacapstoneproject.models.Patient;
import com.cdc.javacapstoneproject.models.Appointment;
import com.cdc.javacapstoneproject.services.ClinicService;
import com.cdc.javacapstoneproject.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ClinicService clinicService;

    // 1. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatientDetails(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Patient patient = patientService.getPatientDetails(token);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Patient not found"));
        }

        return ResponseEntity.ok(patient);
    }

    // 2. Create a New Patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean exists = patientService.patientExists(patient.getEmail(), patient.getPhone());
            if (exists) {
                response.put("error", "Patient with email or phone already exists");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            boolean created = patientService.createPatient(patient);
            if (created) {
                response.put("message", "Signup successful");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("error", "Internal server error");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            response.put("error", "Exception occurred while creating patient");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return clinicService.validatePatientLogin(login);
    }

    // 4. Get Patient Appointments
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable Long id, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        List<Appointment> appointments = patientService.getPatientAppointments(id);
        return ResponseEntity.ok(appointments);
    }

    // 5. Filter Patient Appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterAppointments(@PathVariable String condition,
                                                @PathVariable String name,
                                                @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        List<Appointment> filteredAppointments = patientService.filterPatientAppointments(condition, name, token);
        return ResponseEntity.ok(filteredAppointments);
    }
}

