export function openModal(type) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");

    if (type === "adminLogin") {
        modalBody.innerHTML = `
            <h3>Admin Login</h3>
            <input type="text" id="adminUsername" placeholder="Username" /><br>
            <input type="password" id="adminPassword" placeholder="Password" /><br>
            <button onclick="adminLoginHandler()">Login</button>
        `;
    } else if (type === "doctorLogin") {
        modalBody.innerHTML = `
            <h3>Doctor Login</h3>
            <input type="email" id="doctorEmail" placeholder="Email" /><br>
            <input type="password" id="doctorPassword" placeholder="Password" /><br>
            <button onclick="doctorLoginHandler()">Login</button>
        `;
    }

    modal.style.display = "block";

    document.getElementById("closeModal").onclick = () => {
        modal.style.display = "none";
    };

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };
}
