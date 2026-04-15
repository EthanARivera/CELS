package ui;

import helper.CotizacionHelper;
import helper.MaterialHelper;
import helper.UsuarioHelper;
import imf.cels.entity.*;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentMatcher;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CelsUnitTests {

    @Mock
    private FacesContext facesContext;

    private MockedStatic<FacesContext> mockedFacesContext;

    @Mock
    private LoginBeanUI loginUI;

    @Mock
    private CotizacionHelper cotizacionHelper;

    @Mock
    private UsuarioHelper usuarioHelper;

    @InjectMocks
    private AltaCotizacionBeanUI altaCotizacionBeanUI;

    @InjectMocks
    private UsuarioBeanUI usuarioBeanUI;

    @BeforeEach
    public void setUp() {
        mockedFacesContext = Mockito.mockStatic(FacesContext.class);
        mockedFacesContext.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
    }

    @AfterEach
    public void tearDown() {
        mockedFacesContext.close();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Prueba 1: Calculo total de una cotización (AltaCotizacionBeanUI)
     */
    @Test
    public void testAltaCotizacionCalculationSuccess() throws Exception {
        // Escenario exitoso.
        List<CotizacionMaterial> listMateriales = new ArrayList<>();
        CotizacionMaterial cm1 = new CotizacionMaterial();
        cm1.setSubtotal(new BigDecimal("100.00"));
        listMateriales.add(cm1);

        List<CotizacionManoDeObra> listManoObra = new ArrayList<>();
        CotizacionManoDeObra mo1 = new CotizacionManoDeObra();
        mo1.setSubtotal(new BigDecimal("200.00"));
        listManoObra.add(mo1);

        setField(altaCotizacionBeanUI, "listaMateriales", listMateriales);
        setField(altaCotizacionBeanUI, "listaManoDeObra", listManoObra);
        setField(altaCotizacionBeanUI, "gananciaPercent", new BigDecimal("15"));

        altaCotizacionBeanUI.recalcularTotales();

        assertEquals(0, new BigDecimal("300.00").compareTo(altaCotizacionBeanUI.getCostoBruto()));
        assertEquals(0, new BigDecimal("345.00").compareTo(altaCotizacionBeanUI.getPrecioFinal()));
    }

    @Test
    public void testAltaCotizacionCalculationFailure() throws Exception {
        // Escenario B Lista vacía.
        List<CotizacionMaterial> listMateriales = new ArrayList<>();
        CotizacionMaterial cmNull = new CotizacionMaterial();
        cmNull.setSubtotal(null);
        listMateriales.add(cmNull);

        List<CotizacionManoDeObra> listManoObra = new ArrayList<>();

        setField(altaCotizacionBeanUI, "listaMateriales", listMateriales);
        setField(altaCotizacionBeanUI, "listaManoDeObra", listManoObra);
        setField(altaCotizacionBeanUI, "gananciaPercent", new BigDecimal("10"));

        altaCotizacionBeanUI.recalcularTotales();

        assertEquals(0, BigDecimal.ZERO.compareTo(altaCotizacionBeanUI.getCostoBruto()));
        assertEquals(0, BigDecimal.ZERO.compareTo(altaCotizacionBeanUI.getPrecioFinal()));
    }

    /**
     *  Prueba 2: Validación del material (MaterialBeanUI.guardar)
     */
    @Test
    public void testMaterialGuardarSuccess() throws Exception {
        // Escenario A Todos los campos válidos.
        try (MockedConstruction<MaterialHelper> mocked = mockConstruction(MaterialHelper.class)) {
            MaterialBeanUI materialBean = new MaterialBeanUI();

            Material m = new Material();
            m.setNombre("Cemento");
            m.setTipoMaterial("Construccion");
            m.setCosto(new BigDecimal("150.00"));
            m.setTipoUnidad(TipoUnidad.KILOGRAMOS);
            materialBean.setMaterial(m);

            materialBean.guardar();

            MaterialHelper mockHelper = mocked.constructed().get(0);
            verify(mockHelper, times(1)).saveMaterial(eq("Cemento"), eq("Construccion"), eq(new BigDecimal("150.00")), eq(TipoUnidad.KILOGRAMOS));

            verify(facesContext, times(1)).addMessage(eq(null), argThat(msg -> msg.getSeverity() == FacesMessage.SEVERITY_INFO));
        }
    }

    @Test
    public void testMaterialGuardarFailure() throws Exception {
        // Escenario B Campos vacíos.
        try (MockedConstruction<MaterialHelper> mocked = mockConstruction(MaterialHelper.class)) {
            MaterialBeanUI materialBean = new MaterialBeanUI();

            Material m = new Material();
            m.setNombre("");
            m.setTipoMaterial("Construccion");
            m.setCosto(new BigDecimal("-10.00"));
            m.setTipoUnidad(null);
            materialBean.setMaterial(m);

            materialBean.guardar();

            if (!mocked.constructed().isEmpty()) {
                MaterialHelper mockHelper = mocked.constructed().get(0);
                verify(mockHelper, never()).saveMaterial(any(), any(), any(), any());
            }

            verify(facesContext, times(1)).addMessage(eq(null), argThat(msg -> msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
        }
    }

    /**
     * Prueba 3: Cambiar el estado del usuario (UsuarioBeanUI.cambiarEstadoUsuario)
     */
    @Test
    public void testCambiarEstadoUsuarioSuccess() throws Exception {
        // Escenario A Usuario válido.
        Usuario user = new Usuario();
        user.setId(1);
        user.setNombre("Juan");
        user.setEstado(true); // Activo

        usuarioBeanUI.setUsuarioSeleccionado(user);

        setField(usuarioBeanUI, "helper", usuarioHelper);

        when(usuarioHelper.cambiarEstadoUsuario(1, false)).thenReturn(true);

        usuarioBeanUI.cambiarEstadoUsuario();

        assertFalse(user.getEstado()); // Cambio de estado
        verify(facesContext, times(1)).addMessage(eq(null), argThat(msg -> msg.getSeverity() == FacesMessage.SEVERITY_INFO));
    }

    @Test
    public void testCambiarEstadoUsuarioFailure() throws Exception {
        // Escenario B el helper nos regresa un false.
        Usuario user = new Usuario();
        user.setId(2);
        user.setNombre("Pepe");
        user.setEstado(true);
        usuarioBeanUI.setUsuarioSeleccionado(user);

        setField(usuarioBeanUI, "helper", usuarioHelper);
        when(usuarioHelper.cambiarEstadoUsuario(2, false)).thenReturn(false);

        usuarioBeanUI.cambiarEstadoUsuario();

        assertTrue(user.getEstado()); // Estado sin cambiar
        verify(facesContext, times(1)).addMessage(eq(null), argThat(msg -> msg.getSeverity() == FacesMessage.SEVERITY_ERROR));
    }
}