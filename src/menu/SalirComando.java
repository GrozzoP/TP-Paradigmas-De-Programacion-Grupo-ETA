package menu;

import cargador_datos.CargadorDatos;
import dto.EstadoRecitalDTO;
import modelo.Asignacion;
import modelo.Recital;

import java.util.ArrayList;
import java.util.List;

public class SalirComando implements Comando {
    private Recital recital;
    private Menu menu;

    public SalirComando(Recital recital, Menu menu) {
        this.recital = recital;
        this.menu = menu;
    }

    @Override
    public void ejecutar() {
        List<Asignacion> asignaciones = recital.getAsignaciones();

        if (asignaciones.isEmpty()) {
            System.out.println("No hay asignaciones para guardar... Asi que se terminará el programa sin guardar nada :(");
            return;
        }

        List<EstadoRecitalDTO> estadosRecital = new ArrayList<>();

        try {
            for (Asignacion asignacion : asignaciones) {
                double costoFinal = recital.getAsignacionServicio().calcularCostoFinalPorAsignacion(
                        asignacion.getArtista(),
                        asignacion.getCancion(),
                        recital.getAsignaciones(),
                        recital.getArtistasBase()
                );

                estadosRecital.add(new EstadoRecitalDTO(asignacion, costoFinal));
            }

            CargadorDatos.guardarEstado(estadosRecital);

            System.out.println(Menu.ANSI_GREEN + "Estado de asignaciones guardado con éxito." + Menu.ANSI_RESET);

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "ERROR AL GUARDAR EL ESTADO: El se va a cerrar sin guardar el estado actual. Motivo: " + e.getMessage() + Menu.ANSI_RESET);
        }

        this.menu.setEstaEnEjecucion(false);
    }

    @Override
    public String getDescripcion() {
        return "Salir del programa";
    }
}
