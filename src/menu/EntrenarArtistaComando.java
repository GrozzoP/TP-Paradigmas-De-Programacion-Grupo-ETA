package menu;

import excepcion.ArtistaNoEntrenable;
import modelo.ArtistaExterno;
import modelo.Recital;
import modelo.Rol;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class EntrenarArtistaComando implements Comando {
    private Recital recital;
    private Scanner scanner;

    public EntrenarArtistaComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- ENTRENAR ARTISTA EXTERNO EN NUEVO ROL ---");
        Set<ArtistaExterno> elegibles = obtenerYMostrarArtistasElegibles();

        if(elegibles.isEmpty()) {
            System.out.println(Menu.ANSI_YELLOW + "\nNo hay artistas externos elegibles para entrenamiento (están contratados o no existen)." + Menu.ANSI_RESET);
        } else {
            Optional<ArtistaExterno> artistaOpc;
            String nombreArtista;

            do {
                System.out.print("\nIngrese el nombre del artista externo a entrenar: ");
                nombreArtista = scanner.nextLine().trim();

                final String nombreArtistaConst = nombreArtista;

                artistaOpc = elegibles.stream()
                        .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtistaConst))
                        .findFirst();

                if (artistaOpc.isEmpty()) {
                    System.out.println(Menu.ANSI_RED + "El Artista '" + nombreArtista + "' no fue encontrado o no es elegible. Ingresar de nuevo..." + Menu.ANSI_RESET);
                }
            } while (artistaOpc.isEmpty());

            ArtistaExterno artista = artistaOpc.get();

            Rol nuevoRol;
            boolean rolYaCubierto = false;
            String nombreRol;

            do {
                System.out.print("Ingrese el nombre del rol a enseñar: ");
                nombreRol = scanner.nextLine().trim();

                nuevoRol = new Rol(nombreRol);

                rolYaCubierto = artista.puedeCubrir(nuevoRol);

                if (rolYaCubierto) {
                    System.out.println(Menu.ANSI_YELLOW + "El artista " + artista.getNombre() +
                            " ya cubre el rol '" + nombreRol + "' históricamente o por entrenamiento previo. Ingrese un rol nuevo." + Menu.ANSI_RESET);
                }

            } while (rolYaCubierto);

            try {
                recital.entrenarArtista(artista, nuevoRol);

                System.out.println(Menu.ANSI_GREEN + "\nEntrenamiento exitoso!" + Menu.ANSI_RESET);
                System.out.printf("- %s ahora cubre el rol: %s.\n", artista.getNombre(), nuevoRol.getNombre());
                System.out.printf("- Su nuevo costo base es: $%,.2f\n", artista.getCostoBase());

            } catch (ArtistaNoEntrenable e) {
                System.err.println(Menu.ANSI_RED + "\nENTRENAMIENTO FALLIDO: " + e.getMessage() + Menu.ANSI_RESET);
            }
        }
    }

    private Set<ArtistaExterno> obtenerYMostrarArtistasElegibles() {
        Set<ArtistaExterno> externos = recital.getArtistaExternos();

        Set<ArtistaExterno> elegibles = externos.stream()
                .filter(a -> recital.getEntrenamientoServicio().esArtistaEntrenable(a, recital.getAsignaciones()))
                .collect(Collectors.toSet());

        System.out.print("\nArtistas externos disponibles para entrenar:\n");
        System.out.println("---------------------------------------------------------");

        if (!elegibles.isEmpty()) {
            for (ArtistaExterno artista : elegibles) {
                System.out.printf("- %s (Costo base: $%,.2f)\n",
                        artista.getNombre(),
                        artista.getCostoBase());
            }
            System.out.println("---------------------------------------------------------");

        }

        return elegibles;
    }

    @Override
    public String getDescripcion() {
        return "Entrenar un artista capacitado en un rol determinado";
    }
}
