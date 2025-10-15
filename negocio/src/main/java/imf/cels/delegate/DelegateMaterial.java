package imf.cels.delegate;

import imf.cels.entity.Material;
import imf.cels.integration.ServiceLocator;

public class DelegateMaterial {

    public void saveMaterial(Material material) {
        ServiceLocator.getInstanceMaterialDAO().save(material);
    }

}
