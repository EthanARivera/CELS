package imf.cels.facade;

import imf.cels.delegate.DelegateMaterial;
import imf.cels.entity.Material;
import java.util.List;

public class FacadeMaterial {
    private final DelegateMaterial delegateMaterial;

    public FacadeMaterial() {
        this.delegateMaterial = new DelegateMaterial();
    }

    public void guardarMaterial(Material material) {
        delegateMaterial.guardarMaterial(material);
    }

    public List<Material> listarMateriales() {
        return delegateMaterial.listarMateriales();
    }
}
