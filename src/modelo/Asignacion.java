package modelo;

import java.util.Set;

public class Asignacion {
    //¿Podrian ser variables finales ya que no se deberian modificar? Abro hilo...
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
     * Abro hilo...
     * Devuelve el costo efectivo de esta asignación, solicitando al artista
     * su costo final y pasándole el conjunto de artistas base (para chequear descuentos).
     *
     * Nota: el costo devuelto es por canción (la consigna indica "costo por contratación por canción").
     */
    public double getCostoEfectivo(Set<ArtistaBase> artistasBase) {
        return artista.getCostoFinal(artistasBase);
    }

    @Override
    public String toString() {
        return "Asignacion{" + "artista: " + artista + ", rolAsignado: " + rolAsignado + ", cancion: " + cancion + '}';
    }
    
}
