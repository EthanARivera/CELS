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

// === FUNCIONES PARA AÑADIR / ELIMINAR BLOQUES DE MATERIALES ===

function agregarGrupoMaterial(boton) {
    const contenedor = document.getElementById('materialesContainer');
    const grupo = boton.closest('.grupo-material');
    const clon = grupo.cloneNode(true);

    // Limpia los valores de los inputs en el clon
    clon.querySelectorAll('input').forEach(input => input.value = '');

    // Inserta el nuevo grupo al final
    contenedor.appendChild(clon);
}

function eliminarGrupoMaterial(boton) {
    const grupo = boton.closest('.grupo-material');
    const contenedor = document.getElementById('materialesContainer');

    // Solo eliminar si hay más de un grupo
    if (contenedor.querySelectorAll('.grupo-material').length > 1) {
        grupo.remove();
    } else {
        alert("Debe haber al menos un grupo de materiales.");
    }
}

// === FUNCIONES PARA AÑADIR / ELIMINAR BLOQUES DE MANO DE OBRA ===

function agregarGrupoMDO(boton) {
    const contenedor = document.getElementById('manoObraContainer');
    const grupo = boton.closest('.grupo-mdo');
    const clon = grupo.cloneNode(true);

    // Limpia los valores de los inputs en el clon
    clon.querySelectorAll('input').forEach(input => input.value = '');

    // Inserta el nuevo grupo al final
    contenedor.appendChild(clon);
}

function eliminarGrupoMDO(boton) {
    const grupo = boton.closest('.grupo-mdo');
    const contenedor = document.getElementById('manoObraContainer');

    // Solo eliminar si hay más de un grupo
    if (contenedor.querySelectorAll('.grupo-mdo').length > 1) {
        grupo.remove();
    } else {
        alert("Debe haber al menos un grupo de mano de obra.");
    }
}
