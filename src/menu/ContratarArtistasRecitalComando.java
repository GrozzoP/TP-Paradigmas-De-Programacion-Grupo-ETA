package menu;

import excepcion.RolesNoCubiertos;
import modelo.Asignacion;
import modelo.Recital;

import java.util.List;
import java.util.Scanner;

public class ContratarArtistasRecitalComando implements Comando {
    private Recital recital;
    private Scanner scanner;

    public ContratarArtistasRecitalComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- CONTRATACIÓN PARA TODO EL RECITAL ---");

        List<Asignacion> nuevasAsignaciones = null;
        int asignacionesAntes = recital.getAsignaciones().size();

        String mensajeDeFallo = null;

        try {
            nuevasAsignaciones = recital.contratarParaRecitalCompleto();

        } catch (RolesNoCubiertos e) {
            mensajeDeFallo = e.getMessage();
            List<Asignacion> todasAsignacionesDespues = recital.getAsignaciones();
            nuevasAsignaciones = todasAsignacionesDespues.subList(asignacionesAntes, todasAsignacionesDespues.size());

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "\nERROR INESPERADO: " + e.getMessage() + Menu.ANSI_RESET);
        }

        int asignacionesNue = (nuevasAsignaciones != null) ? nuevasAsignaciones.size() : 0;

        if (asignacionesNue > 0) {
            System.out.println(Menu.ANSI_GREEN + "\n✅ PROCESO DE ASIGNACIÓN EXITOSO. Se crearon " + asignacionesNue + " nuevas asignaciones." + Menu.ANSI_RESET);
            System.out.println("\n--- DETALLE DE LAS CONTRATACIONES EXITOSAS ---");

            for(Asignacion asignacion : nuevasAsignaciones) {
                recital.mostrarDetalleAsignacion(asignacion);
            }
        } else if (mensajeDeFallo == null) {
            System.out.println(Menu.ANSI_GREEN + "\n✅ PROCESO COMPLETADO. No se requirieron nuevas asignaciones." + Menu.ANSI_RESET);
        }

        if (mensajeDeFallo != null) {
            System.out.println(Menu.ANSI_RED + "\n❌ CONTRATACIÓN PARCIAL: Fallaron algunas canciones." + Menu.ANSI_RESET);
            System.out.println(mensajeDeFallo);
        }

        System.out.println("\n-----------------------------------------------------------------");
        System.out.printf("Costo total final del recital: $%,.2f\n", recital.getCostoTotalRecital());
        System.out.println("-----------------------------------------------------------------");
    }

    @Override
    public String getDescripcion() {
        return "Contratar a TODOS los artistas necesarios para el recital";
    }
}
