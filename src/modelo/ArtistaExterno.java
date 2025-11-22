package modelo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representa un artista externo que puede ser contratado.
 * Mantiene roles adquiridos por entrenamiento además del historial de bandas.
 * El costo final tiene en cuenta entrenamientos y descuento por compartir banda con base.
 */
public class ArtistaExterno extends ArtistaBase {
    private Set<Rol> rolesAdquiridos;
    public static final double INCREMENTO_ENTRENAMIENTO = 1.5;
    public static final double DESCUENTO_BANDA = 0.5;

    public ArtistaExterno(String nombre, double costoBase, int maxCanciones) {
        super(nombre, costoBase, maxCanciones);
        this.rolesAdquiridos = new HashSet<>();
    }

    public ArtistaExterno(ArtistaBase base) {
        super(base.getNombre(), base.getCostoBase(), base.getMaxCanciones());
        this.rolesAdquiridos = new HashSet<>();
        this.historialBandas.addAll(base.historialBandas);
    }

    public Set<Rol> getRolesAdquiridos() {
        return rolesAdquiridos;
    }

    /**
     * Añade un rol entrenado. No modifica costoBase aquí; el cálculo del
     * incremento se hace en getCostoFinal para evitar duplicaciones.
     */
    public void entrenar(Rol rol) {
        if(rol == null)
            throw new IllegalArgumentException("El rol a entrenar no puede ser nulo!");

        rolesAdquiridos.add(rol);
        this.costoBase = this.costoBase * INCREMENTO_ENTRENAMIENTO;
    }

    public boolean esEntrenable() {
        return true;
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
    public String toString() {
        String costoFormateado = String.format("$%.2f", this.getCostoBase());

        Set<Rol> rolesHistoricos = super.getRolesHistoricos();

        String rolesHistoricosStr = rolesHistoricos.stream()
                .map(Rol::getNombre)
                .collect(Collectors.joining(", "));

        String rolesAdquiridosStr = this.rolesAdquiridos.stream()
                .map(Rol::getNombre)
                .collect(Collectors.joining(", "));

        return "⭐| Nombre: " + super.getNombre() +
                " | Costo Actual: " + costoFormateado +
                " | Máx Canciones: " + super.getMaxCanciones() +
                " | Roles Históricos: " + rolesHistoricosStr +
                " | Roles Entrenados: " + rolesAdquiridosStr +
                " (" + this.rolesAdquiridos + ")";
    }
}
