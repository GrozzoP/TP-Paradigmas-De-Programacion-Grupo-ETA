package test;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArtistaTest {
    // ARTISTAS
    private ArtistaBase artistaBase;
    private ArtistaExterno artistaExterno;
    private ArtistaExterno artistaExternoSinRoles;

    // ROLES
    private Rol rolGuitarrista;
    private Rol rolBajista;
    private Rol rolBaterista;
    private Rol rolCompositor;

    // COLABORACIONES & BANDA
    private Banda bandaQueen;
    private Colaboracion colaboracionQueen;

    @BeforeEach
    void setUp() {
        // Roles
        rolGuitarrista = new Rol("Guitarrista");
        rolBaterista = new Rol("Baterista");
        rolBajista = new Rol("Bajista");
        rolCompositor = new Rol("Compositor");

        // Colaboraciones
        bandaQueen = new Banda("Queen");
        Set<Rol> rolesSantana = Set.of(rolGuitarrista, rolCompositor);
        colaboracionQueen = new Colaboracion(bandaQueen, rolesSantana);

        // --- ARTISTAS ---
        // Artista Base
        artistaBase = new ArtistaBase("Freedy Mercury", 0.0, 5);
        artistaBase.agregarColaboracion(colaboracionQueen);

        // Artista Externo
        artistaExterno = new ArtistaExterno("David Bowie", 10000.0, 7);
        artistaExternoSinRoles = new ArtistaExterno("Saje", 5000.0, 3);
    }

    @Test
    void dadoArtistaConHistorial_cuandoSeVerificaRolHistorico_entoncesRetornaTrue() {
        assertTrue(artistaBase.puedeCubrir(rolGuitarrista),
                "El artista base puede cubrir el rol de guitarrista al estar en su historial de colaboraciones");
    }

    @Test
    void dadoArtistasQueCompartenBanda_cuandoSeVerificaColaboracion_entoncesRetornaTrue() {
        ArtistaBase otroArtista = new ArtistaBase("Otro Musico", 1000.0, 5);

        Set<Rol> rolesOtro = Set.of(rolBajista);
        Colaboracion colOtro = new Colaboracion(bandaQueen, rolesOtro);
        otroArtista.agregarColaboracion(colOtro);

        assertTrue(artistaBase.comparteBanda(otroArtista),
                "Deberían compartir banda si tienen la misma banda en su historial");
    }

    @Test
    void dadoDosArtistasMismoNombre_cuandoSeCompara_entoncesRetornaTrue() {
        ArtistaBase artistaDuplicado = new ArtistaBase("Freedy Mercury", 0.0, 15);

        assertEquals(artistaBase, artistaDuplicado, "Dos artistas son iguales si tienen el mismo nombre");
    }

    @Test
    void dadoArtistaExternoEntrenadoUnaVez_cuandoSeEntrenaNuevamente_entoncesCostoAumentaMultiplicativamente() {
        double costoInicial = artistaExternoSinRoles.getCostoBase();
        double costoEsperado = costoInicial * Math.pow(ArtistaExterno.INCREMENTO_ENTRENAMIENTO, 2);

        artistaExternoSinRoles.entrenar(rolBajista);
        artistaExternoSinRoles.entrenar(rolBaterista);

        assertEquals(costoEsperado, artistaExternoSinRoles.getCostoBase(), 0.001,
                "El costo debe aumentar en un 50% con cada entrenamiento!");
    }
}
