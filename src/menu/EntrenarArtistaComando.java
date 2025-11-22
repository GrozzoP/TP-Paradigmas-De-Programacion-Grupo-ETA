package menu;

import excepcion.ArtistaNoEntrenable;
import modelo.ArtistaExterno;
import modelo.Recital;
import modelo.Rol;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

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
        mostrarArtistasElegibles();

        System.out.print("\nIngresá el nombre del artista externo a entrenar: ");
        String nombreArtista = scanner.nextLine().trim();

        System.out.print("Ingresá el nombre del rol a enseñar: ");
        String nombreRol = scanner.nextLine().trim();

        Rol nuevoRol = new Rol(nombreRol);

        Optional<ArtistaExterno> artistaOpt = recital.getArtistaExternos().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst();

        if (artistaOpt.isEmpty()) {
            System.out.println(Menu.ANSI_RED + "ERROR: Artista externo '" + nombreArtista + "' no encontrado." + Menu.ANSI_RESET);
            return;
        }

        try {
            ArtistaExterno artista = artistaOpt.get();
            recital.entrenarArtista(artista, nuevoRol);

            System.out.println(Menu.ANSI_GREEN + "\nEntrenamiento exitoso." + Menu.ANSI_RESET);
            System.out.printf("- %s ahora cubre el rol: %s.\n", artista.getNombre(), nuevoRol.getNombre());
            System.out.printf("- Su nuevo costo base es: $%,.2f\n", artista.getCostoBase());

        } catch (ArtistaNoEntrenable e) {
            System.err.println(Menu.ANSI_RED + "\nENTRENAMIENTO FALLIDO: " + e.getMessage() + Menu.ANSI_RESET);
        }
    }

    private void mostrarArtistasElegibles() {
        Set<ArtistaExterno> externos = recital.getArtistaExternos();

        System.out.println("\nArtistas Externos Disponibles:");
        System.out.println("---------------------------------------------------------");

        for (ArtistaExterno artista : externos) {
            if (recital.getEntrenamientoServicio().esArtistaEntrenable(artista, recital.getAsignaciones())) {
                System.out.printf("- %s (Costo: $%,.2f)\n",
                        artista.getNombre(),
                        artista.getCostoBase());
            }
        }
    }

    @Override
    public String getDescripcion() {
        return "Entrenar un artista capacitado en un rol determinado";
    }
}
