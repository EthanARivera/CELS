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

// VARIABLES GLOBALES

// Variables para Materiales
let catalogoMateriales = [];      // Lista completa enviada desde el backend
let materialesUsados = new Set(); // IDs ya seleccionados
let filasMateriales = [];         // Representación para el JSON enviado al backend

// Variables para Mano de Obra
let contadorMDO = 1;
let manoDeObraItems = [];

document.addEventListener("DOMContentLoaded", () => {
    cargarCatalogo();
});

function cargarCatalogo() {
    const hidden = document.getElementById("formCotizacion:jsonMaterialesHidden");
    try {
        catalogoMateriales = JSON.parse(hidden.value);
    } catch (e) {
        console.error("Error leyendo catálogo de materiales", e);
        catalogoMateriales = [];
    }
}

// OBTENER HIDDENS
function getHidden(idSuffix) {
    // intenta clientId completo primero
    const full = document.getElementById("formCotizacion:" + idSuffix);
    if (full) return full;
    // fallback: buscar por id que termine con suffix
    return document.querySelector("[id$='" + idSuffix + "']");
}

// AGREGAR UNA FILA DE MATERIAL

function agregarFilaMaterial() {
    const tbody = document.getElementById("tbodyMateriales");

    const tr = document.createElement("tr");
    tr.classList.add("fila-material");

    tr.innerHTML = `
        <td>
            <input type="text" class="input-cot material-input" placeholder="Buscar material..." />
            <span class="mat-id" style="display:none;"></span>
        </td>
        <td><input type="number" class="input-cot cantidad" min="0" step="1" /></td>
        <td><input type="text" class="input-cot precio" readonly /></td>
        <td><input type="text" class="input-cot subtotal" readonly /></td>
        <td>
            <button type="button" class="btn btn-danger" onclick="eliminarFilaMaterial(this)">
                <i class="pi pi-trash"></i>
            </button>
        </td>
    `;

    tbody.appendChild(tr);

    configurarAutocomplete(tr);
}


// CONFIGURAR AUTOCOMPLETE DE LA FILA

function configurarAutocomplete(tr) {
    const input = tr.querySelector(".material-input");

    const listaDisponible = catalogoMateriales
        .filter(m => !materialesUsados.has(m.id));

    const aw = new Awesomplete(input, {
        list: listaDisponible.map(m => m.nombre),
        minChars: 1,
        autoFirst: true
    });

    input.addEventListener("awesomplete-selectcomplete", () => {
        onMaterialSeleccionado(tr, input.value);
    });

    actualizarTotales();
}


// CUANDO SE SELECCIONA UN MATERIAL

function onMaterialSeleccionado(tr, nombreBuscado) {
    const mat = catalogoMateriales.find(m => m.nombre === nombreBuscado);
    if (!mat) return;

    // Guardar el ID visualmente
    tr.querySelector(".mat-id").textContent = mat.id;

    // Guardar dataset necesario para calcular subtotal y JSON final
    tr.dataset.id = mat.id;
    tr.dataset.costo = mat.costo;

    // Llenar precio unitario
    tr.querySelector(".precio").value = mat.costo;

    materialesUsados.add(mat.id);

    // Cuando cambie la cantidad, recalcular
    tr.querySelector(".cantidad").addEventListener("input", () => {
        calcularSubtotalMaterial(tr);
    });

    calcularSubtotalMaterial(tr);
}

// CALCULAR SUBTOTAL DE UNA FILA

function calcularSubtotalMaterial(tr) {
    const costo = parseFloat(tr.dataset.costo) || 0;
    const cantidad = parseFloat(tr.querySelector(".cantidad").value) || 0;

    if (cantidad < 0) {
        tr.querySelector(".cantidad").value = 0;
    }
    const subtotal = costo * cantidad;

    // Como tu subtotal es un input .subtotal
    tr.querySelector(".subtotal").value = subtotal.toFixed(2);

    actualizarTotales();
    prepararJSONMateriales();
}



// ELIMINAR UNA FILA DE MATERIAL

function eliminarFilaMaterial(btn) {
    const tr = btn.closest("tr");
    const id = tr.querySelector(".mat-id").textContent;

    if (id) {
        materialesUsados.delete(parseInt(id));
    }

    tr.remove();
    prepararJSONMateriales();
    actualizarTotales();
}


// CONSTRUIR JSON PARA EL BACKEND

function prepararJSONMateriales() {
    const filas = document.querySelectorAll(".fila-material");
    const lista = [];

    filas.forEach(fila => {
        lista.push({
            idMaterial: parseInt(fila.dataset.id) || 0,
            cantidad: parseFloat(fila.querySelector(".cantidad").value) || 0,
            subtotal: parseFloat(fila.querySelector(".subtotal").value) || 0
        });
    });

    const hidden = getHidden("jsonTablaMateriales");
    hidden.value = JSON.stringify(lista);

    console.log("JSON Materiales generado:", hidden.value);
}

