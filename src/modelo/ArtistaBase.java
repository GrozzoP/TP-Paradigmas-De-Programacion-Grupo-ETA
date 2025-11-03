package modelo;

import java.util.Set;

public class ArtistaBase {
    private String nombre;
    protected Set<Colaboracion> historialBandas;
    protected double costoBase;
    protected int maxCanciones;

    public ArtistaBase(String nombre, double costoBase, int maxCanciones) {
        this.nombre = nombre;
        this.costoBase = costoBase;
        this.maxCanciones = maxCanciones;
    }

    public boolean puedeCubrir(Rol rol) {
        return false;
    }

    public boolean comparteBanda(ArtistaBase otro) {
        return false;
    }

    public double getCostoFinal(Set<ArtistaBase> artistasBase) {
        return 0.0;
    }

    public double getCostoBase() {
        return costoBase;
    }
}
