const { test, expect } = require('@playwright/test');

const APP_URL = 'http://localhost:8080/CELS';

test.describe('Pruebas de UI Reales - PDF CELS', () => {

    test('Caso UI 1: Login y Redirección de Rol', async ({ page }) => {
        await page.goto(`${APP_URL}/login.xhtml`);
        await page.fill('input[name*="email"]', 'gerente@cels.com');
        await page.fill('input[name*="psswd"]', 'admin123');
        await page.click('button:has-text("Iniciar sesion")');
        await expect(page).toHaveURL(/.*indexGerente.xhtml/);
    });

    test('Caso UI 2: Gestión de Materiales y AJAX', async ({ page }) => {
        await page.goto(`${APP_URL}/consulta_materiales.xhtml`);
        await page.fill('input[id*="nombreBusqueda"]', 'Lona');
        await page.waitForTimeout(600); 
        const filas = await page.locator('table tbody tr').count();
        expect(filas).toBeGreaterThan(0);
    });

    test('Caso UI 3: Creación de Cotización y Pedido', async ({ page }) => {
        await page.goto(`${APP_URL}/registroCotizacion.xhtml`);
      
        await page.fill('input[id*="cliente"]', 'Cliente Real Test');
        await page.selectOption('select[id*="tipoProyecto"]', 'FACHADA');
        await page.click('button:has-text("Generar Cotización")');
     
        const mensaje = page.locator('.ui-messages-info');
        await expect(mensaje).toBeVisible();
    });
});