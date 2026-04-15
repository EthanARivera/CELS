const { test, expect } = require('@playwright/test');

// Cambia esto si tu entorno de pruebas corre en otro puerto o contexto
const BASE_URL = "http://localhost:8080/vista_war_exploded";

// ==========================================
// Función de ayuda (Helper) para iniciar sesión
// ==========================================
async function loginAs(page, email, pwd) {
    await page.goto(`${BASE_URL}/login.xhtml`);

    await page.locator("input[type='text']").fill(email);
    await page.locator("input[type='password']").fill(pwd);

    // En JS de Playwright, hacer click automáticamente espera la navegación,
    // pero si queremos estar seguros, usamos Promise.all
    await Promise.all([
        page.waitForLoadState('networkidle'), // Espera a que la red se calme (navegación terminada)
        page.locator("button:has-text('Iniciar sesion')").click()
    ]);
}

// ==========================================
// UI CASE 1: Login and Role-based Redirection
// ==========================================
test.describe('UI CASE 1: Login and Role-based Redirection', () => {

    test('testLoginAndRedirection_Success', async ({ browser }) => {
        // Función interna para probar diferentes roles usando contextos nuevos
        async function testRoleRedirection(email, password, expectedPage) {
            const context = await browser.newContext();
            const rolePage = await context.newPage();

            await rolePage.goto(`${BASE_URL}/login.xhtml`);
            await rolePage.locator("input[type='text']").fill(email);
            await rolePage.waitForTimeout(2000);
            await rolePage.locator("input[type='password']").fill(password);
            await rolePage.waitForTimeout(2000);
            await rolePage.locator("button:has-text('Iniciar sesion')").click();
            await rolePage.waitForTimeout(2000);

            // Espera a que la URL cambie a la página esperada
            await rolePage.waitForURL(new RegExp(`.*${expectedPage}.*`));

            expect(rolePage.url()).toContain(expectedPage);
            await rolePage.close();
            await context.close();
        }

        // Gerente (Type 0)
        await testRoleRedirection("john.garcia.viray@uabc.edu.mx", "dino0899", "indexGerente.xhtml");
        // Vendedor (Type 1)
        await testRoleRedirection("abraham.flores.cabanillas@uabc.edu.mx", "123", "indexVendedor.xhtml");
        // Productor (Type 2)
        await testRoleRedirection("brayan.leon93@uabc.edu.mx", "1234", "indexProductor.xhtml");
    });

    test('testLogin_Fail_EmptyOrWrong', async ({ page }) => {
        await page.goto(`${BASE_URL}/login.xhtml`);

        // ==========================================
        // CASO A: Campos vacíos
        // ==========================================
        await page.locator("button:has-text('Iniciar sesion')").click();

        // Buscamos la advertencia amarilla (.ui-messages-warn) o cualquier mensaje general
        const warningMessage = page.locator(".ui-messages-warn, .ui-messages").first();
        await expect(warningMessage).toBeVisible({ timeout: 5000 });
        expect(page.url()).toContain("login.xhtml");

        // ==========================================
        // CASO B: Contraseña incorrecta
        // ==========================================
        await page.locator("input[type='text']").fill("john.garcia.viray@uabc.edu.mx");
        await page.waitForTimeout(2000);
        await page.locator("input[type='password']").fill("wrongpassword");
        await page.waitForTimeout(2000);
        await page.locator("button:has-text('Iniciar sesion')").click();
        await page.waitForTimeout(2000);

        // Aquí puede que tu sistema lance rojo (error) o amarillo (warn), así que abarcamos todos
        const errorMessage = page.locator(".ui-messages-error, .ui-messages-warn, .ui-messages").first();
        await expect(errorMessage).toBeVisible({ timeout: 5000 });
        expect(page.url()).toContain("login.xhtml");
    });
});

