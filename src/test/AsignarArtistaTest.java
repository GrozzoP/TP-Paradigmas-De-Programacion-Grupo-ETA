package test;

import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Banda;
import modelo.Cancion;
import modelo.Colaboracion;
import modelo.Rol;
import servicio.AsignacionServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de la lógica de asignación de artistas a roles en canciones.
 */
public class AsignarArtistaTest {

    private Rol rolGuitarra;
    private Rol rolBateria;

    private Banda bandaQueen;
    private Cancion cancion1;

    private ArtistaBase baseBrian;
    private ArtistaBase baseRoger;
    private ArtistaExterno externoGary;

    private AsignacionServicio asignacionServicio;

    @BeforeEach
    void setUp() {
        //Rol
        rolGuitarra = new Rol("Guitarra");
        rolBateria  = new Rol("Batería");

        //Banda y colaboraciones 
        bandaQueen = new Banda("Queen");

        Set<Rol> rolesGuitarra = Set.of(rolGuitarra);
        Set<Rol> rolesBateria  = Set.of(rolBateria);

        Colaboracion colBrian = new Colaboracion(bandaQueen, rolesGuitarra);
        Colaboracion colRoger = new Colaboracion(bandaQueen, rolesBateria);

        //Artistas base
        baseBrian = new ArtistaBase("Brian May", 0.0, 10);
        baseBrian.agregarColaboracion(colBrian);

        baseRoger = new ArtistaBase("Roger Taylor", 0.0, 10);
        baseRoger.agregarColaboracion(colRoger);

        //Artista externo
        externoGary = new ArtistaExterno("Gary Moore", 1000.0, 10);
        externoGary.agregarColaboracion(colBrian); // comparte banda → descuento


        cancion1 = new Cancion(
                "Song Test",
                Map.of(
                        rolGuitarra, 1,
                        rolBateria , 1
                )
        );

        asignacionServicio = new AsignacionServicio();
    }

    @Test
    void artistaNoEsElegibleSiNoPuedeCubrirRol() {
        Rol rolTeclados = new Rol("Teclados"); // Ningún artista lo cubre

        List<Asignacion> asignaciones = new ArrayList<>();
        Set<ArtistaBase> yaElegidos = new HashSet<>();

        boolean elegible = asignacionServicio.esArtistaElegibleParaRol(
                asignaciones,
                baseBrian,
                rolTeclados,
                yaElegidos
        );

        assertFalse(
                elegible,
                "El artista no puede cubrir el rol, por lo tanto no debe ser elegible."
        );
    }

    @Test
    void costoFinalConDescuentoCuandoExternoComparteBandaConBase() {

        Asignacion asignacionBase =
                new Asignacion(baseBrian, rolGuitarra, cancion1);

        List<Asignacion> asignaciones = List.of(asignacionBase);


        Asignacion asignacionExterno =
                new Asignacion(externoGary, rolBateria, cancion1);

        double costoCalculado = asignacionServicio.calcularCostoFinalPorAsignacion(
                externoGary,
                cancion1,
                asignaciones,
                Set.of(baseBrian, baseRoger)
        );

        double costoEsperado =
                externoGary.getCostoBase() *
                        (1 - ArtistaExterno.DESCUENTO_BANDA);

        assertEquals(
                costoEsperado,
                costoCalculado,
                0.01,
                "Debe aplicar descuento por compartir banda con artista base."
        );
    }

    @Test
    void costoTotalCancionEsSumaDeAsignacionesExterno() {

        Asignacion asigBase =
                new Asignacion(baseBrian, rolGuitarra, cancion1);

        // Externo en batería → tiene descuento
        Asignacion asigExterno =
                new Asignacion(externoGary, rolBateria, cancion1);

        List<Asignacion> asignaciones = List.of(asigBase, asigExterno);

        double costoCancion = asignacionServicio.calcularCostoTotalCancion(
                cancion1,
                asignaciones,
                Set.of(baseBrian, baseRoger)
        );

        double costoEsperado =
                asignacionServicio.calcularCostoFinalPorAsignacion(
                        externoGary,
                        cancion1,
                        asignaciones,
                        Set.of(baseBrian, baseRoger)
                );

        assertEquals(
                costoEsperado,
                costoCancion,
                0.01,
                "El costo de la canción debe ser la suma de los costos finales de artistas externos."
        );
    }
}