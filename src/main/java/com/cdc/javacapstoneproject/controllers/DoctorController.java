package com.cdc.javacapstoneproject.controllers;

import com.cdc.javacapstoneproject.dto.Login;
import com.cdc.javacapstoneproject.models.Doctor;
import com.cdc.javacapstoneproject.services.ClinicService;
import com.cdc.javacapstoneproject.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicService clinicService;

    // 1. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable String user,
                                                   @PathVariable Long doctorId,
                                                   @PathVariable String date,
                                                   @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, user);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        try {
            LocalDate localDate = LocalDate.parse(date); // Convert string to LocalDate (format: yyyy-MM-dd)
            List<String> availability = doctorService.getDoctorAvailability(doctorId, localDate);
            return ResponseEntity.ok(Collections.singletonMap("availability", availability));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid date format. Use yyyy-MM-dd"));
        }
    }

    // 2. Get List of Doctors
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok(Collections.singletonMap("doctors", doctors));
    }

    // 3. Add New Doctor (admin only)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(@PathVariable String token,
                                                         @RequestBody Doctor doctor) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Map<String, String> response = new HashMap<>();
        try {
            int result = doctorService.saveDoctor(doctor);
            if (result == 0) {
                response.put("message", "Doctor already exists");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            response.put("message", "Doctor added to db");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", "Some internal error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginDoctor(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }


    // 5. Update Doctor Details (admin only)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@PathVariable String token,
                                                            @RequestBody Doctor doctor) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Map<String, String> response = new HashMap<>();
        try {
            int result = doctorService.updateDoctor(doctor);
            if (result == -1) {
                response.put("error", "Doctor not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else if (result == 1) {
                response.put("message", "Doctor updated");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("error", "Some internal error occurred");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            response.put("error", "Some internal error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 6. Delete Doctor (admin only)
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id,
                                                            @PathVariable String token) {
        ResponseEntity<Map<String, String>> validation = clinicService.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        Map<String, String> response = new HashMap<>();
        try {
            int result = doctorService.deleteDoctor(id);
            if (result == -1) {
                response.put("error", "Doctor not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else if (result == 0) {
                response.put("error", "Some internal error occurred");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("message", "Doctor deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", "Some internal error occurred");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 7. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filterDoctors(@PathVariable String name,
                                           @PathVariable String time,
                                           @PathVariable String speciality) {
        List<Doctor> filteredDoctors = clinicService.filterDoctor(name, time, speciality);
        return ResponseEntity.ok(Collections.singletonMap("doctors", filteredDoctors));
    }
}
