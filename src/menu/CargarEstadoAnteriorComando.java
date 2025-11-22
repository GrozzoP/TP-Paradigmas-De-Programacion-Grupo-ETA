package menu;

import cargador_datos.CargadorDatos;
import dto.EstadoRecitalDTO;
import modelo.Recital;

import java.util.List;

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
            recital.cargarEstadoPrevio(estadoGuardado);

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "ERROR al cargar el estado: " + e.getMessage() + Menu.ANSI_RESET);
        }
    }

    @Override
    public String getDescripcion() {
        return "Cargar del archivo de salida los datos anteriores";
    }
}
