package modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa un artista externo que puede ser contratado.
 * Mantiene roles adquiridos por entrenamiento además del historial de bandas.
 * El costo final tiene en cuenta entrenamientos y descuento por compartir banda con base.
 */
public class ArtistaExterno extends ArtistaBase {
    private Set<Rol> rolesAdquiridos;

    public ArtistaExterno(String nombre, double costoBase, int maxCanciones) {
        super(nombre, costoBase, maxCanciones);
        this.rolesAdquiridos = new HashSet<>();
    }

    public Set<Rol> getRolesAdquiridos() {
        return rolesAdquiridos;
    }
    
    /**
     * Política de cantidad máxima de entrenamientos permitidos.
     * Valor elegido en el diseño; si lo cambian, actualizar acá.
     */
    public boolean puedeEntrenarse() {
        return rolesAdquiridos.size() < 3;
    }
    
    /**
     * Añade un rol entrenado. No modifica costoBase aquí; el cálculo del
     * incremento se hace en getCostoFinal para evitar duplicaciones.
     */
    public void entrenar(Rol rol) {
        if(puedeEntrenarse()) {
            rolesAdquiridos.add(rol);
        }
    }

    /**
     * Un artista externo cubre un rol si lo hizo históricamente 
     * o lo tiene como rol entrenado.
     */
    @Override
    public boolean puedeCubrir(Rol rol) {
        boolean cubrePorHistoria = super.puedeCubrir(rol);
        boolean cubrePorEntrenamiento = this.rolesAdquiridos.contains(rol);
        return cubrePorEntrenamiento || cubrePorHistoria;
    }

    @Override
    public void agregarSiContratable(Set<ArtistaExterno> contratados) {
        contratados.add(this);
    }

    /**
     * Calcula el costo final por canción:
     * - Aplica incremento por entrenamientos: costoBase * (1.5 ^ nEntrenamientos)
     * - Aplica descuento 50% si comparte banda con al menos un artista base del recital.
     */
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
