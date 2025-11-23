package menu;

import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Recital;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class EliminarArtistaRecitalComando implements Comando {
    private Recital recital;
    private Scanner scanner;

    public EliminarArtistaRecitalComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n--- ELIMINAR ARTISTA DE ASIGNACIONES ---");
        System.out.print("Ingresá el nombre del artista a eliminar del recital: ");
        String nombreArtista = this.scanner.nextLine().trim();

        Optional<ArtistaBase> artistaOpc =  buscarArtista(nombreArtista);

        if(artistaOpc.isEmpty()) {
            System.out.println(Menu.ANSI_RED + "El artista '" + nombreArtista + "' no existe!");
        } else {
            ArtistaBase artista = artistaOpc.get();

            int asignacionesElim = this.recital.quitarArtista(artista);

            if(asignacionesElim > 0) {
                System.out.printf(Menu.ANSI_GREEN + "Se desvinculó al artista %s de %d asignación(es).\n" + Menu.ANSI_RESET,
                        nombreArtista, asignacionesElim);
            } else {
                System.out.printf(Menu.ANSI_YELLOW + "El artista %s no está vinculado a ninguna canción!\n" + Menu.ANSI_RESET,
                        nombreArtista);
            }
        }
    }

    private Optional<ArtistaBase> buscarArtista(String nombre) {
        Set<ArtistaBase> artistasBase = recital.getArtistasBase();
        Set<ArtistaExterno> artistasExternos = recital.getArtistaExternos();

        return Stream.concat(artistasBase.stream(), artistasExternos.stream())
                .filter(a -> a.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public String getDescripcion() {
        return "(BONUS) Eliminar a un artista ya asignado";
    }
}
