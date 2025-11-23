package menu;

import cargador_datos.CargadorDatos;
import dto.EstadoRecitalDTO;
import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Recital;

import java.util.ArrayList;
import java.util.List;

public class GuardarEstadoComando implements Comando {
    private Recital recital;

    public GuardarEstadoComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- 💾 GUARDANDO ESTADO DE ASIGNACIONES ---");

        List<Asignacion> asignaciones = recital.getAsignaciones();
        if (asignaciones.isEmpty()) {
            System.out.println("No hay asignaciones para guardar.");
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

            System.out.println(Menu.ANSI_GREEN + "✅ Estado guardado con éxito en: " + CargadorDatos.RUTA_ARCHIVO_GUARDADO + Menu.ANSI_RESET);
            System.out.printf("Se almacenaron %d asignaciones.\n", estadosRecital.size());

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "❌ ERROR al guardar el estado: " + e.getMessage() + Menu.ANSI_RESET);
        }
    }

    @Override
    public String getDescripcion() {
        return "(BONUS) Guardar el estado actual del recital";
    }
}
