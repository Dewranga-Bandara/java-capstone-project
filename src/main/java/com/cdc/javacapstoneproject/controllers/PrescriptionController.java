package com.cdc.javacapstoneproject.controllers;


import com.cdc.javacapstoneproject.models.Prescription;
import com.cdc.javacapstoneproject.services.ClinicService;
import com.cdc.javacapstoneproject.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private ClinicService clinicService;

    // 1. Save Prescription
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(@PathVariable String token, @RequestBody Prescription prescription) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "doctor");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return prescriptionService.savePrescription(prescription);
    }


    // 2. Get Prescription by Appointment ID
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "doctor");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return prescriptionService.getPrescription(appointmentId);
    }

}

