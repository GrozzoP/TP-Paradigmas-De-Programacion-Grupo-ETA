package servicio;

import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Rol;

import java.util.*;

public class EntrenarServicio {
    /**
     * Verifica si un ArtistaExterno es elegible para adquirir un nuevo rol mediante entrenamiento.
     * Reglas: 1. No debe estar contratado en ninguna canción. 2. No debe ser un artistaBase.
     * * @param artista El ArtistaExterno a evaluar.
     * @param asignaciones La lista global de asignaciones del Recital.
     * @return true si el artista es elegible para ser entrenado.
     */
    public boolean esArtistaEntrenable(ArtistaExterno artista, List<Asignacion> asignaciones) {
        boolean noEstaContratado = asignaciones.stream()
                .noneMatch(a -> a.getArtista().equals(artista));

        boolean esEntrenable = artista.esEntrenable();

        return noEstaContratado && esEntrenable;
    }

    /**
     * Identifica artistas externos que son entrenables y podrían cubrir roles faltantes.
     * * @param rolesFaltantes Los roles que quedaron sin cubrir.
     * @param artistasExternos Todos los artistas externos disponibles.
     * @param asignaciones La lista global de asignaciones para chequear si el artista ya fue contratado.
     * @return Mapa de ArtistaExterno y los Roles faltantes que pueden adquirir.
     */
    public Map<ArtistaExterno, Set<Rol>> recomendarEntrenamiento(Set<Rol> rolesFaltantes, Set<ArtistaExterno> artistasExternos, List<Asignacion> asignaciones) {
        Map<ArtistaExterno, Set<Rol>> recomendaciones = new HashMap<>();

        for (ArtistaExterno artista : artistasExternos) {
            if (this.esArtistaEntrenable(artista, asignaciones)) {
                Set<Rol> rolesPuedeAdquirir = new HashSet<>();

                for (Rol rol : rolesFaltantes) {
                    if (!artista.puedeCubrir(rol)) {
                        rolesPuedeAdquirir.add(rol);
                    }
                }

                if (!rolesPuedeAdquirir.isEmpty()) {
                    recomendaciones.put(artista, rolesPuedeAdquirir);
                }
            }
        }

        return recomendaciones;
    }
}
