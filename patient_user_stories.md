# Patient User Stories

---

**Title:**  
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._  

**Acceptance Criteria:**  
1. Doctor list is publicly accessible.  
2. Each doctor profile shows name, specialization, and availability.  
3. The list is searchable and paginated.  

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Access should not require authentication.

---

**Title:**  
_As a patient, I want to sign up using my email and password, so that I can book appointments._  

**Acceptance Criteria:**  
1. Sign-up form includes email, password, and basic profile fields.  
2. Duplicate emails are not allowed.  
3. Success message is shown after registration.  

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Include email verification if time permits.

---

**Title:**  
_As a patient, I want to log into the portal, so that I can manage my bookings._  

**Acceptance Criteria:**  
1. Login form accepts valid credentials.  
2. Invalid attempts show appropriate error messages.  
3. On success, user is redirected to the dashboard.  

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Use session or token-based authentication.

---

**Title:**  
_As a patient, I want to log out of the portal, so that I can secure my account._  

**Acceptance Criteria:**  
1. Logout button is visible on all logged-in pages.  
2. On logout, session/token is destroyed.  
3. User is redirected to the homepage/login screen.  

**Priority:** Medium  
**Story Points:** 1  
**Notes:**  
- Session timeout should also be considered.

---

**Title:**  
_As a patient, I want to log in and book an hour-long appointment, so that I can consult with a doctor._  

**Acceptance Criteria:**  
1. Patient selects doctor, date, and time.  
2. System checks doctor availability.  
3. Appointment is confirmed and visible in dashboard.  

**Priority:** High  
**Story Points:** 5  
**Notes:**  
- One-hour duration should be enforced programmatically.

---

**Title:**  
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._  

**Acceptance Criteria:**  
1. Upcoming appointments are shown on the dashboard.  
2. Each entry includes doctor name, date, and time.  
3. Appointments are sorted by date.  

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Support filtering for canceled/rescheduled appointments later.
