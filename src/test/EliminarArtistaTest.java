package test;

import cargador_datos.CargadorDatos;
import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EliminarArtistaTest {

    private Recital recital;
    private Cancion c1, c2;
    private ArtistaBase brian;
    private ArtistaBase roger;
    private Rol guitarra;
    private Rol bateria;

    @BeforeEach
    void setUp() {
        guitarra = new Rol("Guitarra");
        bateria  = new Rol("Batería");

        c1 = new Cancion("Song 1", Map.of(guitarra, 1, bateria, 1));
        c2 = new Cancion("Song 2", Map.of(guitarra, 1));

        brian = new ArtistaBase("Brian May", 0.0, 10);
        roger = new ArtistaBase("Roger Taylor", 0.0, 10);

        CargadorDatos dummy = new CargadorDatos() {
            @Override
            public Set<Cancion> cargarCanciones() {
                return new HashSet<>();
            }

            @Override
            public Set<ArtistaBase> cargarSoloArtistasBase() {
                return new HashSet<>();
            }

            @Override
            public Set<ArtistaExterno> cargarSoloArtistasExternos() {
                return new HashSet<>();
            }
        };

        try {
            recital = new Recital(dummy);
        } catch (Exception e) {
            fail("No debería lanzar excepción al crear el recital para testing");
        }

        recital.agregarCancion(c1);
        recital.agregarCancion(c2);
        recital.agregarArtistaBase(brian);
        recital.agregarArtistaBase(roger);

        recital.agregarAsignacion(new Asignacion(brian, guitarra, c1));
        recital.agregarAsignacion(new Asignacion(brian, guitarra, c2));
        recital.agregarAsignacion(new Asignacion(roger, bateria, c1));
    }

    @Test
    void dadoArtistaConMultiplesAsignaciones_cuandoSeQuitaArtista_entoncesEliminaTodasAsignaciones() {
        int eliminadas = recital.quitarArtista(brian);

        assertEquals(2, eliminadas,
                "Debería eliminar todas las asignaciones de Brian (2 en total)");

        // Brian no debe aparecer en ninguna asignación
        assertTrue(
                recital.getAsignaciones().stream()
                        .noneMatch(a -> a.getArtista().equals(brian)),
                "No debería quedar ninguna asignación del artista eliminado"
        );

        // Solo debería quedar la asignación de Roger
        assertEquals(1, recital.getAsignaciones().size());
    }

    @Test
    void dadoArtistaSinAsignaciones_cuandoSeQuitaArtista_entoncesListaGlobalNoSeModifica() {
        ArtistaBase otro = new ArtistaBase("Artista X", 0.0, 5);

        int antes = recital.getAsignaciones().size();
        int eliminadas = recital.quitarArtista(otro);

        assertEquals(0, eliminadas,
                "Si el artista no tenía asignaciones, el resultado debe ser 0");
        assertEquals(antes, recital.getAsignaciones().size(),
                "La cantidad de asignaciones no debe cambiar");
    }

    @Test
    void dadoArtistaNulo_cuandoSeIntentaQuitar_entoncesRetornaErrorYNoModificaAsignaciones() {
        int antes = recital.getAsignaciones().size();

        int eliminadas = recital.quitarArtista(null);

        assertEquals(-1, eliminadas,
                "Si el artista es null, el servicio debe devolver -1");
        assertEquals(antes, recital.getAsignaciones().size(),
                "No debe modificarse la lista de asignaciones cuando el artista es null");
    }
}