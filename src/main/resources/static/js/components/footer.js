export function renderFooter() {
    const footerContainer = document.getElementById("footer");
    if (!footerContainer) return;

    footerContainer.innerHTML = `
    <footer class="bg-blue-700 text-white py-4 text-center mt-10">
      <div class="container mx-auto">
        <p>&copy; ${new Date().getFullYear()} Clinic Management System. All rights reserved.</p>
        <div class="mt-2 space-x-4">
          <a href="/pages/index.html" class="hover:underline">Home</a>
          <a href="/pages/about.html" class="hover:underline">About</a>
          <a href="/pages/contact.html" class="hover:underline">Contact</a>
        </div>
      </div>
    </footer>
  `;
}

// Automatically render when file loads
renderFooter();
