package com.cdc.javacapstoneproject.controllers;

import com.cdc.javacapstoneproject.models.Appointment;
import com.cdc.javacapstoneproject.services.AppointmentService;
import com.cdc.javacapstoneproject.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClinicService clinicService;

    // 1. Get appointments for a specific date and patient name (doctor only)
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        // Token validation for doctor role
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format. Use yyyy-MM-dd."));
        }

        // Get appointment data
        Map<String, Object> appointmentsMap = appointmentService.getAppointment(patientName, localDate, token);
        return new ResponseEntity<>(appointmentsMap, HttpStatus.OK);
    }

    // 2. Book a new appointment (patient only)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@PathVariable String token,
                                                               @RequestBody Appointment appointment) {
        // Token validation for patient role
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        // Validate the appointment
        int result = clinicService.validateAppointment(appointment);
        Map<String, String> response = new HashMap<>();

        if (result == -1) {
            response.put("error", "Invalid doctor or appointment time");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (result == 0) {
            response.put("error", "Time slot not available");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // Book appointment
        appointmentService.bookAppointment(appointment);
        response.put("message", "Appointment booked successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 3. Update an appointment (patient only)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@PathVariable String token,
                                                                 @RequestBody Appointment appointment) {
        // Token validation for patient
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        appointmentService.updateAppointment(appointment);
        return ResponseEntity.ok(Collections.singletonMap("message", "Appointment updated successfully"));
    }

    // 4. Cancel an appointment (patient only)
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id,
                                                                 @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        // ✅ Pass both id and token to match the method signature
        return appointmentService.cancelAppointment(id, token);
    }

}
