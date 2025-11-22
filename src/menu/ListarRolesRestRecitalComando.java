package menu;

import modelo.Cancion;
import modelo.Recital;
import modelo.Rol;

import java.util.Map;

public class ListarRolesRestRecitalComando implements Comando {
    private Recital recital;

    public ListarRolesRestRecitalComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        boolean hayCanciones = false;
        System.out.println("\n--- 🔎 ROLES FALTANTES PARA EL RECITAL COMPLETO ---");

        for(Cancion cancion : recital.getCanciones()) {
            Map<Rol, Integer> rolesFaltantes = recital.getRolesFaltantes(cancion);

            if(!rolesFaltantes.isEmpty()) {
                System.out.println("❌ La canción '" + cancion.getTitulo() + "' está incompleta, faltan los siguientes roles...");

                rolesFaltantes.forEach((rol, cantidad) -> {
                    System.out.printf("- [%s] : Falta/n %d\n", rol.getNombre(), cantidad);
                });
                hayCanciones = true;
            }
        }
        if(!hayCanciones) {
            System.out.println(Menu.ANSI_GREEN + "Todo el recital está completamente cubierto." + Menu.ANSI_RESET);
        }
        System.out.println("----------------------------------------");
    }

    @Override
    public String getDescripcion() {
        return "Listar los roles faltantes para las canciones del recital";
    }
}
