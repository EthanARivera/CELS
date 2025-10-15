package imf.cels.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte valores entre el tipo enum "TipoUnidad" y su representación
 * de texto utilizada en la base de datos.
 * Este convertidor se aplica manualmente en la entidad "Material"
 * mediante la anotación @Convert(converter = TipoUnidadConverter.class)
 * - Cuando se guarda un material, convierte el enum (Java) en texto (BD).
 * - Cuando se lee un material, convierte el texto (BD) en enum (Java).
 */

@Converter(autoApply = false) //// No se aplica automáticamente a todos los enums, solo cuando se indique explícitamente.
public class TipoUnidadConverter implements AttributeConverter<TipoUnidad, String> {

    /**
     * Convierte un valor del enum "TipoUnidad" a su representación en texto
     * antes de ser almacenado en la base de datos..*/
    @Override
    public String convertToDatabaseColumn(TipoUnidad tipoUnidad) {
        // Si el enum no es nulo, devuelve su etiqueta (label), de lo contrario null.
        return tipoUnidad != null ? tipoUnidad.getLabel() : null;
    }

/**Convierte el valor en texto leido desde la BD
 * correspondiente alor del enum "TipoUnidad"*/
    @Override
    public TipoUnidad convertToEntityAttribute(String label) {
        // Si la cadena no es nula, busca el enum correspondiente mediante fromLabel().
        return label != null ? TipoUnidad.fromLabel(label) : null;
    }
}