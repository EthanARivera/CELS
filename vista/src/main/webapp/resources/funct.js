// Función para mostrar/ocultar el menú
function toggleMenu() {
    const menu = document.getElementById('menuPanel');
    const isVisible = menu.style.display === 'flex';
    menu.style.display = isVisible ? 'none' : 'flex';
}

// Cerrar el menú al hacer clic fuera de él
document.addEventListener('click', function(event) {
    const menu = document.getElementById('menuPanel');
    const menuButton = document.querySelector('.menu'); // botón hamburguesa

    // Verifica que el menú esté visible
    if (menu && menu.style.display === 'flex') {
        // Si el clic NO ocurrió dentro del menú ni en el botón del menú, lo cerramos
        if (!menu.contains(event.target) && !menuButton.contains(event.target)) {
            menu.style.display = 'none';
        }
    }
});