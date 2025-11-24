package test;

import cargador_datos.CargadorDatos;
import excepcion.RolesNoCubiertos;
import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecitalTest {

    private Recital recital;
    private ArtistaBase base, otroBase;
    private ArtistaExterno externo, otroExterno;
    private Cancion cancionEuropa;
    private Rol rolVoz, rolGuitarra, rolCompositor;

    // Clase dummy para simular la carga
    private class CargadorDatosDummy extends CargadorDatos {
        private final ArtistaBase baseRef, otroBase;
        private final ArtistaExterno externoRef, otroExternoRef;
        private final Cancion cancionRef;

        public CargadorDatosDummy(ArtistaBase base, ArtistaBase otroBase, ArtistaExterno externo, ArtistaExterno otroExterno, Cancion cancion) {
            this.baseRef = base;
            this.otroBase = otroBase;
            this.externoRef = externo;
            this.otroExternoRef = otroExterno;
            this.cancionRef = cancion;
        }

        @Override
        public Set<Cancion> cargarCanciones() {
            Set<Cancion> canciones = new HashSet<>();
            canciones.add(cancionEuropa);
            return canciones;
        }

        @Override
        public Set<ArtistaBase> cargarSoloArtistasBase() {
            Set<ArtistaBase> artistas = new HashSet<>();
            artistas.add(base);
            artistas.add(otroBase);
            return artistas;
        }

        @Override
        public Set<ArtistaExterno> cargarSoloArtistasExternos() {
            Set<ArtistaExterno> artistas = new HashSet<>();
            artistas.add(externo);
            artistas.add(otroExterno);
            return artistas;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        rolVoz = new Rol("Voz");
        rolGuitarra = new Rol("Guitarrista");
        rolCompositor = new Rol("Compositor");

        // ARTISTAS
        base = new ArtistaBase("Carlos Santana", 0.0, 6);
        otroBase = new ArtistaBase("Charly García", 0.0, 5);
        externo = new ArtistaExterno("Dua Lipa", 60000.0, 7);
        otroExterno = new ArtistaExterno("Saje", 7000.0, 7);
        base.agregarColaboracion(new Colaboracion(new Banda("Santana"), Set.of(rolGuitarra, rolCompositor)));
        otroBase.agregarColaboracion(new Colaboracion(new Banda("SeruGiran"), Set.of(rolCompositor)));
        externo.agregarColaboracion(new Colaboracion(new Banda("PopHits"), Set.of(rolVoz)));
        otroExterno.agregarColaboracion((new Colaboracion(new Banda("Kamada"), Set.of(rolGuitarra))));

        // CANCION
        cancionEuropa = new Cancion("Europa", Map.of(rolGuitarra, 1, rolCompositor, 1, rolVoz, 1));

        // RECITAL
        recital = new Recital(new CargadorDatosDummy(base, otroBase, externo, otroExterno, cancionEuropa));
    }

    @Test
    void dadoRecitalRecienCargado_cuandoSeLlamaRolesFaltantesTotales_entoncesRetornaSumaDeRolesRequeridos() {
        Map<Rol, Integer> faltantes = recital.getRolesFaltantesTotales();

        assertEquals(3, faltantes.size(), "Debería haber 3 tipos de roles faltantes al inicio (son todos los roles cargados).");
        assertEquals(1, faltantes.get(rolCompositor), "Debe faltar 1 compositor.");
    }

    @Test
    void dadoCancionConRolesFaltantes_cuandoSeContrataParcialmente_entoncesCancionQuedaCompletamenteCubierta() throws RolesNoCubiertos {
        recital.contratarParaCancion(cancionEuropa);

        Map<Rol, Integer> faltantes = recital.getRolesFaltantes(cancionEuropa);

        assertEquals(0, faltantes.size(), "No debería faltar ningún rol, ya que todos fueron cubiertos.");
    }

    @Test
    void dadoArtistasBaseDisponibles_cuandoSeContrataParaCancion_entoncesAsignaArtistasBase() throws RolesNoCubiertos {
        recital.contratarParaCancion(cancionEuropa);

        List<Asignacion> asignacionesEuropa = recital.getAsignacionesPorCancion(cancionEuropa);

        assertEquals(3, asignacionesEuropa.size(), "Deben haber 3 asignaciones exitosas.");

        assertTrue(asignacionesEuropa.stream()
                        .anyMatch(a -> a.getArtista().equals(base) && a.getRolAsignado().equals(rolGuitarra)),
                "Carlos Santana debe ser el asignado como guitarrista.");
    }

    @Test
    void dadoCancionConRolImposible_cuandoSeIntentaContratar_entoncesLanzaRolesNoCubiertos() {
        Cancion cancionImposible = new Cancion("Bad Song", Map.of(new Rol("Flautista"), 1));

        assertThrows(RolesNoCubiertos.class,
                () -> recital.contratarParaCancion(cancionImposible),
                "Debe lanzar excepción si ningún artista (Base o Externo) cubre el rol.");
    }
}
