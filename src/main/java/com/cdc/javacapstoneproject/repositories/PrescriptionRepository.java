package com.cdc.javacapstoneproject.repositories;


import com.cdc.javacapstoneproject.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    // Find all prescriptions related to a specific appointment ID
    List<Prescription> findByAppointmentId(Long appointmentId);
}
