package cargador_datos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ArtistaDTO;
import dto.BandaDTO;
import dto.CancionDTO;
import modelo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase utilitaria encargada de cargar artistas desde dos archivos JSON.
 * Esta clase sólo se ocupa de transformar JSON → objetos del modelo.
 */
public class CargadorDatos {
    
    /** Objeto Jackson usado para parsear JSON. */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Carga todos los artistas desde el archivo seleccionado.
     *
     * @param rutaArtistas ruta al archivo artistas.json (contiene datos completos de cada artista).
     * @param rutaDiscografica ruta a artistas-discografica.json (contiene lista con nombres de artistas "base").
     * @return lista de artistas convertidos a ArtistaBase o ArtistaExterno según correspondan.
     */
    public static List<ArtistaBase> cargarArtistas(String rutaArtistas, String rutaDiscografica) throws Exception {
        File fileArtistas = new File(rutaArtistas);
        File fileArtistasDiscografica = new File(rutaDiscografica); // lo corregi, antes decia rutaArtistas

        List<ArtistaDTO> artistasDTO = mapper.readValue(
                fileArtistas, 
                new TypeReference<List<ArtistaDTO>>() {}
        );

        List<String> artistasDiscografica = mapper.readValue(
                fileArtistasDiscografica,
                new TypeReference<List<String>>() {}
        );

        List<ArtistaBase> artistas = new ArrayList<>();

        // Crear instancias concretas según si pertenecen o no a la discografica
        for (ArtistaDTO dto : artistasDTO) {
            ArtistaBase artista = null;
            boolean esBase = artistasDiscografica.contains(dto.getNombre());

            if (esBase) {
                artista = new ArtistaBase(dto.getNombre(), dto.getCosto(), dto.getMaxCanciones());
            } else {
                artista = new ArtistaExterno(dto.getNombre(), dto.getCosto(), dto.getMaxCanciones());
            }

            // Agregar colaboraciones (bandas + roles)
            for (BandaDTO bandaDTO : dto.getBandas()) {
                Set<Rol> roles = new HashSet<>();
                for (String r : bandaDTO.getRoles()) {
                    roles.add(new Rol(r));
                }
                Banda banda = new Banda(bandaDTO.getNombre());
                artista.agregarColaboracion(new Colaboracion(banda, roles));
            }

            artistas.add(artista);
        }

        return artistas;
    }

    /**
     * Carga canciones desde un archivo JSON que contiene una lista de CancionDTO.
     * 
     * Convierte la lista de roles repetidos del JSON en un Map<Rol, Integer>, que la clase
     * Cancion necesita para saber cuántas veces se requiere cada rol.
     *
     * @param rutaRecital ruta al JSON con las canciones.
     * @return conjunto de canciones del modelo completamente transformadas.
     */
    public static Set<Cancion> cargarCanciones(String rutaRecital) throws Exception {
        File fileRecital = new File(rutaRecital);

        List<CancionDTO> cancionesDTO = mapper.readValue(
                fileRecital,
                new TypeReference<List<CancionDTO>>() {}
        );

        Set<Cancion> canciones = new HashSet<>();
        
        for (CancionDTO dto : cancionesDTO) {
            
            // Mapa de roles → cantidad requerida (cuenta repeticiones)
            Map<Rol, Integer> rolesContados = new HashMap<>();
  
            for (String nombreRol : dto.getRolesRequeridos()) {
                Rol rol = new Rol(nombreRol);

                // Contabiliza roles duplicados
                if (rolesContados.containsKey(rol)) {
                    int actual = rolesContados.get(rol);
                    rolesContados.put(rol, actual + 1);
                } else {
                    rolesContados.put(rol, 1);
                }
            }
            
            // Crea la Cancion del modelo con título + mapa de roles requeridos
            Cancion cancion = new Cancion(dto.getTitulo(), rolesContados);
            canciones.add(cancion);
        }

        return canciones;
    }

    /**
     * Carga un subconjunto de artistas marcados como "incluidos" desde un JSON.
     * 
     * Este JSON contiene simplemente una lista de nombres de artistas que deben
     * formar parte del recital.
     *
     * @param rutaArtistasIncluidos JSON con lista de nombres.
     * @param artistasCargados lista general de todos los artistas ya cargados previamente.
     * @return conjunto de ArtistaBase filtrados según la lista de nombres.
     */
    public static Set<ArtistaBase> cargarArtistasIncluidos(
            String rutaArtistasIncluidos,
            List<ArtistaBase> artistasCargados) throws Exception {

        File fileIncluidos = new File(rutaArtistasIncluidos);

        // Lista simple de nombres
        List<String> nombresIncluidos = mapper.readValue(
                fileIncluidos,
                new TypeReference<List<String>>() {}
        );

        Set<ArtistaBase> incluidos = new HashSet<>();

        // Selecciona sólo los artistas presentes en ambas estructuras
        for (ArtistaBase artista : artistasCargados) {
            if (nombresIncluidos.contains(artista.getNombre())) {
                incluidos.add(artista);
            }
        }

        return incluidos;
    }
}
