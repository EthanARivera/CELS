package ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.reflect.TypeToken;

import helper.CotizacionHelper;
import helper.MaterialHelper;

import imf.cels.dto.CotizacionManoObraDTO;
import imf.cels.dto.CotizacionMaterialDTO;
import imf.cels.dto.MaterialDTO;
import imf.cels.entity.*;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Named("altaCotizacionUI")
@ViewScoped
public class AltaCotizacionBeanUI implements Serializable {

    // Dependencias

    @Inject
    private LoginBeanUI loginUI;

    private CotizacionHelper cotizacionHelper = new CotizacionHelper();
    private MaterialHelper materialHelper = new MaterialHelper();

    // Gson con adaptador para LocalDate
    private Gson gson;

    // Datos principales
    private Cotizacion cotizacion;

    // Variables para PBI-CO-US13
    private boolean modoActualizar = false;
    private Integer idCotizacionOriginal;
    private Cotizacion cotizacionOriginal;

    private Usuario usuarioActivo;
    private TipoProyecto tipoProyecto;
    private TipoProyecto[] tiposProyecto = TipoProyecto.values();

    // JSON para comunicación con la vista
    private String jsonMateriales;
    private String jsonTablaMateriales;
    private String jsonTablaManoObra;

    private List<CotizacionMaterial> listaMateriales;
    private List<Material> materialesSeleccionados;

    private List<CotizacionManoDeObra> listaManoDeObra;

    // Totales
    private BigDecimal totalMateriales = BigDecimal.ZERO;
    private BigDecimal totalManoDeObra = BigDecimal.ZERO;
    private BigDecimal costoBruto = BigDecimal.ZERO;
    private BigDecimal precioFinal = BigDecimal.ZERO;

    private BigDecimal gananciaPercent = BigDecimal.ZERO;

    // Para autocompletar
    private String manoObraSeleccionada;

    // INIT
    // INIT ORIGINAL

  /*  @PostConstruct
    public void init() {

        // Construimos el Gson personalizado
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        try {
            loginUI.verificarSesion();
            usuarioActivo = loginUI.getUsuario();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Usuario activo: " + usuarioActivo);

        cotizacion = new Cotizacion();
        cotizacion.setFecha(LocalDate.now());
        cotizacion.setIdUsuario(usuarioActivo);
        cotizacion.setCotizacionMateriales(new LinkedHashSet<>());
        cotizacion.setCotizacionManoDeObras(new LinkedHashSet<>());

        listaMateriales = new ArrayList<>();
        materialesSeleccionados = new ArrayList<>();
        listaManoDeObra = new ArrayList<>();

        cargarCatalogoMateriales();
        jsonTablaMateriales = "[]";
        jsonTablaManoObra = "[]";
    }*/

