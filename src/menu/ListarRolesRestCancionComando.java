package menu;

import modelo.Cancion;
import modelo.Recital;
import modelo.Rol;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ListarRolesRestCancionComando implements Comando {
    private Recital recital;
    private Scanner scanner;

    public ListarRolesRestCancionComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {
        System.out.print("Ingresá el nombre de una canción del recital: ");

        String cancionStr = scanner.nextLine().trim();

        Optional<Cancion> cancionOpc = recital.getCanciones().stream()
                    .filter(c -> c.getTitulo().equalsIgnoreCase(cancionStr))
                    .findFirst();

        if(cancionOpc.isPresent()) {
            Cancion cancion = cancionOpc.get();

            Map<Rol, Integer> rolesFaltantes = recital.getRolesFaltantes(cancion);

            System.out.println("\n--- 🔎 ROLES FALTANTES PARA '" + cancion.getTitulo() + "' ---");

            if(rolesFaltantes.isEmpty()) {
                System.out.println("✅ La cancion " + cancionStr + " está completa!");
            } else {
                System.out.println("❌ La canción está incompleta, faltan los siguientes roles...");

                rolesFaltantes.forEach((rol, cantidad) -> {
                    System.out.printf("- [%s] : Falta/n %d\n", rol.getNombre(), cantidad);
                });
            }
        } else {
            System.out.println("La canción con título '" + cancionStr + "' no se encuentra en el recital.");
        }

    }

    @Override
    public String getDescripcion() {
        return "Listar los roles faltantes para UNA cancion";
    }
}
