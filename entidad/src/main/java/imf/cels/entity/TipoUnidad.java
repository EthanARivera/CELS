package imf.cels.entity;

public enum TipoUnidad {
    LITROS("lts"),
    METROS_CUADRADOS("m²"),
    KILOGRAMOS("kgs"),
    PIEZAS("pzas"),
    METROS("mts"),
    OTRO("otro");

    private final String label;

    TipoUnidad(String label) {
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
     * Convierte un string de la BD al enum correspondiente.
     */
    public static TipoUnidad fromLabel(String label) {
        for (TipoUnidad t : values()) {
            if (t.label.equalsIgnoreCase(label)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unidad desconocida: " + label);
    }
}
