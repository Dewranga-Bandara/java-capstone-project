export async function deleteDoctor(id) {
    const res = await fetch(`/api/doctors/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (!res.ok) throw new Error("Failed to delete doctor");
}
