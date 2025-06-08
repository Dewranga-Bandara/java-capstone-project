export function selectRole(role) {
    localStorage.setItem("role", role);
    if (role === "admin") {
        window.location.href = "/adminDashboard.html";
    } else if (role === "doctor") {
        window.location.href = "/doctorDashboard.html";
    } else if (role === "patient") {
        window.location.href = "/patientDashboard.html";
    }
}
