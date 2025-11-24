package test;

import dto.EstadoRecitalDTO;
import modelo.*;
import cargador_datos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CargarEstadoTest {

    private Recital recital;
    private Cancion c1, c2;
    private ArtistaBase brian;
    private ArtistaExterno gary;
    private Rol guitarra, bateria;

    @BeforeEach
    void setUp() {
        guitarra = new Rol("Guitarra");
        bateria  = new Rol("Batería");

        c1 = new Cancion("Song 1", Map.of(guitarra, 1));
        c2 = new Cancion("Song 2", Map.of(bateria, 1));

        brian = new ArtistaBase("Brian May", 0.0, 10);
        gary  = new ArtistaExterno("Gary Moore", 1000.0, 10);

        //CargadorDatos Dummy para que no lance excepcion 
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
            recital.agregarCancion(c1);
            recital.agregarCancion(c2);
            recital.agregarArtistaBase(brian);
            recital.getArtistaExternos().add(gary);

        } catch (Exception e) {
            fail("No debería lanzar excepción al crear el recital para testing");
        }
    }

    @Test
    void dadoEstadoValido_cuandoSeCargaEstadoPrevio_entoncesGeneraAsignacionesCorrectas() {
        List<EstadoRecitalDTO> estado = List.of(
                new EstadoRecitalDTO("Brian May", 0.0, "Song 1", "Guitarra"),
                new EstadoRecitalDTO("Gary Moore", 1000.0, "Song 2", "Batería")
        );

        recital.cargarEstadoPrevio(estado);

        var asignaciones = recital.getAsignaciones();

        assertEquals(2, asignaciones.size());

        Asignacion a1 = asignaciones.get(0);
        assertEquals("Brian May", a1.getArtista().getNombre());
        assertEquals("Song 1", a1.getCancion().getTitulo());
        assertEquals("Guitarra", a1.getRolAsignado().getNombre());

        Asignacion a2 = asignaciones.get(1);
        assertEquals("Gary Moore", a2.getArtista().getNombre());
        assertEquals("Song 2", a2.getCancion().getTitulo());
        assertEquals("Batería", a2.getRolAsignado().getNombre());
    }

    @Test
    void dadoEstadoConDatosInvalidos_cuandoSeCarga_entoncesIgnoraAsignacionesSinModelo() {
        List<EstadoRecitalDTO> estado = List.of(
                new EstadoRecitalDTO("Brian May", 0.0, "Song 1", "Guitarra"),
                new EstadoRecitalDTO("ArtistaInexistente", 0.0, "Song 1", "Guitarra"),
                new EstadoRecitalDTO("Gary Moore", 0.0, "CancionInvalida", "Batería")
        );

        recital.cargarEstadoPrevio(estado);

        var asignaciones = recital.getAsignaciones();

        assertEquals(1, asignaciones.size());
        assertEquals("Brian May", asignaciones.get(0).getArtista().getNombre());
    }

    @Test
    void dadoAsignacionesExistentes_cuandoSeCargaNuevoEstado_entoncesAsignacionesPreviasSeVacian() {
        recital.cargarEstadoPrevio(List.of(
                new EstadoRecitalDTO("Brian May", 0.0, "Song 1", "Guitarra")
        ));

        assertEquals(1, recital.getAsignaciones().size());

        recital.cargarEstadoPrevio(List.of(
                new EstadoRecitalDTO("Gary Moore", 0.0, "Song 2", "Batería")
        ));

        var asignaciones = recital.getAsignaciones();

        assertEquals(1, asignaciones.size());
        assertEquals("Gary Moore", asignaciones.get(0).getArtista().getNombre());
        assertEquals("Song 2", asignaciones.get(0).getCancion().getTitulo());
    }
}