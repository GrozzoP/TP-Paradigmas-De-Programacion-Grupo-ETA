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
            System.out.println("⚠️ No hay asignaciones para guardar.");
            return;
        }

        // Renombrar la lista a algo más descriptivo si aplica
        List<EstadoRecitalDTO> dtoList = new ArrayList<>();

        try {
            for (Asignacion asignacion : asignaciones) {

                // 1. Calcular el costo final (el descuento ya está incluido en este valor)
                double costoFinal = recital.getAsignacionServicio().calcularCostoFinalPorAsignacion(
                        asignacion.getArtista(),
                        asignacion.getCancion(),
                        recital.getAsignaciones(),
                        recital.getArtistasBase()
                );

                // 2. 💡 CAMBIO: Usamos el constructor del DTO sin el campo 'descuento'
                dtoList.add(new EstadoRecitalDTO(asignacion, costoFinal));
            }

            // Asumo que tienes CargadorDatos.guardarEstado(List<EstadoRecitalDTO>)
            CargadorDatos.guardarEstado(dtoList);

            System.out.println(Menu.ANSI_GREEN + "✅ Estado guardado con éxito en: " + CargadorDatos.RUTA_ARCHIVO_GUARDADO + Menu.ANSI_RESET);
            System.out.printf("Se serializaron %d asignaciones.\n", dtoList.size());

        } catch (Exception e) {
            System.err.println(Menu.ANSI_RED + "❌ ERROR al guardar el estado: " + e.getMessage() + Menu.ANSI_RESET);
        }
    }

    @Override
    public String getDescripcion() {
        return "Guardar el estado actual del recital";
    }
}
