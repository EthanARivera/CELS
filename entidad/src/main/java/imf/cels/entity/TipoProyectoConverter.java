package imf.cels.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoProyectoConverter implements AttributeConverter<TipoProyecto, String> {

    @Override
    public String convertToDatabaseColumn(TipoProyecto tipoProyecto) {
        return tipoProyecto != null ? tipoProyecto.getLabel() : null;
    }

    @Override
    public TipoProyecto convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.trim()) {
            case "1": return TipoProyecto.CAJA_LUMINOSA;
            case "2": return TipoProyecto.CHANNEL_LETTERS;
            case "3": return TipoProyecto.LETREROS_LUMINOSOS;
            case "4": return TipoProyecto.FACHADA;
            case "5": return TipoProyecto.ROTULACION_VEHICULAR;
            case "6": return TipoProyecto.ROTULACION_COMERCIAL;
            case "7": return TipoProyecto.IMPRESION_LONA;
            case "8": return TipoProyecto.IMPRESION_VINIL;
            case "9": return TipoProyecto.OTRO;
            default:
                return TipoProyecto.fromLabel(dbData);
        }
    }
}