export function renderHeader() {
    const headerContainer = document.getElementById("header");
    if (!headerContainer) return;

    if (window.location.pathname.endsWith("index.html") || window.location.pathname === "/") {
        return; // Skip rendering header on homepage
    }

    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");

    if (!token && role && role !== "patient") {
        window.location.href = "/pages/index.html"; // Invalid session
        return;
    }

    let headerHTML = `
    <header class="bg-blue-600 text-white px-6 py-4 flex justify-between items-center shadow-md">
      <h1 class="text-xl font-semibold">Clinic Management</h1>
      <nav class="flex space-x-4">
  `;

    if (role === "admin") {
        headerHTML += `
      <a href="/pages/adminDashboard.html" class="hover:text-gray-200">Dashboard</a>
      <a href="/pages/addDoctor.html" class="hover:text-gray-200">Add Doctor</a>
      <button id="logoutBtn" class="hover:text-gray-200">Logout</button>
    `;
    } else if (role === "doctor") {
        headerHTML += `
      <a href="/pages/doctorDashboard.html" class="hover:text-gray-200">Dashboard</a>
      <button id="logoutBtn" class="hover:text-gray-200">Logout</button>
    `;
    } else if (role === "patient") {
        headerHTML += `
      <a href="/pages/login.html" class="hover:text-gray-200">Login</a>
      <a href="/pages/patientRegister.html" class="hover:text-gray-200">Sign Up</a>
    `;
    } else if (role === "loggedPatient") {
        headerHTML += `
      <a href="/pages/patientDashboard.html" class="hover:text-gray-200">Home</a>
      <a href="/pages/appointments.html" class="hover:text-gray-200">Appointments</a>
      <button id="logoutPatientBtn" class="hover:text-gray-200">Logout</button>
    `;
    }

    headerHTML += `
      </nav>
    </header>
  `;

    headerContainer.innerHTML = headerHTML;

    // Attach event listeners
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) logoutBtn.addEventListener("click", logout);

    const logoutPatientBtn = document.getElementById("logoutPatientBtn");
    if (logoutPatientBtn) logoutPatientBtn.addEventListener("click", logoutPatient);
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    window.location.href = "/pages/index.html";
}

function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("patientId");
    window.location.href = "/pages/index.html";
}
