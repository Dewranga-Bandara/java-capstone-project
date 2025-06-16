# Doctor User Stories

---

**Title:** Doctor Login  
_As a doctor, I want to log into the portal, so that I can manage my appointments securely._  
**Acceptance Criteria:**  
1. Doctor can enter username and password  
2. Credentials are authenticated  
3. Doctor dashboard is displayed upon successful login  
**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Redirect to login page if session expires

---

**Title:** Doctor Logout  
_As a doctor, I want to log out of the portal, so that my data stays protected._  
**Acceptance Criteria:**  
1. Logout button is accessible from dashboard  
2. Session is invalidated after logout  
3. Redirect to login/home page  
**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Automatic logout on inactivity should be considered

---

**Title:** View Appointment Calendar  
_As a doctor, I want to view my appointment calendar, so that I stay organized._  
**Acceptance Criteria:**  
1. Doctor can see calendar view  
2. Each day shows appointments with patient name and time  
3. Click on a slot shows detailed appointment info  
**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Should support daily, weekly, and monthly views

---

**Title:** Mark Unavailability  
_As a doctor, I want to mark my unavailability, so that patients can only book available time slots._  
**Acceptance Criteria:**  
1. Doctor can select date/time ranges as unavailable  
2. These times are removed from patient booking options  
3. Confirmation before saving unavailability  
**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Unavailable slots should visually appear in the calendar

---

**Title:** Update Profile  
_As a doctor, I want to update my specialization and contact info, so that patients have up-to-date information._  
**Acceptance Criteria:**  
1. Editable profile form available  
2. Required fields are validated  
3. Changes are saved and reflected immediately  
**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Consider image upload for profile picture

---

**Title:** View Patient Details  
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._  
**Acceptance Criteria:**  
1. Clickable appointment slot shows patient details  
2. View includes name, age, contact, and past prescriptions  
3. Data should be read-only  
**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Include emergency contact info if available
