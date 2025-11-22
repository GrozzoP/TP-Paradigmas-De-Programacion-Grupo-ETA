package menu;

import modelo.*;
import java.util.List;
import java.util.Map;

public class ListarEstadoCancionesComando implements Comando {
    private Recital recital;

    public ListarEstadoCancionesComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        for (Cancion c : this.recital.getCanciones()) {

            List<Asignacion> asignacionesPorCancion = this.recital.getAsignacionesPorCancion(c);
            Map<Rol, Integer> rolesFaltantes = this.recital.getRolesFaltantes(c);
            boolean completa = rolesFaltantes.isEmpty();

            double costo = this.recital.getAsignacionServicio()
                    .calcularCostoTotalCancion(c, asignacionesPorCancion, this.recital.getArtistasBase());

            List<ArtistaBase> artistasPorCancion = asignacionesPorCancion.stream()
                    .map(Asignacion::getArtista)
                    .toList();

            System.out.printf("CANCIÓN: %s | Costo: $%,.2f | Completa: %s\n",
                    c.getTitulo(),
                    costo,
                    completa ? Menu.ANSI_GREEN + "SÍ" + Menu.ANSI_RESET : Menu.ANSI_RED + "NO" + Menu.ANSI_RESET);

            if (!artistasPorCancion.isEmpty()) {
                System.out.println("    Artistas Asignados:");
                asignacionesPorCancion.forEach(a -> {
                    System.out.printf("     - %s (Rol: %s)\n",
                            a.getArtista().getNombre(),
                            a.getRolAsignado().getNombre());
                });
            } else {
                System.out.println("    Artistas Asignados: Ninguno.");
            }

            if (!completa) {
                System.out.println(Menu.ANSI_RED +  "    Roles Faltantes:" + Menu.ANSI_RESET);
                rolesFaltantes.forEach((rol, cantidad) -> {
                    System.out.printf("    - %s: Falta/n %d artista(s)\n",
                            rol.getNombre(),
                            cantidad);
                });
            }

            System.out.println("------------------------------------------------------------------");
        }
    }

    @Override
    public String getDescripcion() {
        return "Listar el estado actual de las canciones";
    }
}
