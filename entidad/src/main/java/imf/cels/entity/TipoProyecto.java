package imf.cels.entity;

public enum TipoProyecto {
    CAJA_LUMINOSA("Caja Luminosa"),
    CHANNEL_LETTERS("Channel Letters"),
    LETREROS_LUMINOSOS("Letreros Luminosos"),
    FACHADA("Fachada"),
    ROTULACION_VEHICULAR("Rotulación Vehicular"),
    ROTULACION_COMERCIAL("Rotulación Comercial"),
    IMPRESION_LONA("Impresión de Lona"),
    IMPRESION_VINIL("Impresión de Vinil"),
    OTRO("Otro");

    private final String label;

    TipoProyecto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * Convierte de String de la BD al enum.
     */
    public static TipoProyecto fromLabel(String label) {
        if (label == null) return null;
        label = label.trim().toLowerCase();

        for (TipoProyecto t : values()) {
            String normalized = t.label.trim().toLowerCase();
            if (normalized.equals(label) ||
                    normalized.replace("ó", "o").equals(label.replace("ó", "o"))) {
                return t;
            }
        }
        throw new IllegalArgumentException("TipoProyecto desconocido: " + label);
    }
}