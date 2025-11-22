package servicio;

import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Rol;

import java.util.*;
import java.util.stream.Collectors;

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
     * Genera el mensaje de error detallado cuando la contratación falla y hay
     * artistas entrenables disponibles.
     *
     * @param conteoRoles Cantidad de roles necesarios
     * @param recomendaciones La recoemndacion de que roles pueden entrenar
     * @return String con el mensaje de error formateado.
     */
    public String generarMensajeRecomendacion(Map<String, Long> conteoRoles, Map<ArtistaExterno, Set<Rol>> recomendaciones) {
        StringBuilder errorMsg = new StringBuilder();

        errorMsg.append("Contratación incompleta. Faltan cubrir los siguientes roles:\n");

        conteoRoles.forEach((nombre, count) -> {
            errorMsg.append("• ").append(nombre).append(": Falta/n ").append(count).append(".\n");
        });

        errorMsg.append("\n=================================================================\n");
        errorMsg.append("OPCIONES DE ENTRENAMIENTO:\n");
        errorMsg.append("=================================================================\n");

        for (Map.Entry<ArtistaExterno, Set<Rol>> entry : recomendaciones.entrySet()) {
            String rolesAdquiribles = entry.getValue().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.joining(", "));

            errorMsg.append("- ").append(entry.getKey().getNombre()).append(": Puede entrenarse en [");
            errorMsg.append(rolesAdquiribles).append("]\n");
        }

        return errorMsg.toString();
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
