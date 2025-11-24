package test;

import cargador_datos.CargadorDatos;
import modelo.Recital;
import modelo.Rol;
import org.junit.jupiter.api.Test;
import servicio.PrologServicio;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrologTest {

    @Test
    void testMinEntrenamientosCoincideConRolesFaltantes() throws Exception {
        CargadorDatos loader = new CargadorDatos();
        Recital recital = new Recital(loader);
        PrologServicio prolog = new PrologServicio();

        Map<Rol, Integer> faltantes = recital.getRolesFaltantesTotales();
        int cantidadFaltantes = faltantes.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        int min = prolog.calcularEntrenamientosMinimos(recital);

        assertEquals(cantidadFaltantes, min);
    }

    @Test
    void testCostoEntrenamientosEsMinPorCostoBase() throws Exception {
        CargadorDatos loader = new CargadorDatos();
        Recital recital = new Recital(loader);
        PrologServicio prolog = new PrologServicio();

        double costoBase = 1000.0;

        int min = prolog.calcularEntrenamientosMinimos(recital);
        double costoTotal = prolog.calcularCostoTotalEntrenamientos(recital, costoBase);


        assertEquals(min * costoBase, costoTotal, 0.01);
    }
}