    //Init
    @PostConstruct
    public void init() {

        // Construimos el Gson personalizado
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        try {
            loginUI.verificarSesion();
            usuarioActivo = loginUI.getUsuario();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Usuario activo: " + usuarioActivo);

        //      DETECTAR SI VENIMOS DE "Actualizar"
        try {
            String idParam = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestParameterMap()
                    .get("idCotizacion");

            if (idParam != null && !idParam.trim().isEmpty()) {

                modoActualizar = true;
                idCotizacionOriginal = Integer.parseInt(idParam);

                // Traer cotización original desde DB
                cotizacionOriginal = cotizacionHelper.obtenerCotizacionPorId(idCotizacionOriginal);

                // Crear una cotización NUEVA
                cotizacion = new Cotizacion();
                cotizacion.setFecha(LocalDate.now());
                cotizacion.setIdUsuario(usuarioActivo);

                listaMateriales = new ArrayList<>();
                listaManoDeObra = new ArrayList<>();
                materialesSeleccionados = new ArrayList<>();

                cargarCatalogoMateriales();

                // Rellenar campos
                cargarDatosEnPantalla(cotizacionOriginal);

                return; // IMPORTANTE (no ejecuta el init normal)
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            modoActualizar = false;
        }


        //      FLUJO NORMAL (NUEVA COTIZACIÓN)
        cotizacion = new Cotizacion();
        cotizacion.setFecha(LocalDate.now());
        cotizacion.setIdUsuario(usuarioActivo);
        cotizacion.setCotizacionMateriales(new LinkedHashSet<>());
        cotizacion.setCotizacionManoDeObras(new LinkedHashSet<>());

        listaMateriales = new ArrayList<>();
        materialesSeleccionados = new ArrayList<>();
        listaManoDeObra = new ArrayList<>();

        cargarCatalogoMateriales();
        jsonTablaMateriales = "[]";
        jsonTablaManoObra = "[]";
    }


    // CARGA DE NOMBRE DE USUARIO EN SESION Y FECHA

    public String obtenerNombreUsuarioEnSesion() {
        return loginUI.obtenerNombreUsuarioEnSesion();
    }

    public LocalDate obtenerFechaDeCotizacion() {
        return LocalDate.now();
    }

    // CARGA CATÁLOGO

    private void cargarCatalogoMateriales() {

        List<Material> materiales = materialHelper.obtenerMateriales();  // tu método actual

        List<MaterialDTO> dtoList = materiales.stream()
                .map(m -> new MaterialDTO(
                        m.getId(),
                        m.getNombre(),
                        m.getCosto()
                ))
                .collect(Collectors.toList());

        Gson gson = new Gson();
        this.jsonMateriales = gson.toJson(dtoList);
    }


    // RECONSTRUIR DESDE JSON

    public void reconstruirDesdeJsonTablaMateriales() {
        try {
            System.out.println("JSON Tabla Materiales (bean): " + jsonTablaMateriales);

            Type type = new TypeToken<List<MaterialFila>>() {
            }.getType();
            List<MaterialFila> filas = gson.fromJson(jsonTablaMateriales, type);

            listaMateriales.clear();
            materialesSeleccionados.clear();  // Necesario para Awesomplete

            for (MaterialFila f : filas) {

                Material mat = materialHelper.buscarPorId(f.getIdMaterial());

                // Armar cotizacionMaterial
                CotizacionMaterial cm = new CotizacionMaterial();
                CotizacionMaterialId id = new CotizacionMaterialId();

                id.setIdMaterial(mat.getId());
                cm.setId(id);
                cm.setIdMaterial(mat);
                cm.setIdFolio(cotizacion);
                cm.setCantidad(f.getCantidad());
                cm.setSubtotal(f.getSubtotal() != null ? f.getSubtotal() : BigDecimal.ZERO);

                listaMateriales.add(cm);

                // NECESARIO PARA EL AUTOCOMPLETE
                materialesSeleccionados.add(mat);
            }

            recalcularTotales();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reconstruirDesdeJsonTablaManoObra() {
        try {
            if (jsonTablaManoObra == null || jsonTablaManoObra.trim().isEmpty()) {
                listaManoDeObra.clear();
                totalManoDeObra = BigDecimal.ZERO;
                return;
            }

            System.out.println("JSON Tabla ManoObra (bean): " + jsonTablaManoObra);

            Type listType = new TypeToken<List<CotizacionManoObraDTO>>() {
            }.getType();
            List<CotizacionManoObraDTO> dtoList = gson.fromJson(jsonTablaManoObra, listType);

            listaManoDeObra.clear();
            totalManoDeObra = BigDecimal.ZERO;

            for (CotizacionManoObraDTO dto : dtoList) {

                // Crear entidad
                CotizacionManoDeObra mdo = new CotizacionManoDeObra();
                CotizacionManoDeObraId id = new CotizacionManoDeObraId();

                // ← clave primaria correcta según BD
                id.setNumResponsable(dto.getNumResponsable());

                mdo.setId(id);
                mdo.setCostoHora(dto.getCostoHora());
                mdo.setCantidadHoras(dto.getCantidadHoras());
                mdo.setSubtotal(dto.getSubtotal());

                // asociación con la entidad padre (cotizacion)
                mdo.setIdFolio(cotizacion);

                listaManoDeObra.add(mdo);

                // suma total mano de obra
                totalManoDeObra = totalManoDeObra.add(
                        dto.getSubtotal() != null ? dto.getSubtotal() : BigDecimal.ZERO
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ACTUALIZACION DE FRONTEND
    public void actualizarManoDeObraDesdeFrontend() {
        reconstruirDesdeJsonTablaManoObra();
        recalcularTotales();
    }

    public void actualizarMaterialesDesdeFrontend() {
        reconstruirDesdeJsonTablaMateriales();
        recalcularTotales();
    }

    // RECÁLCULOS


    public void recalcularTotales() {
        totalMateriales = BigDecimal.ZERO;
        totalManoDeObra = BigDecimal.ZERO;

        for (CotizacionMaterial cm : listaMateriales) {
            if (cm.getSubtotal() != null)
                totalMateriales = totalMateriales.add(cm.getSubtotal());
        }

        for (CotizacionManoDeObra cmdo : listaManoDeObra) {
            if (cmdo.getSubtotal() != null)
                totalManoDeObra = totalManoDeObra.add(cmdo.getSubtotal());
        }

        costoBruto = totalMateriales.add(totalManoDeObra);

        aplicarGanancia();
    }

    public void aplicarGanancia() {
        if (gananciaPercent == null)
            gananciaPercent = BigDecimal.ZERO;

        BigDecimal multiplicador = gananciaPercent
                .divide(BigDecimal.valueOf(100))
                .add(BigDecimal.ONE);

        precioFinal = costoBruto.multiply(multiplicador);
    }


    // REGISTRAR COTIZACIÓN


    public void registrarCotizacion() {

        // Detecta si es actualización PBI-CO-US13
        if (modoActualizar) {
            registrarCotizacionComoNuevaVersion();
            return;
        }

        FacesContext ctx = FacesContext.getCurrentInstance();

        try {

            if (cotizacion.getCliente() == null || cotizacion.getCliente().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre del cliente es obligatorio"));
                throw new IllegalArgumentException("Debe indicar el nombre del cliente.");
            }

            if (cotizacion.getDescripcion() == null || cotizacion.getDescripcion().trim().isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar la descripción del proyecto.");
            }


            if (tipoProyecto == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de proyecto.");
            }

            BigDecimal cero = BigDecimal.ZERO;

            if (totalMateriales.compareTo(cero) < 0 || totalManoDeObra.compareTo(cero) < 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Hay valores inválidos en la cotización"));
                return;
            }

            // 1. Reconstruir desde JSON
            reconstruirDesdeJsonTablaMateriales();
            reconstruirDesdeJsonTablaManoObra();

            // 2. Guardar cotización sola primero → generar id_folio
            aplicarGanancia();
            cotizacion.setPrecioFinal(precioFinal);
            cotizacion.setTipoProyecto(tipoProyecto);

            cotizacionHelper.saveCotizacion(cotizacion);
            Integer idFolio = cotizacion.getId();

            System.out.println("FOLIO GENERADO = " + idFolio);

            // 3. Inicializar correctamente PK de MATERIAL
            for (CotizacionMaterial cm : listaMateriales) {

                CotizacionMaterialId pk = new CotizacionMaterialId();
                pk.setIdFolio(idFolio);
                pk.setIdMaterial(cm.getIdMaterial().getId());   // MUY IMPORTANTE

                cm.setId(pk);
                cm.setIdFolio(cotizacion);

                cotizacionHelper.saveCotizacionMaterial(cm);
            }

            // 4. Inicializar correctamente PK de MANO DE OBRA
            for (CotizacionManoDeObra mo : listaManoDeObra) {

                CotizacionManoDeObraId pk = new CotizacionManoDeObraId();
                pk.setIdFolio(idFolio);
                pk.setNumResponsable(mo.getId().getNumResponsable());

                mo.setId(pk);
                mo.setIdFolio(cotizacion);

                cotizacionHelper.saveCotizacionManoDeObra(mo);
            }

            // 5. Actualizar sets en cotización (opcional pero recomendado)
            cotizacion.setCotizacionMateriales(new LinkedHashSet<>(listaMateriales));
            cotizacion.setCotizacionManoDeObras(new LinkedHashSet<>(listaManoDeObra));

            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Cotización creada",
                    "La cotización se registró correctamente."
            ));

        } catch (Exception e) {
            e.printStackTrace();

            FacesContext.getCurrentInstance().validationFailed(); //Evita borrar campos

            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "No se pudo completar el registro."
            ));
        }
    }

    // DTO PARA JSON


    @Data
    public static class MaterialFila {
        private Integer idMaterial;
        private BigDecimal cantidad;
        private BigDecimal costo;
        private BigDecimal subtotal;
    }


    // AUTOCOMPLETE CON FILTRO


    public List<Material> buscarMateriales(String nombre) {

        System.out.println("BUSCANDO: '" + nombre + "'");

        List<Material> todos = materialHelper.buscarPorNombreCot(nombre);
        if (todos == null) todos = new ArrayList<>();

        List<Integer> idsUsados = new ArrayList<>();
        for (CotizacionMaterial cm : listaMateriales) {
            if (cm.getIdMaterial() != null)
                idsUsados.add(cm.getIdMaterial().getId());
        }

        List<Material> filtrados = new ArrayList<>();
        for (Material m : todos) {
            if (!idsUsados.contains(m.getId()))
                filtrados.add(m);
        }

        return filtrados;
    }


    // RESPONSE JSON DIRECTO


    public void buscar() throws IOException {

        String texto = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("buscar");

        List<Material> resultados = materialHelper.buscarPorNombre(texto);

        String json = gson.toJson(resultados);

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response =
                (HttpServletResponse) context.getExternalContext().getResponse();

        response.setContentType("application/json");
        response.getWriter().write(json);
        response.getWriter().flush();
        context.responseComplete();
    }


    // ADAPTADOR PARA LOCALDATE


    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(JsonWriter writer, LocalDate date) throws IOException {
            writer.value(date.toString());
        }

        @Override
        public LocalDate read(JsonReader reader) throws IOException {
            return LocalDate.parse(reader.nextString());
        }
    }

    //  AQUÍ COMIENZAN LAS FUNCIONES PARA PBI-CO-US13
    private void cargarDatosEnPantalla(Cotizacion c) {
        try {
            // Cmpos simples
            cotizacion.setCliente(c.getCliente());
            cotizacion.setDescripcion(c.getDescripcion());
            cotizacion.setFecha(c.getFecha());
            tipoProyecto = c.getTipoProyecto();

            // Materiales
            listaMateriales = new ArrayList<>();
            List<MaterialFila> filasMat = new ArrayList<>();

            for (CotizacionMaterial cm : c.getCotizacionMateriales()) {

                listaMateriales.add(cm); // para backend

                MaterialFila fila = new MaterialFila();
                fila.setIdMaterial(cm.getIdMaterial().getId());
                fila.setCantidad(cm.getCantidad());
                fila.setCosto(cm.getIdMaterial().getCosto());
                fila.setSubtotal(cm.getSubtotal());

                filasMat.add(fila);
            }

            jsonTablaMateriales = gson.toJson(filasMat);


            // Mano de obra
            listaManoDeObra = new ArrayList<>();
            List<CotizacionManoObraDTO> filasMo = new ArrayList<>();

            for (CotizacionManoDeObra mo : c.getCotizacionManoDeObras()) {

                listaManoDeObra.add(mo); // para backend

                CotizacionManoObraDTO dto = new CotizacionManoObraDTO();
                dto.setNumResponsable(mo.getId().getNumResponsable());
                dto.setCostoHora(mo.getCostoHora());
                dto.setCantidadHoras(mo.getCantidadHoras());
                dto.setSubtotal(mo.getSubtotal());

                filasMo.add(dto);
            }

            jsonTablaManoObra = gson.toJson(filasMo);


            // Recalculo de totales
            recalcularTotales();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Para registrar la nueva versión
    private void registrarCotizacionComoNuevaVersion() {

        FacesContext ctx = FacesContext.getCurrentInstance();

        try { //Valida si hay cambios
            // Si el metodo devuelve false (no hay cambios), mostramos aviso y nos salimos.
            if (!hayCambios()) {
                ctx.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Sin Cambios",
                        "No ha realizado ninguna modificación respecto a la cotización original. No se creó una nueva versión."
                ));
                return;
            }

            reconstruirDesdeJsonTablaMateriales();
            reconstruirDesdeJsonTablaManoObra();

            aplicarGanancia();

            Cotizacion nueva = new Cotizacion();
            nueva.setCliente(cotizacion.getCliente());
            nueva.setDescripcion(cotizacion.getDescripcion());
            nueva.setFecha(LocalDate.now());
            nueva.setIdUsuario(usuarioActivo);
            nueva.setTipoProyecto(tipoProyecto);
            nueva.setPrecioFinal(precioFinal);

            cotizacionHelper.saveCotizacion(nueva);

            Integer idFolio = nueva.getId();

            // Guardar materiales
            for (CotizacionMaterial cmViejito : listaMateriales) {

                // Creamos un objeto 100% NUEVO
                CotizacionMaterial cmNuevo = new CotizacionMaterial();

                // Creamos su ID
                CotizacionMaterialId pk = new CotizacionMaterialId();
                pk.setIdFolio(idFolio);
                pk.setIdMaterial(cmViejito.getIdMaterial().getId());

                // Llenamos el objeto nuevo con los datos del viejito
                cmNuevo.setId(pk);
                cmNuevo.setIdMaterial(cmViejito.getIdMaterial());
                cmNuevo.setCantidad(cmViejito.getCantidad());
                cmNuevo.setSubtotal(cmViejito.getSubtotal());
                cmNuevo.setIdFolio(nueva); // Lo amarramos a la cotización nueva

                cotizacionHelper.saveCotizacionMaterial(cmNuevo);
            }

            // Guardar mano de obra
            for (CotizacionManoDeObra moViejito : listaManoDeObra) {

                // Creamos un objeto 100% NUEVO
                CotizacionManoDeObra moNuevo = new CotizacionManoDeObra();

                // Creamos su ID
                CotizacionManoDeObraId pk = new CotizacionManoDeObraId();
                pk.setIdFolio(idFolio);

                // Numero de responsable
                int numResp = (moViejito.getId() != null) ? moViejito.getId().getNumResponsable() : 0;
                pk.setNumResponsable(numResp);

                // Llenamos el objeto nuevo con los datos del viejito
                moNuevo.setId(pk);
                moNuevo.setCostoHora(moViejito.getCostoHora());
                moNuevo.setCantidadHoras(moViejito.getCantidadHoras());
                moNuevo.setSubtotal(moViejito.getSubtotal());
                moNuevo.setIdFolio(nueva); // Lo amarramos a la cotización nueva

                cotizacionHelper.saveCotizacionManoDeObra(moNuevo);
            }

            //Para que no deje guaradar una actualizacion sin cambios en la misma vista
            //Actualizamos la vista con la cotización nueva (para que tenga el ID nuevo)
            this.cotizacion = nueva;

            // Actualizamos la referencia "Original"
            // Ahora la "Original" es esta nueva que acabamos de crear.
            // Si das clic otra vez en guardar, el sistema comparará "Nueva vs Nueva"
            this.idCotizacionOriginal = nueva.getId();

            this.cotizacionOriginal = cotizacionHelper.obtenerCotizacionPorId(this.idCotizacionOriginal);

            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Actualización exitosa",
                    "Se creó una nueva versión de la cotización con el folio " + idFolio
            ));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "No se pudo crear la nueva versión."
            ));
        }
    }

    // --- METODO PARA DETECTAR SI HUBO CAMBIOS REALES ---
    private boolean hayCambios() {
        // IMPORTANTE: Actualizamos los datos de memoria con lo que hay en pantalla (JSONs)
        reconstruirDesdeJsonTablaMateriales();
        reconstruirDesdeJsonTablaManoObra();
        aplicarGanancia(); // Recalcula el precioFinal basado en los nuevos datos

        // Comparar Encabezados (Cliente, Descripcion, Tipo)
        // Usamos !equals para ver si son DIFERENTES.
        if (!cotizacion.getCliente().trim().equals(cotizacionOriginal.getCliente().trim())) return true;
        if (!cotizacion.getDescripcion().trim().equals(cotizacionOriginal.getDescripcion().trim())) return true;
        if (tipoProyecto != cotizacionOriginal.getTipoProyecto()) return true;

        // Comparar Totales (Si cambió el precio final, algo cambió)
        // Usamos compareTo != 0 porque BigDecimal no siempre funciona bien con equals()
        if (precioFinal.compareTo(cotizacionOriginal.getPrecioFinal()) != 0) return true;

        // Comparar Cantidad de elementos (Si antes había 3 materiales y ahora hay 2)
        if (listaMateriales.size() != cotizacionOriginal.getCotizacionMateriales().size()) return true;
        if (listaManoDeObra.size() != cotizacionOriginal.getCotizacionManoDeObras().size()) return true;

        // Comparación profunda de Materiales (Contenido)
        // Verificamos si para cada material nuevo, existe uno idéntico en la original
        for (CotizacionMaterial nuevo : listaMateriales) {
            boolean matchEncontrado = false;

            for (CotizacionMaterial original : cotizacionOriginal.getCotizacionMateriales()) {
                // Comparamos IDs de material
                if (nuevo.getIdMaterial().getId().equals(original.getIdMaterial().getId())) {
                    // Si es el mismo material, verificamos la cantidad
                    if (nuevo.getCantidad().compareTo(original.getCantidad()) == 0) {
                        matchEncontrado = true;
                    }
                    break; // Ya encontramos el material, dejamos de buscar en la original
                }
            }
            // Si recorrimos toda la lista original y no encontramos un match exacto quiere decir que hubo cambio
            if (!matchEncontrado) return true;
        }

        // Comparación profunda de Mano de Obra
        for (CotizacionManoDeObra nuevo : listaManoDeObra) {
            boolean matchEncontrado = false;

            for (CotizacionManoDeObra original : cotizacionOriginal.getCotizacionManoDeObras()) {
                // Aquí comparamos Costo y Horas (ya que los IDs de fila pueden variar o regenerarse)
                // Si el costo O las horas son diferentes, no es el mismo registro.
                if (nuevo.getCostoHora().compareTo(original.getCostoHora()) == 0 &&
                        nuevo.getCantidadHoras().compareTo(original.getCantidadHoras()) == 0) {
                    matchEncontrado = true;
                    break;
                }
            }
            if (!matchEncontrado) return true;
        }

        // Si pasó todas las pruebas y no retornó true, significa que es IDÉNTICA.
        return false;
    }



    // GETTERS Y SETTERS


    public String getJsonMateriales() {
        return jsonMateriales;
    }

    public void setJsonMateriales(String jsonMateriales) {
        this.jsonMateriales = jsonMateriales;
    }

    public String getJsonTablaMateriales() {
        return jsonTablaMateriales;
    }

    public void setJsonTablaMateriales(String jsonTablaMateriales) {
        this.jsonTablaMateriales = jsonTablaMateriales;
    }

    public String getJsonTablaManoObra() {
        return jsonTablaManoObra;
    }

    public void setJsonTablaManoObra(String jsonTablaManoObra) {
        this.jsonTablaManoObra = jsonTablaManoObra;
    }

    public BigDecimal getTotalMateriales() {
        return totalMateriales;
    }

    public BigDecimal getTotalManoDeObra() {
        return totalManoDeObra;
    }

    public BigDecimal getCostoBruto() {
        return costoBruto;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public BigDecimal getGananciaPercent() {
        return gananciaPercent;
    }

    public void setGananciaPercent(BigDecimal gananciaPercent) {
        this.gananciaPercent = gananciaPercent;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public TipoProyecto[] getTiposProyecto() {
        return tiposProyecto;
    }

    public void setTiposProyecto(TipoProyecto[] tiposProyecto) {
        this.tiposProyecto = tiposProyecto;
    }
}