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


// === FUNCIONES PARA AÑADIR / ELIMINAR BLOQUES DE MATERIALES ==
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

    if (contenedor.querySelectorAll('.grupo-material').length > 1) {
        grupo.remove();
        actualizarTotales(); // recalcula después de eliminar
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
        actualizarTotales(); // recalcula después de eliminar
    } else {
        alert("Debe haber al menos un grupo de mano de obra.");
    }
}


// === CÁLCULO AUTOMÁTICO DE TOTALES (INDIVIDUALES Y GENERALES) ===
document.addEventListener("input", function (e) {
    // --- MATERIALES ---
    if (e.target.closest(".grupo-material")) {
        const grupo = e.target.closest(".grupo-material");
        const costo = parseFloat(grupo.querySelector(".costo-unitario")?.value) || 0;
        const cantidad = parseFloat(grupo.querySelector(".cantidad")?.value) || 0;
        const total = grupo.querySelector(".total-material");
        if (total) total.value = (costo * cantidad).toFixed(2);
    }

    // --- MANO DE OBRA ---
    if (e.target.closest(".grupo-mdo")) {
        const grupo = e.target.closest(".grupo-mdo");
        const costoHora = parseFloat(grupo.querySelector(".costo-hora")?.value) || 0;
        const horas = parseFloat(grupo.querySelector(".cantidad-horas")?.value) || 0;
        const total = grupo.querySelector(".total-mdo");
        if (total) total.value = (costoHora * horas).toFixed(2);
    }

    // --- ACTUALIZAR LOS TOTALES GLOBALES ---
    actualizarTotales();
});

function actualizarTotales() {
    // === SUMAR TODOS LOS MATERIALES ===
    const materiales = document.querySelectorAll(".total-material");
    let sumaMateriales = 0;
    materiales.forEach(input => {
        sumaMateriales += parseFloat(input.value) || 0;
    });

    // === SUMAR TODAS LAS MANOS DE OBRA ===
    const mdo = document.querySelectorAll(".total-mdo");
    let sumaMDO = 0;
    mdo.forEach(input => {
        sumaMDO += parseFloat(input.value) || 0;
    });

    // === MOSTRAR LOS RESULTADOS ===
    const campoMateriales = document.getElementById("totalMateriales");
    const campoManoObra = document.getElementById("totalManoObra");
    const campoBruto = document.getElementById("costoBruto");

    if (campoMateriales) campoMateriales.value = sumaMateriales.toFixed(2);
    if (campoManoObra) campoManoObra.value = sumaMDO.toFixed(2);
    if (campoBruto) campoBruto.value = (sumaMateriales + sumaMDO).toFixed(2);

    // === CALCULAR COSTO FINAL CON GANANCIA ===
    calcularCostoFinal();
}

function calcularCostoFinal() {
    const bruto = parseFloat(document.getElementById("costoBruto")?.value) || 0;
    const ganancia = parseFloat(document.getElementById("indiceGanancia")?.value) || 0;
    const campoFinal = document.getElementById("costoFinal");

    if (campoFinal) {
        const totalConGanancia = bruto + (bruto * (ganancia / 100));
        campoFinal.value = totalConGanancia.toFixed(2);
    }
}

// Recalcular también si cambia el índice de ganancia
document.getElementById("indiceGanancia")?.addEventListener("input", calcularCostoFinal);