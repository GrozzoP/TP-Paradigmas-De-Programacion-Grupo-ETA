package menu;

import excepcion.RolesNoCubiertos;
import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Recital;
import modelo.Rol;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContratarArtistasRecitalComando implements Comando {
    private Recital recital;

    public ContratarArtistasRecitalComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- CONTRATACIÓN PARA TODO EL RECITAL ---");

        List<Asignacion> nuevasAsignaciones = null;
        int asignacionesAntes = recital.getAsignaciones().size();
        String mensajeDeFallo = null;
        boolean huboFallo = false;

        try {
            nuevasAsignaciones = recital.contratarParaRecitalCompleto();
        } catch (RolesNoCubiertos e) {
            mensajeDeFallo = e.getMessage();
            huboFallo = true;

            List<Asignacion> todasAsignacionesDespues = recital.getAsignaciones();
            nuevasAsignaciones = todasAsignacionesDespues.subList(asignacionesAntes, todasAsignacionesDespues.size());

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "\n ERROR INESPERADO: " + e.getMessage() + Menu.ANSI_RESET);
        }

        int asignacionesNue = (nuevasAsignaciones != null) ? nuevasAsignaciones.size() : 0;

        if (asignacionesNue > 0) {
            System.out.println(Menu.ANSI_GREEN + "\n✅ PROCESO DE ASIGNACIÓN EXITOSO. Se crearon " + asignacionesNue + " nuevas asignaciones." + Menu.ANSI_RESET);
            System.out.println("\n--- DETALLE DE LAS CONTRATACIONES EXITOSAS ---");

            for(Asignacion asignacion : nuevasAsignaciones) {
                recital.mostrarDetalleAsignacion(asignacion);
            }
        } else if (mensajeDeFallo == null && !huboFallo) {
            System.out.println(Menu.ANSI_GREEN + "\n✅ PROCESO COMPLETADO. No se generaron nuevas asignaciones." + Menu.ANSI_RESET);
        }

        Map<Rol, Integer> rolesFaltantesTotales = recital.getRolesFaltantesTotales();
        String reporteFaltantes = null;

        if (huboFallo && !rolesFaltantesTotales.isEmpty()) {
            Set<Rol> rolesFaltantesSet = rolesFaltantesTotales.keySet();

            Map<ArtistaExterno, Set<Rol>> recomendaciones = recital.getEntrenamientoServicio().recomendarEntrenamiento(
                    rolesFaltantesSet,
                    recital.getArtistaExternos(),
                    recital.getAsignaciones()
            );

            if (!recomendaciones.isEmpty()) {
                Map<String, Long> conteoRoles = rolesFaltantesTotales.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().getNombre(),
                                e -> e.getValue().longValue()
                        ));

                reporteFaltantes = recital.getEntrenamientoServicio().generarMensajeRecomendacion(conteoRoles, recomendaciones);
            } else {
                StringBuilder sbReporteFaltantes = new StringBuilder();

                sbReporteFaltantes.append("Contratación incompleta! Faltan cubrir los siguientes roles:\n");

                rolesFaltantesTotales.forEach((rol, cont) -> {
                    sbReporteFaltantes.append("• ")
                            .append(rol.getNombre())
                            .append(": Falta/n ")
                            .append(cont)
                            .append(".\n");
                });

                sbReporteFaltantes.append("\nNo se encontraron artistas externos para entrenar!");

                reporteFaltantes = sbReporteFaltantes.toString();
            }

            System.out.println(Menu.ANSI_RED + "\n❌ CONTRATACIÓN PARCIAL: Fallaron algunas canciones." + Menu.ANSI_RESET);
            System.out.println(reporteFaltantes);
            System.out.println("==================================================================");


        } else if (mensajeDeFallo != null) {
            System.out.println(Menu.ANSI_RED + "\nCONTRATACIÓN PARCIAL: Fallaron algunas canciones." + Menu.ANSI_RESET);
            System.out.println(mensajeDeFallo);
        }

        System.out.println("\n-----------------------------------------------------------------");
        System.out.printf(Menu.ANSI_GREEN + "Costo total final del recital: $%,.2f\n" + Menu.ANSI_RESET, recital.getCostoTotalRecital());
        System.out.println("-----------------------------------------------------------------");
    }

    @Override
    public String getDescripcion() {
        return "Contratar a TODOS los artistas necesarios para el recital";
    }
}
