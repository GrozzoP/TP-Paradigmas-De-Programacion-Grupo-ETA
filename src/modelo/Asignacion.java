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


    @Override
    public String toString() {
        return "Asignacion{" + "artista: " + artista + ", rolAsignado: " + rolAsignado + ", cancion: " + cancion + '}';
    }
    
}
