package imf.cels.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TipoUnidadConverter implements AttributeConverter<TipoUnidad, String> {

    @Override
    public String convertToDatabaseColumn(TipoUnidad tipoUnidad) {
        return tipoUnidad != null ? tipoUnidad.getLabel() : null;
    }

    @Override
    public TipoUnidad convertToEntityAttribute(String label) {
        return label != null ? TipoUnidad.fromLabel(label) : null;
    }
}