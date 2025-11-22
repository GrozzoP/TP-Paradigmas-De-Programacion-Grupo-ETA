package servicio;

import modelo.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsignacionServicio {
    /**
     * Verifica si un Artista es elegible para cubrir un Rol en la iteración actual
     * del algoritmo Greedy, comprobando la no repetición dentro de ESTA operación.
     * * @param asignaciones La lista global de asignaciones del Recital (para chequear el límite de canciones).
     * @param candidato El Artista (Base o Externo) a evaluar.
     * @param rol El rol que debe cubrir.
     * @param artistasElegidos El Set de artistas elegidos en la operación actual de contratación.
     * @return true si el artista pasa todas las reglas de elegibilidad.
     */
    public boolean esArtistaElegibleParaRol(
            List<Asignacion> asignaciones,
            ArtistaBase candidato,
            Rol rol,
            Set<ArtistaBase> artistasElegidos
    ) {
        if (!candidato.puedeCubrir(rol)) {
            return false;
        }

        if (artistasElegidos.contains(candidato)) {
            return false;
        }

        if (!this.respetaLimiteCanciones(candidato, asignaciones)) {
            return false;
        }

        return true;
    }

    /**
     * Obtiene la lista de Artistas Base que están asignados a la Canción.
     */
    public Set<ArtistaBase> getArtistasBaseCancion(List<Asignacion> asignaciones, Set<ArtistaBase> artistasBase, Cancion cancion) {
        Set<ArtistaBase> artistasBasePresentes = new HashSet<>();

        for(Asignacion asignacion : asignaciones) {
            if(asignacion.getCancion().equals(cancion) && artistasBase.contains(asignacion.getArtista()))
                artistasBasePresentes.add(asignacion.getArtista());
        }

        return artistasBasePresentes;
    }

    public boolean chequearDescuentoArtista(
            ArtistaBase artista,
            Cancion cancion,
            List<modelo.Asignacion> asignaciones,
            Set<ArtistaBase> artistasBase
    ) {
        if (artista.getCostoBase() == 0) {
            return false;
        }

        return this.esDescuentoAplicablePorCancion(
                (ArtistaExterno) artista,
                cancion,
                asignaciones,
                artistasBase
        );
    }

    /**
     * Verifica si el descuento del 50% es aplicable a un ArtistaExterno candidato
     * basándose en si comparte banda con algún ArtistaBase que ya esté
     * contratado y asignado para la CANCIÓN Específica.
     * @param candidatoExterno El ArtistaExterno candidato a contratar.
     * @param cancion La Cancion para la cual se está evaluando el descuento.
     * @param asignaciones La lista global de asignaciones del Recital.
     * @return true si se aplica el descuento (comparte banda con un ArtistaBase en esa Canción).
     */
    public boolean esDescuentoAplicablePorCancion(
            ArtistaExterno candidatoExterno,
            Cancion cancion,
            List<Asignacion> asignaciones,
            Set<ArtistaBase> artistasBaseDelRecital
    ) {
        Set<ArtistaBase> artBasesEnCancion = this.getArtistasBaseCancion(asignaciones, artistasBaseDelRecital, cancion);

        if (artBasesEnCancion.isEmpty()) {
            return false;
        }

        return artBasesEnCancion.stream().anyMatch(candidatoExterno::comparteBanda);
    }

    public double calcularCostoFinalPorAsignacion(
            ArtistaBase artista,
            Cancion cancion,
            List<Asignacion> asignaciones,
            Set<ArtistaBase> artistasBase
    ) {
        if(artista.getCostoBase() == 0)
            return 0.0;
        else
        {
            double costoBaseConIncremento = artista.getCostoBase();

            boolean aplicaDescuento = this.esDescuentoAplicablePorCancion(
                    (ArtistaExterno) artista, cancion, asignaciones, artistasBase
            );

            return aplicaDescuento ? costoBaseConIncremento * ArtistaExterno.DESCUENTO_BANDA : costoBaseConIncremento;
        }
    }

    public double calcularCostoTotalCancion(Cancion cancion, List<Asignacion> asignaciones, Set<ArtistaBase> artistasBaseRecital) {
        Set<ArtistaBase> artistasEnCancion = new HashSet<>();

        for (Asignacion a : asignaciones) {
            if (a.getCancion().equals(cancion)) {
                artistasEnCancion.add(a.getArtista());
            }
        }

        return artistasEnCancion.stream()
                .mapToDouble(artista -> {
                    return this.calcularCostoFinalPorAsignacion(artista, cancion, asignaciones, artistasBaseRecital);}).sum();
    }

    // METODOS AUXILIARES
    private int contarCancionesDeArtista(List<Asignacion> asignaciones, ArtistaBase artista) {
        return (int) asignaciones.stream()
                .filter(a -> a.getArtista().equals(artista))
                .map(Asignacion::getCancion)
                .distinct()
                .count();
    }

    private boolean respetaLimiteCanciones(ArtistaBase artista, List<Asignacion> asignaciones) {
        int cancionesYaAsignadas = contarCancionesDeArtista(asignaciones, artista);
        return cancionesYaAsignadas < artista.getMaxCanciones();
    }
}
