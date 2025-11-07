package modelo;

import java.util.HashSet;
import java.util.Set;

public class ArtistaExterno extends ArtistaBase {
    private Set<Rol> rolesAdquiridos;

    public ArtistaExterno(String nombre, double costoBase, int maxCanciones) {
        super(nombre, costoBase, maxCanciones);
        this.rolesAdquiridos = new HashSet<>();
    }

    public Set<Rol> getRolesAdquiridos() {
        return rolesAdquiridos;
    }

    public boolean puedeEntrenarse() {
        return rolesAdquiridos.size() < 3;
    }
    
    /**
     * Abro hilo...
     * Entrena un nuevo rol: sólo agrega el rol a rolesAdquiridos.
     * NO multiplicamos costoBase aca para evitar acumulaciones dobles.
     * La subida de costo se aplica en getCostoFinal() con la fórmula (1.5 ^ n).
     */
    public void entrenar(Rol rol) {
        if(puedeEntrenarse()) {
            rolesAdquiridos.add(rol);
        }
    }

    @Override
    public boolean puedeCubrir(Rol rol) {
        boolean cubrePorHistoria = super.puedeCubrir(rol);

        boolean cubrePorEntrenamiento = this.rolesAdquiridos.contains(rol);

        return cubrePorEntrenamiento || cubrePorHistoria;
    }

    public boolean esContratable() {
        return false;
    }

    @Override
    public double getCostoFinal(Set<ArtistaBase> artistasBase) {
       int cantEntrenamientos = rolesAdquiridos.size();
       double costoConEntrenamientos = costoBase * Math.pow(1.5, cantEntrenamientos);

       boolean comparte = (artistasBase != null) && (artistasBase.stream().anyMatch(this::comparteBanda));
       if(comparte) {
            return costoConEntrenamientos * 0.5;
       } else {
            return costoConEntrenamientos;
       }
    }
    
}
