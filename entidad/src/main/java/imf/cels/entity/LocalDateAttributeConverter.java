package imf.cels.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return (localDate != null ? Date.valueOf(localDate) : null);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return (date != null ? date.toLocalDate() : null);
    }
}