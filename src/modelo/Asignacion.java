package modelo;

import java.util.List;
import java.util.Set;

/**
 * Representa la asignación de un artista a un rol en una canción concreta.
 * Es la entidad que relaciona ternariamente Artista - Rol - Cancion.
 */
public class Asignacion {
    private final ArtistaBase artista;
    private final Rol rolAsignado;
    private final Cancion cancion;

    public Asignacion(ArtistaBase artista, Rol rolAsignado, Cancion cancion) {
        this.artista = artista;
        this.rolAsignado = rolAsignado;
        this.cancion = cancion;
    }

    public ArtistaBase getArtista() {
        return artista;
    }
    public Rol getRolAsignado() {
        return rolAsignado;
    }
    public Cancion getCancion() {
        return cancion;
    }

    /**
     * Devuelve el costo efectivo de esta asignación (por canción), 
     * preguntando al artista por su costo final y pasando el 
     * conjunto de artistas base (necesario para descuentos).
     */
    public double getCostoEfectivo(Set<ArtistaBase> artistasBase) {
        return artista.getCostoFinal(artistasBase);
    }

    /**
     * Cuenta la cantidad de canciones distintas en las que 
     * participa un artista en la lista de asignaciones provista.
     */
    public static int contarCancionesDeArtista(List<Asignacion> asignaciones, ArtistaExterno artista) {
        return (int) asignaciones.stream()
                .filter(a -> a.getArtista().equals(artista))
                .map(Asignacion::getCancion)
                .distinct()
                .count();
    }

    @Override
    public String toString() {
        return "Asignacion{" + "artista: " + artista + ", rolAsignado: " + rolAsignado + ", cancion: " + cancion + '}';
    }
    
}
