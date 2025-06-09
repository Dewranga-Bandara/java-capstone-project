package com.cdc.javacapstoneproject.utils;

import com.cdc.javacapstoneproject.models.Admin;
import com.cdc.javacapstoneproject.models.Doctor;
import com.cdc.javacapstoneproject.models.Patient;
import com.cdc.javacapstoneproject.repositories.AdminRepository;
import com.cdc.javacapstoneproject.repositories.DoctorRepository;
import com.cdc.javacapstoneproject.repositories.PatientRepository;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final SecretKey key;

    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            @Value("${jwt.secret}") String secret
    ) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // 7 days

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean validateToken(String token, String user) {
        String email = extractEmail(token);
        if (email == null) return false;

        return switch (user.toLowerCase()) {
            case "admin" -> adminRepository.findByEmail(email).isPresent();
            case "doctor" -> doctorRepository.findByEmail(email).isPresent();
            case "patient" -> patientRepository.findByEmail(email).isPresent();
            default -> false;
        };
    }

    public SecretKey getSigningKey() {
        return key;
    }

}

