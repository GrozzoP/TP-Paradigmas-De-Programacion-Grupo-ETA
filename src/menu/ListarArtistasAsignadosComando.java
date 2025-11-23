package menu;

import modelo.ArtistaBase;
import modelo.Asignacion;
import modelo.Recital;

import java.util.List;
import java.util.Set;

public class ListarArtistasAsignadosComando implements Comando {
    private Recital recital;

    public ListarArtistasAsignadosComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        Set<ArtistaBase> artistasContratados = this.recital.getArtistasContratados();

        if(artistasContratados.isEmpty()) {
            System.out.println("Actualmente, no hay artistas asignados!");
        }
        else {
            System.out.println("\n--- 🎤 ARTISTAS REGISTRADOS EN EL RECITAL   ---");
            int contador = 1;

            for(ArtistaBase artista : artistasContratados) {
                List<Asignacion> asignacionesPorArtista = this.recital.getAsignacionesPorArtista(artista);

                double costoTotalArtista = asignacionesPorArtista.stream()
                        .mapToDouble(asignacion ->
                                this.recital.getAsignacionServicio().calcularCostoFinalPorAsignacion(
                                        asignacion.getArtista(),
                                        asignacion.getCancion(),
                                        this.recital.getAsignaciones(),
                                        this.recital.getArtistasBase()
                                )
                        ).sum();

                String costoTotal = String.format("$%.2f", costoTotalArtista);
                String costoBase = String.format("$%,.2f", artista.getCostoBase());

                boolean esArtistaExterno = artista.getCostoBase() > 0;
                String operacion = esArtistaExterno ? "Contratado" : "Asignado";

                long cantidadCancionesAsig = asignacionesPorArtista.stream()
                        .map(Asignacion::getCancion)
                        .distinct()
                        .count();

                System.out.println(Menu.ANSI_GREEN + contador++ + "° ARTISTA: " + artista.getNombre() + " | " + operacion + Menu.ANSI_RESET);

                System.out.printf("   - Costo base: %s\n", costoBase);
                System.out.printf("   - Total (asignaciones): %s\n", costoTotal);
                System.out.printf("   - Cantidad de canciones asignadas: %d / %d\n",
                        cantidadCancionesAsig, artista.getMaxCanciones());
                System.out.println("   - Detalle de asignaciones:");
                asignacionesPorArtista.forEach(a -> {
                    System.out.printf("     • En '%s' como %s\n", a.getCancion().getTitulo(), a.getRolAsignado().getNombre());
                });

                System.out.println("----------------------------------------------");
            }
        }
    }

    @Override
    public String getDescripcion() {
        return "Listar los artistas presentes en las asignaciones";
    }
}