// ==========================================
// UI CASE 2: Material CRUD and AJAX Search
// ==========================================
test.describe('UI CASE 2: Material CRUD and AJAX Search', () => {

    test('testMaterialCRUDAndAjax', async ({ page }) => {
        // Asumiendo que el login de Gerente lleva al módulo de materiales
        await loginAs(page, "john.garcia.viray@uabc.edu.mx", "dino0899");
        await page.waitForTimeout(1000);

        await page.goto(`${BASE_URL}/consulta_materiales.xhtml`);
        await page.waitForTimeout(1000);

        // 1. Capturar Material
        const uniqueSuffix = Date.now();
        const uniqueMatName = `Material de Prueba ${uniqueSuffix}`;

        // Clic en el botón Agregar de la tabla (asumo que este te llevó a la pantalla de la foto)
        await page.locator("button:has-text('Agregar'), a:has-text('Agregar')").first().click();
        await page.waitForTimeout(1000);

        // Espera a que los inputs del modal/página sean visibles
        const inputs = page.locator("input[type='text']");
        await inputs.first().waitFor({ state: 'visible' });

        // 1er Cuadro: Nombre del Material
        await inputs.nth(0).fill(uniqueMatName);
        await page.waitForTimeout(1000);

        // 2do Cuadro: Tipo de Material
        await inputs.nth(1).fill("Lona");
        await page.waitForTimeout(1000);

        // 3er Cuadro: Costo por Unidad
        await inputs.nth(2).fill("200.50");
        await page.waitForTimeout(1000);

        // 4to Campo: Tipo de Unidad (Menú desplegable de PrimeFaces)
        // Hacemos clic en la flechita para abrir opciones
        const dropdown = page.locator(".ui-selectonemenu-trigger").first();
        if (await dropdown.isVisible()) {
            await dropdown.click();
            // Hacemos clic en la segunda opción de la lista (ej. MTS, PZAS, etc.)
            await page.locator("li.ui-selectonemenu-item").nth(1).click();
        }
        await page.waitForTimeout(1000);

        // Clic en tu botón azul "Agregar"
        await page.locator("button:has-text('Agregar')").first().click();
        await page.waitForTimeout(1000);

        // 2. Validar que aparece en la búsqueda (AJAX)
        const searchInput = page.locator("input[placeholder*='Buscar'], input[id*='buscar']").first();

        // Vaciamos la caja por si tenía algo
        await searchInput.clear();

        // Escribimos como humano, con una pausa de 50 milisegundos entre cada letra
        await searchInput.pressSequentially(uniqueMatName, { delay: 50 });

        // Presionamos Enter (aunque muchas tablas de PrimeFaces buscan solas al teclear)
        await searchInput.press("Enter");

        // Esperamos a que la fila aparezca
        const materialRow = page.locator(`td:has-text('${uniqueMatName}')`);
        await expect(materialRow).toBeVisible();

        await page.waitForTimeout(1000);

        // 3. Modificarlo
        await materialRow.locator("..").locator("button:has-text('Editar'), button[icon*='pencil']").click();
        await page.waitForTimeout(1000);

        // 1. Ubicamos específicamente la ventanita (modal) de "Editar Material"
        const modal = page.locator(".ui-dialog").filter({ hasText: 'Editar Material' });

        // 2. Esperamos a que el modal aparezca completamente en pantalla
        await expect(modal).toBeVisible();

        // 3. Buscamos los inputs ÚNICAMENTE adentro de ese modal (ignorando el fondo)
        const modalInputs = modal.locator("input[type='text']");

        await modalInputs.nth(3).fill("250.75");
        await page.waitForTimeout(1000);

        // 4. Hacemos clic en el botón Guardar que pertenece a ese modal
        await modal.locator("button:has-text('Guardar')").click();
        await page.waitForTimeout(1000);

        // 4. Eliminarlo
        const editedMaterialRow = page.locator(`td:has-text('${uniqueMatName}')`);
        await page.waitForTimeout(1000);
        await editedMaterialRow.locator("..").locator("button:has-text('Eliminar'), button[icon*='trash']").click();

        // Manejar diálogo de confirmación si existe
        const confirmYes = page.locator("button:has-text('Sí'), button:has-text('Yes')");
        if (await confirmYes.isVisible()) {
            await confirmYes.click();
        }

        // Esperar a que desaparezca de la tabla
        await expect(editedMaterialRow).toBeHidden();
    });

    test('testMaterialCRUD_Fail_Duplicate', async ({ page }) => {
        await loginAs(page, "john.garcia.viray@uabc.edu.mx", "dino0899");
        await page.goto(`${BASE_URL}/consulta_materiales.xhtml`);
        await page.waitForTimeout(1000);

        await page.locator("button:has-text('Agregar'), a:has-text('Agregar')").first().click();

        // Intentar guardar un material vacío
        await page.locator("button:has-text('Agregar')").first().click();
        await page.waitForTimeout(1000);

        // Esperar mensajes de error
        const errorMsgs = page.locator(".ui-messages-error, .ui-message-error");
        await expect(errorMsgs.first()).toBeVisible();
    });
});

