package test;

import cargador_datos.CargadorDatos;
import excepcion.ArtistaNoEntrenable;
import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Asignacion;
import modelo.Cancion;
import modelo.Recital;
import modelo.Rol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Reglas según el modelo:
 *  - No se puede entrenar un artista externo que ya esté contratado
 *    (es decir, que tenga alguna Asignacion en el recital).
 *  - Si es entrenable y no está contratado, se entrena en el nuevo rol:
 *      * se agrega el rol a rolesAdquiridos
 *      * se incrementa su costoBase por INCREMENTO_ENTRENAMIENTO.
 *  - Si el rol es null, ArtistaExterno.entrenar lanza IllegalArgumentException.
 */
public class EntrenarArtistaTest {

    private Recital recital;
    private ArtistaExterno externo;
    private Rol rolNuevo;

    @BeforeEach
    void setUp() {
        //Artista externo a entrenar
        externo = new ArtistaExterno("Gary Moore", 1000.0, 10);

        // Rol en el que lo vamos a entrenar
        rolNuevo = new Rol("Teclado");


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
    }

    @Test
    void dadoArtistaExternoLibre_cuandoSeEntrenaConNuevoRol_entoncesAgregaRolYAumentaCosto() throws ArtistaNoEntrenable {
        double costoInicial = externo.getCostoBase();

        recital.entrenarArtista(externo, rolNuevo);

        //El rol nuevo debe estar en los roles adquiridos del artista
        assertTrue(externo.getRolesAdquiridos().contains(rolNuevo),
                "El rol entrenado debería agregarse a rolesAdquiridos del artista externo");

        //El costo base debe haberse multiplicado por INCREMENTO_ENTRENAMIENTO
        double costoEsperado = costoInicial * ArtistaExterno.INCREMENTO_ENTRENAMIENTO;
        assertEquals(costoEsperado, externo.getCostoBase(), 0.01,
                "El costo base del artista externo debería incrementarse por INCREMENTO_ENTRENAMIENTO");
    }

    @Test
    void dadoArtistaExternoContratado_cuandoSeIntentaEntrenar_entoncesLanzaArtistaNoEntrenable() {
        // Marcamos al artista como "contratado" agregándole una asignación cualquiera
        Rol rolGuitarra = new Rol("Guitarra");
        Cancion cancion = new Cancion("Song 1", Map.of(rolGuitarra, 1));

        Asignacion asignacion = new Asignacion(externo, rolGuitarra, cancion);
        recital.agregarAsignacion(asignacion);

        // Al intentar entrenarlo, debe lanzar ArtistaNoEntrenable
        assertThrows(ArtistaNoEntrenable.class,
                () -> recital.entrenarArtista(externo, rolNuevo),
                "No debería poder entrenarse un artista externo que ya está contratado en alguna canción");

        // Y no debería haberse agregado el rol nuevo
        assertFalse(externo.getRolesAdquiridos().contains(rolNuevo),
                "No se debe agregar el rol entrenado si el artista no es entrenable (ya contratado)");
    }

    @Test
    void dadoRolNulo_cuandoSeIntentaEntrenarArtista_entoncesLanzaIllegalArgumentException() {
        // No tiene asignaciones, así que pasa la validación de Recital, pero ArtistaExterno.entrenar(null) lanza IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> recital.entrenarArtista(externo, null),
                "Entrenar con rol null debe lanzar IllegalArgumentException desde ArtistaExterno.entrenar");
    }
}