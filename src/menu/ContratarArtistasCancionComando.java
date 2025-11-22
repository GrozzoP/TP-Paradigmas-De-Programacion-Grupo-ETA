package menu;

import excepcion.RolesNoCubiertos;
import modelo.Asignacion;
import modelo.Cancion;
import modelo.Recital;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ContratarArtistasCancionComando implements Comando {
    private Recital recital;
    private Scanner scanner;

    public ContratarArtistasCancionComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {
        System.out.print("Ingresá el nombre de la canción para contratar artistas: ");

        String cancionStr = scanner.nextLine().trim();

        Optional<Cancion> cancionOpt = recital.getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(cancionStr))
                .findFirst();

        if(cancionOpt.isPresent()) {
            Cancion cancion = cancionOpt.get();

            try {
                List<Asignacion> nuevasAsignaciones = recital.contratarParaCancion(cancion);

                if(!nuevasAsignaciones.isEmpty()) {
                    System.out.println(Menu.ANSI_GREEN + "\n✅ Asignación exitosa para '" + cancion.getTitulo() + "'. Se realizaron " + nuevasAsignaciones.size() + " asignación(es):" + Menu.ANSI_RESET);

                    for(Asignacion asignacion : nuevasAsignaciones) {
                        this.recital.mostrarDetalleAsignacion(asignacion);
                    }
                }

            } catch (RolesNoCubiertos e) {
                System.out.println(Menu.ANSI_RED + "\n❌ Contratación fallida. El motivo es: " + Menu.ANSI_RESET);
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(Menu.ANSI_RED + "\n❌ ERROR: Canción '" + cancionStr + "' no encontrada en el recital." + Menu.ANSI_RESET);
        }
    }

    @Override
    public String getDescripcion() {
        return "Contratar artistas para UNA cancion";
    }
}
