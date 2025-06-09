package com.cdc.javacapstoneproject.services;

import com.cdc.javacapstoneproject.models.Appointment;
import com.cdc.javacapstoneproject.models.Doctor;
import com.cdc.javacapstoneproject.dto.Login;
import com.cdc.javacapstoneproject.repositories.AppointmentRepository;
import com.cdc.javacapstoneproject.repositories.DoctorRepository;
import com.cdc.javacapstoneproject.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    private static final List<String> ALL_SLOTS = Arrays.asList(
            "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00"
    );

    // --------------------------
    // Doctor Availability
    // --------------------------

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        Set<String> bookedSlots = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString().substring(0, 5))
                .collect(Collectors.toSet());

        return ALL_SLOTS.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    // --------------------------
    // CRUD Operations
    // --------------------------

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()).isPresent()) {
                return -1; // Already exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.findById(doctor.getId()).isPresent()) {
                return -1; // Not found
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public int deleteDoctor(long id) {
        try {
            if (!doctorRepository.findById(id).isPresent()) {
                return -1; // Not found
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Error
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // --------------------------
    // Login Validation
    // --------------------------

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Optional<Doctor> doctor = doctorRepository.findByEmail(login.getEmail());

        if (doctor.isPresent() && doctor.get().getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(login.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", "Invalid credentials"));
    }

    // --------------------------
    // Search & Filtering
    // --------------------------

    public ResponseEntity<Map<String, Object>> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return ResponseEntity.ok(Collections.singletonMap("doctors", doctors));
    }

    public ResponseEntity<Map<String, Object>> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(Collections.singletonMap("doctors", filterDoctorByTime(doctors, amOrPm)));
    }

    public ResponseEntity<Map<String, Object>> filterDoctorByNameAndSpecialty(String name, String specialty) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return ResponseEntity.ok(Collections.singletonMap("doctors", doctors));
    }

    public ResponseEntity<Map<String, Object>> filterDoctorBySpecialtyAndTime(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return ResponseEntity.ok(Collections.singletonMap("doctors", filterDoctorByTime(doctors, amOrPm)));
    }

    public ResponseEntity<Map<String, Object>> filterDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return ResponseEntity.ok(Collections.singletonMap("doctors", doctors));
    }

    public ResponseEntity<Map<String, Object>> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok(Collections.singletonMap("doctors", filterDoctorByTime(doctors, amOrPm)));
    }

    // --------------------------
    // Time Filtering Helper
    // --------------------------

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream().filter(doc -> {
            List<String> times = doc.getAvailableTimes();
            return times.stream().anyMatch(t -> {
                int hour = Integer.parseInt(t.split(":")[0]);
                return "AM".equalsIgnoreCase(amOrPm) ? hour < 12 : hour >= 12;
            });
        }).collect(Collectors.toList());
    }

    public List<Doctor> filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTime(doctors, amOrPm);
    }

}
