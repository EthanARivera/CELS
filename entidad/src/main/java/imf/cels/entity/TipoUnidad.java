package imf.cels.entity;

public enum TipoUnidad {
    lts("lts"),
    m2("m²"),       // Guardará "m2" en BD pero mostrará "m²" al usuario
    kgs("kgs"),
    pzas("pzas"),
    mts("mts"),
    otro("otro");

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
     * Convierte un string (guardado en la BD) al enum correspondiente.
     */
    public static TipoUnidad fromLabel(String label) {
        if (label == null) return null;
        for (TipoUnidad u : values()) {
            // 👇 "m2" de la BD se convierte a TipoUnidad.m2
            if (u.name().equalsIgnoreCase(label) || u.label.equalsIgnoreCase(label)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Unidad desconocida: " + label);
    }
}
