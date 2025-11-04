package modelo;

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
    }

    public boolean puedeCubrir(Rol rol) {
        // Verifica si el rol aparece en alguna colaboracion anterior para ver si puede cubrirlo
        for(Colaboracion c : historialBandas){
            if(c.getRolesOcupados().contains(rol)){
                return true;
            }
        }
        return false;
    }

    public boolean comparteBanda(ArtistaBase otro) {
        //Verifica si comparte banda con otro artista
        for(Colaboracion c1 : this.historialBandas){
            for(Colaboracion c2 : otro.historialBandas){
                if(c1.getBanda().getNombre().equalsIgnoreCase(c2.getBanda().getNombre()))
                    return true;
            }
        }
        return false;
    }

    public double getCostoFinal(Set<ArtistaBase> artistasBase) {
        return 0.0;
    }

    public double getCostoBase() {
        return costoBase;
    }
}
