package menu;

import cargador_datos.CargadorDatos;
import dto.EstadoRecitalDTO;
import modelo.Recital;

import java.util.List;
import java.util.Map;

public class CargarEstadoAnteriorComando implements Comando {
    private Recital recital;

    public CargarEstadoAnteriorComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- CARGANDO ESTADO PREVIO ---");
        try {
            List<EstadoRecitalDTO> estadoGuardado = CargadorDatos.cargarEstado();

            Map<Integer, Double> respuesta = recital.cargarEstadoPrevio(estadoGuardado);

            if(!respuesta.isEmpty()) {
                Map.Entry<Integer, Double> entrada = respuesta.entrySet().iterator().next();

                int asignacionesCargadas = entrada.getKey();
                double costoTotal = entrada.getValue();

                System.out.printf("Estado previo cargado con exito!\n");
                System.out.printf("   - %d asignaciones cargadas.\n", asignacionesCargadas);
                System.out.printf("   - Costo total de las asignaciones cargadas: $%,.2f\n", costoTotal);
            } else {
                System.out.println("No hay datos para cargar en el estado previo");
            }

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "ERROR al cargar el estado: " + e.getMessage() + Menu.ANSI_RESET);
        }
    }

    @Override
    public String getDescripcion() {
        return "(BONUS) Cargar del archivo de salida los datos anteriores";
    }
}