function prepararJSONManoDeObra() {
    const filas = document.querySelectorAll(".mano-obra-item");
    const lista = [];

    filas.forEach(fila => {
        const numResponsable = parseInt(fila.querySelector(".num-responsable").value) || 0;
        const costoHora = parseFloat(fila.querySelector(".costo-hora").value) || 0;
        const cantidadHoras = parseFloat(fila.querySelector(".cantidad-horas").value) || 0;
        const subtotal = parseFloat(fila.querySelector(".subtotal-mdo").value) || 0;

        lista.push({
            numResponsable: numResponsable,
            costoHora: costoHora,
            cantidadHoras: cantidadHoras,
            subtotal: subtotal
        });
    });

    // IMPORTANTE: el bean usa jsonTablaManoObra
    const hidden = getHidden("jsonManoObraHidden");
    hidden.value = JSON.stringify(lista);
}


// Apoyo para awesomeplete
function restaurarTablaMateriales() {
    const filas = document.querySelectorAll("#tbodyMateriales tr");

    materialesUsados.clear();

    filas.forEach(tr => {
        const id = parseInt(tr.querySelector(".mat-id").textContent);
        if (!id) return;

        const mat = catalogoMateriales.find(m => m.id === id);
        if (!mat) return;

        // restaurar el nombre para Awesomplete
        tr.querySelector(".material-input").value = mat.nombre;

        // restaurar precio
        tr.querySelector(".precio").value = mat.costo;

        materialesUsados.add(mat.id);

        // restaurar subtotal cuando cambie cantidad
        tr.querySelector(".cantidad").addEventListener("input", () => {
            calcularSubtotalMaterial(tr);
        });
    });

    actualizarTotales();
}

//Funciones para Mano de Obra
function agregarFilaMDO() {
    const tabla = document.getElementById("tbodyMDO");

    const fila = document.createElement("tr");
    fila.classList.add("mano-obra-item");

    // AUTOGENERAR número responsable
    const num = contadorMDO++;

    fila.innerHTML = `
        <td><input type="number" class="input-mdo num-responsable" value="${num}" readonly></td>
        <td><input type="number" class="input-mdo costo-hora" placeholder="Costo/Hora" min="0" step="0.01" oninput="calcularSubtotalMDO(this)"></td>
        <td><input type="number" class="input-mdo cantidad-horas" placeholder="Horas" min="0" step="0.01" oninput="calcularSubtotalMDO(this)"></td>
        <td><input type="number" class="input-mdo subtotal-mdo" placeholder="Subtotal" readonly></td>
        <td class="acciones">
            <button type="button" class="boton-eliminar" onclick="eliminarFilaMDO(this)">
                <i class="pi pi-trash"></i>
            </button>
        </td>
    `;

    tabla.appendChild(fila);
}

function calcularSubtotalMDO(elemento) {
    const fila = elemento.closest("tr");
    const costo = parseFloat(fila.querySelector(".costo-hora").value) || 0;
    const horas = parseFloat(fila.querySelector(".cantidad-horas").value) || 0;

    if (costo < 0) {
        fila.querySelector(".costo-hora").value = 0;
    }

    if (horas < 0) {
        fila.querySelector(".cantidad-horas").value = 0;
    }

    const subtotal = costo * horas;
    fila.querySelector(".subtotal-mdo").value = subtotal.toFixed(2);

    actualizarTotales();
    prepararJSONManoDeObra();
}


function eliminarFilaMDO(btn) {
    const fila = btn.parentNode.parentNode;
    fila.remove();
    prepararJSONManoDeObra();
    actualizarTotales();
}

function recalcularFilaMDO(input) {
    const fila = input.parentNode.parentNode;

    const costo = parseFloat(fila.children[1].children[0].value) || 0;
    const horas = parseFloat(fila.children[2].children[0].value) || 0;

    const subtotal = costo * horas;
    fila.children[3].children[0].value = subtotal.toFixed(2);

    prepararJSONManoDeObra();
    actualizarTotales();
}

//Validar que los campos no se encuentren vacios
function validarFormularioCotizacion() {
    const materiales = document.querySelectorAll(".fila-material");
    const mdo = document.querySelectorAll(".mano-obra-item");

    if (materiales.length === 0 && mdo.length === 0) {
        alert("Debe agregar al menos un material o un responsable de mano de obra.");
        return false;
    }

    return true;
}


/******************************************************************
                    CALCULOS DE TOTALES COMPLETOS
 ******************************************************************/