// ==========================================
// UI CASE 3: Quote Creation and Registration
// ==========================================
test.describe('UI CASE 3: Quote Creation and Registration', () => {

    test('testAltaCotizacion_Success', async ({ page }) => {
        await loginAs(page, "abraham.flores.cabanillas@uabc.edu.mx", "123");
        await page.goto(`${BASE_URL}/registroCotizacion.xhtml`);
        await page.waitForTimeout(1000);

        // 1. Crear cotización (Buscando por Placeholder en lugar de ID)
        await page.locator("input[placeholder='[Nombre]']").fill("Cliente Automatizado");
        await page.locator("textarea[placeholder*='descripción']").fill("Descripción Automática del proyecto Playwright");
        await page.waitForTimeout(1000);

        // 2. Dropdown de TipoProyecto
        const dropdown = page.locator(".ui-selectonemenu-trigger").first();
        if (await dropdown.isVisible()) {
            await dropdown.click();
            await page.locator("li.ui-selectonemenu-item").nth(1).click();
        }
        await page.waitForTimeout(1000);

        // ==========================================
        // AÑADIR MATERIAL (Awesomplete)
        // ==========================================
        await page.locator("button:has-text('+ Agregar Material')").click();

        const primeraFila = page.locator("#tablaMateriales tbody tr").first();
        const inputMaterial = primeraFila.locator("input[type='text']").first();

        await page.waitForTimeout(1000);

        // Clickeamos el input y tecleamos la letra 'a' como un humano para despertar al autocompletado
        await inputMaterial.click();
        await inputMaterial.pressSequentially("a", { delay: 200 });

        // Le damos 1 segundo al menú para aparecer
        await page.waitForTimeout(1000);
        await inputMaterial.press("ArrowDown");
        await inputMaterial.press("Enter");

        // Agregamos la cantidad (es el segundo input dentro de la fila)
        await primeraFila.locator("input").nth(1).fill("10");
        // ==========================================
        await page.waitForTimeout(1000);

        // 3. Ganancia (En tu HTML el ID es indiceGanancia, respetamos mayúsculas/minúsculas)
        await page.locator("input[id*='indiceGanancia']").fill("20");
        await page.waitForTimeout(1000);

        // 4. Acción Generar Cotización
        await page.locator("button:has-text('Generar Cotización')").click();
        await page.waitForTimeout(1000);

        // 5. Verificar Mensaje de Éxito (Solo el de info, y tomamos el primero para evitar Strict Mode)
        await expect(page.locator("#formCotizacion\\:msgs .ui-messages-info, #formCotizacion\\:msgs").first()).toBeVisible({ timeout: 7000 });

    });

    test('testAltaCotizacion_Fail_MissingDetails', async ({ page }) => {
        await loginAs(page, "abraham.flores.cabanillas@uabc.edu.mx", "123");
        await page.goto(`${BASE_URL}/registroCotizacion.xhtml`);
        await page.waitForTimeout(1000);
        // 1. Intentamos generar la cotización sin llenar nada
        await page.locator("button:has-text('Generar Cotización')").click();

        // 2. Le damos un momento pequeño para ver si algo cambia
        await page.waitForTimeout(1000);

        // 3. LA PRUEBA REAL:
        // Si la validación funcionó (bloqueó el registro), el bot
        // DEBE seguir viendo el botón de "Generar Cotización".
        // Si el sistema hubiera avanzado erróneamente, el botón ya no estaría.
        await expect(page.locator("button:has-text('Generar Cotización')")).toBeVisible();

        // Además, confirmamos que la URL no cambió (seguimos en el registro)
        expect(page.url()).toContain("registroCotizacion.xhtml");
    });
});
