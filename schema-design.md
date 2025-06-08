# 📊 Smart Clinic Management System – Schema Design

This document outlines the database design for the Smart Clinic Management System using both **MySQL** (for structured relational data) and **MongoDB** (for flexible document-based data).

---

## 🗃️ MySQL Database Design

MySQL will handle core structured data: Patients, Doctors, Appointments, and Admins.

### Table: patients
- `id`: INT, Primary Key, AUTO_INCREMENT
- `first_name`: VARCHAR(50), NOT NULL
- `last_name`: VARCHAR(50), NOT NULL
- `email`: VARCHAR(100), NOT NULL, UNIQUE
- `phone`: VARCHAR(15), NOT NULL
- `gender`: ENUM('Male', 'Female', 'Other')
- `dob`: DATE
- `created_at`: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

### Table: doctors
- `id`: INT, Primary Key, AUTO_INCREMENT
- `first_name`: VARCHAR(50), NOT NULL
- `last_name`: VARCHAR(50), NOT NULL
- `email`: VARCHAR(100), NOT NULL, UNIQUE
- `phone`: VARCHAR(15), NOT NULL
- `specialization`: VARCHAR(100)
- `availability`: TEXT — JSON string for flexibility (e.g., available days & time slots)
- `created_at`: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

### Table: admins
- `id`: INT, Primary Key, AUTO_INCREMENT
- `username`: VARCHAR(50), NOT NULL, UNIQUE
- `password_hash`: VARCHAR(255), NOT NULL
- `email`: VARCHAR(100), NOT NULL
- `role`: ENUM('SuperAdmin', 'Admin')

### Table: appointments
- `id`: INT, Primary Key, AUTO_INCREMENT
- `patient_id`: INT, Foreign Key → patients(id)
- `doctor_id`: INT, Foreign Key → doctors(id)
- `appointment_time`: DATETIME, NOT NULL
- `status`: ENUM('Scheduled', 'Completed', 'Cancelled') DEFAULT 'Scheduled'
- `notes`: TEXT

### Table: payments (Optional)
- `id`: INT, Primary Key, AUTO_INCREMENT
- `appointment_id`: INT, Foreign Key → appointments(id)
- `amount`: DECIMAL(10,2), NOT NULL
- `payment_status`: ENUM('Pending', 'Paid', 'Failed')
- `timestamp`: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

---

## 🍃 MongoDB Collection Design

MongoDB will handle dynamic, optional, or hierarchical data such as prescriptions and feedback.

### Collection: prescriptions

```json
{
  "_id": "ObjectId('664fa7a1bc12345678')",
  "appointmentId": 101,
  "patientId": 25,
  "doctorId": 12,
  "patientName": "Alice Johnson",
  "medication": [
    {
      "name": "Amoxicillin",
      "dosage": "250mg",
      "frequency": "3 times a day"
    },
    {
      "name": "Ibuprofen",
      "dosage": "400mg",
      "frequency": "Twice a day"
    }
  ],
  "notes": "Take medications with food. Avoid alcohol.",
  "createdAt": "2025-06-08T09:00:00Z",
  "pharmacy": {
    "name": "Green Cross Pharmacy",
    "location": "5th Avenue"
  },
  "refillCount": 1
}
