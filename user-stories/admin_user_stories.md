# Admin User Stories

---

## ✅ User Story 1: Admin Login

**Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. Admin is prompted to enter a valid username and password.  
2. Successful login redirects the admin to the dashboard.  
3. Invalid credentials show an appropriate error message.

**Priority:** High  
**Story Points:** 2  

**Notes:**  
- Must implement authentication using Spring Security.

---

## ✅ User Story 2: Admin Logout

**Title:**  
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**  
1. A logout option is clearly visible in the dashboard.  
2. Clicking logout ends the session and redirects to the login page.  
3. Session cookies are invalidated on logout.

**Priority:** High  
**Story Points:** 1  

**Notes:**  
- Ensure CSRF tokens are handled correctly.

---

## ✅ User Story 3: Add Doctor Profile

**Title:**  
_As an admin, I want to add doctors to the portal, so that they can access the system and serve patients._

**Acceptance Criteria:**  
1. Admin can access a "New Doctor" form from the dashboard.  
2. All mandatory fields (name, specialization, availability) must be filled.  
3. On submission, the doctor is saved in the MySQL database and appears in the list.

**Priority:** High  
**Story Points:** 3  

**Notes:**  
- Backend should validate email format and prevent duplicate entries.

---

## ✅ User Story 4: Delete Doctor Profile

**Title:**  
_As an admin, I want to delete a doctor's profile from the portal, so that I can manage system users effectively._

**Acceptance Criteria:**  
1. Admin can see a list of doctors with delete options.  
2. Clicking delete prompts a confirmation.  
3. Upon confirmation, the doctor is removed from the system.

**Priority:** Medium  
**Story Points:** 2  

**Notes:**  
- Prevent deletion if the doctor has upcoming appointments.

---

## ✅ User Story 5: Run Monthly Appointment Stats Procedure

**Title:**  
_As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**  
1. Stored procedure exists in the database.  
2. Admin can execute the procedure and view monthly appointment counts.  
3. Output includes month and total appointments.

**Priority:** Medium  
**Story Points:** 3  

**Notes:**  
- Consider integrating this into a dashboard view in future iterations.