function actualizarTotales() {
    // 1) subtotal materiales desde DOM
    let totalMateriales = 0;
    document.querySelectorAll("#tbodyMateriales tr").forEach(tr => {
        const subEl = tr.querySelector(".subtotal");
        const v = subEl ? parseFloat(subEl.value) : 0;
        totalMateriales += (isNaN(v) ? 0 : v);
    });

    // 2) subtotal mano de obra desde DOM
    let totalMDO = 0;
    document.querySelectorAll("#tbodyMDO tr").forEach(tr => {
        const subEl = tr.querySelector(".subtotal-mdo");
        const v = subEl ? parseFloat(subEl.value) : 0;
        totalMDO += (isNaN(v) ? 0 : v);
    });

    // 3) costo bruto
    const costoBruto = totalMateriales + totalMDO;

    // 4) ganancia
    const ganInput = document.getElementById("formCotizacion:indiceGanancia");
    const gan = ganInput ? parseFloat(ganInput.value) : 0;
    const multiplicador = 1 + (isNaN(gan) ? 0 : gan / 100);
    let costoConGan = costoBruto * multiplicador;

    // 5) facturación 8%
    const requiereFact = document.getElementById("requiereFactCheckbox");
    if (requiereFact && requiereFact.checked) {
        costoConGan *= 1.08;
    }

    // 6) escribir en los campos (usar clientIds)
    const totalMatEl = document.getElementById("formCotizacion:totalMateriales");
    if (totalMatEl) totalMatEl.value = totalMateriales.toFixed(2);

    const totalMDOEl = document.getElementById("formCotizacion:totalManoObra");
    if (totalMDOEl) totalMDOEl.value = totalMDO.toFixed(2);

    const brutoEl = document.getElementById("formCotizacion:costoBruto");
    if (brutoEl) brutoEl.value = costoBruto.toFixed(2);

    const finalEl = document.getElementById("formCotizacion:costoFinal");
    if (finalEl) finalEl.value = costoConGan.toFixed(2);
}

//  AQUÍ COMIENZAN LAS FUNCIONES EXCLUSIVAS PARA PBI-CO-US13
function verificarYcargarEdicion() {

    // Para Materiales
    const hiddenMat = document.getElementById("formCotizacion:jsonTablaMateriales");

    // Para Mano de Obra
    const hiddenMdo = document.getElementById("formCotizacion:jsonManoObraHidden");

    // --- VALIDACIÓN DE SEGURIDAD ---
    // Si estamos en "Altas", estos inputs tendrán "[]" o estarán vacíos.
    // Si es así, NO HACEMOS NADA. Así no rompemos nada.
    if (hiddenMat && hiddenMat.value && hiddenMat.value !== "[]" && hiddenMat.value.length > 2) {
        console.log("Modo Edición Detectado: Cargando materiales...");
        _cargarMaterialesExclusivo(hiddenMat.value);
    }

    if (hiddenMdo && hiddenMdo.value && hiddenMdo.value !== "[]" && hiddenMdo.value.length > 2) {
        console.log("Modo Edición Detectado: Cargando mano de obra...");
        _cargarManoObraExclusivo(hiddenMdo.value);
    }
}

// Función para cargar materiales
function _cargarMaterialesExclusivo(jsonTexto) {
    try {
        const lista = JSON.parse(jsonTexto);
        const tbody = document.getElementById("tbodyMateriales");

        // Limpiamos tabla
        tbody.innerHTML = "";

        lista.forEach(item => {
            agregarFilaMaterial(); // Usamos la función original de Altas para crear el hueco
            const tr = tbody.lastElementChild;

            // Llenamos visualmente
            // Nota: Agregamos el "?" para evitar error si el ID no existe en catalogo
            const nombreMaterial = catalogoMateriales.find(m => m.id === item.idMaterial)?.nombre || "Material Guardado";

            tr.querySelector(".material-input").value = nombreMaterial;
            tr.querySelector(".cantidad").value = item.cantidad;
            tr.querySelector(".precio").value = item.costo;
            tr.querySelector(".subtotal").value = item.subtotal;

            // Llenamos datos ocultos (dataset)
            tr.querySelector(".mat-id").textContent = item.idMaterial;
            tr.dataset.id = item.idMaterial;
            tr.dataset.costo = item.costo;

            materialesUsados.add(item.idMaterial);

            // Reactivamos listeners
            tr.querySelector(".cantidad").addEventListener("input", () => {
                calcularSubtotalMaterial(tr);
            });
        });
        actualizarTotales();
    } catch (e) {
        console.error("Error (Materiales):", e);
    }
}

// Función Mano Obra
function _cargarManoObraExclusivo(jsonTexto) {
    try {
        const lista = JSON.parse(jsonTexto);
        const tbody = document.getElementById("tbodyMDO");
        tbody.innerHTML = "";

        lista.forEach(item => {
            agregarFilaMDO();
            const tr = tbody.lastElementChild;

            tr.querySelector(".num-responsable").value = item.numResponsable;
            tr.querySelector(".costo-hora").value = item.costoHora;
            tr.querySelector(".cantidad-horas").value = item.cantidadHoras;
            tr.querySelector(".subtotal-mdo").value = item.subtotal;

            if(item.numResponsable >= contadorMDO) {
                contadorMDO = item.numResponsable + 1;
            }
        });
        actualizarTotales();
    } catch (e) {
        console.error("Error (Mano Obra):", e);
    }
    }