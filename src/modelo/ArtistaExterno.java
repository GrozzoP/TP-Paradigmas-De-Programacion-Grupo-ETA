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

    public void entrenar(Rol rol) {
        if (puedeEntrenarse()) {
            rolesAdquiridos.add(rol);
            //¿Modifico el costo base incrementandolo un 50%? Abro hilo...
            this.costoBase *= 1.5;
        }
    }

    @Override
    public double getCostoFinal(Set<ArtistaBase> artistas) {
        double extra = rolesAdquiridos.size() * 100;
        return getCostoBase() + extra;
    }
}
