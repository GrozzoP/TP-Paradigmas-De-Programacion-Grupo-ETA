package modelo;

import java.util.HashSet;
import java.util.Set;

public class ArtistaBase {
    private final String nombre;
    protected Set<Colaboracion> historialBandas;
    protected double costoBase;
    protected int maxCanciones;

    public ArtistaBase(String nombre, double costoBase, int maxCanciones) {
        this.nombre = nombre;
        this.costoBase = costoBase;
        this.maxCanciones = maxCanciones;
        this.historialBandas = new HashSet<>();
    }

    public Set<Rol> getRolesHistoricos() {
        Set<Rol> roles = new HashSet<>();

        for(Colaboracion colaboracion : this.historialBandas) {
            roles.addAll(colaboracion.getRolesOcupados());
        }

        return roles;
    }

    public boolean puedeCubrir(Rol rol) {
        for(Colaboracion c : historialBandas){
            if(c.getRolesOcupados().contains(rol)) {
                return true;
            }
        }
        return false;
    }

    public boolean comparteBanda(ArtistaBase otro) {
        for(Colaboracion c1 : this.historialBandas) {
            for(Colaboracion c2 : otro.historialBandas) {
                if(c1.getBanda().equals(c2.getBanda()))
                    return true;
            }
        }
        return false;
    }

    public void agregarColaboracion(Colaboracion colaboracion) {
        this.historialBandas.add(colaboracion);
    }

    public void agregarSiContratable(Set<ArtistaExterno> contratados) {
        // Como es un artista base, no es contratable, no agrego nada
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaxCanciones() {
        return this.maxCanciones;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public double getCostoFinal(Set<ArtistaBase> artistasBase) {
        return 0;
    }

    @Override
    public String toString() {
        return "ArtistaBase{" + "nombre: " + nombre + '}';
    }
    
}
