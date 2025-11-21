package modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa un artista genérico del dominio.
 * - Puede ser un artista "base" (pertenece a la discográfica) o un artista externo
 *   (subclase ArtistaExterno).
 * - Mantiene un historial de colaboraciones (bandas y roles que ocupó).
 */
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
    
    public String getNombre() {
        return nombre;
    }
    public int getMaxCanciones() {
        return this.maxCanciones;
    }
    public double getCostoBase() {
        return costoBase;
    }
    
    /**
     * Devuelve el conjunto de roles históricos 
     * ocupados por el artista
     */
    public Set<Rol> getRolesHistoricos() {
        Set<Rol> roles = new HashSet<>();
        for(Colaboracion colaboracion : this.historialBandas) {
            roles.addAll(colaboracion.getRolesOcupados());
        }
        return roles;
    }

    /**
     * Indica si el artista pudo ocupar históricamente el rol pasado.
     * Busca en todas sus colaboraciones.
     */
    public boolean puedeCubrir(Rol rol) {
        for(Colaboracion c : historialBandas){
            if(c.getRolesOcupados().contains(rol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si comparte al menos una banda 
     * con otro artista (para descuentos)
     */
    public boolean comparteBanda(ArtistaBase otro) {
        for(Colaboracion c1 : this.historialBandas) {
            for(Colaboracion c2 : otro.historialBandas) {
                if(c1.getBanda().equals(c2.getBanda()))
                    return true;
            }
        }
        return false;
    }

    public boolean esEntrenable() {
        return false;
    }
    
    /** Añade una colaboración (historial) */
    public void agregarColaboracion(Colaboracion colaboracion) {
        this.historialBandas.add(colaboracion);
    }

    @Override
    public String toString() {
        String costoFormateado = String.format("$%.2f", this.costoBase);

        return "🎤| Nombre: " + this.nombre +
                " | Costo: " + costoFormateado +
                " | Máx Canciones: " + this.maxCanciones +
                " | Roles Históricos: " + this.getRolesHistoricos().size() + " en total";
    }
}
