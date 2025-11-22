package cargador_datos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ArtistaDTO;
import dto.BandaDTO;
import dto.EstadoRecitalDTO;
import dto.RecitalDTO;
import modelo.*;

import java.io.File;
import java.io.InputStream;
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
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String  RUTA_ARCHIVO_ARTISTAS = "data/artistas.json";
    public static final String  RUTA_ARCHIVO_ARTISTAS_DISCOGRAFICA = "data/artistas-discografica.json";
    public static final String  RUTA_ARCHIVO_RECITAL = "data/recital.json";
    public static final String RUTA_ARCHIVO_GUARDADO = "src/data/recital-out.json";

    /**
     * Obtiene el recurso como un InputStream usando el ClassLoader.
     * @param ruta La ruta relativa al classpath (ej. "data/archivo.json").
     * @return InputStream del recurso.
     * @throws RuntimeException si el archivo no se encuentra en el classpath.
     */
    private InputStream getResource(String ruta) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);

        if (is == null) {
            throw new RuntimeException("ERROR: No se pudo encontrar el recurso: " + ruta +
                    ". Asegúrate de que está en la carpeta 'resources'.");
        }
        return is;
    }

    // ----------------------------------------------------------------------

    /**
     * Carga todos los artistas desde el archivo seleccionado.
     *
     * @return lista de artistas convertidos a ArtistaBase o ArtistaExterno según correspondan.
     */
    public Set<ArtistaBase> cargarArtistas() throws Exception {
        try (InputStream isArtistas = getResource(RUTA_ARCHIVO_ARTISTAS)) {

            List<ArtistaDTO> artistasDTO = mapper.readValue(
                    isArtistas,
                    new TypeReference<List<ArtistaDTO>>() {}
            );

            Set<ArtistaBase> artistas = new HashSet<>();

            // Crear instancias concretas según si pertenecen o no a la discografica
            for (ArtistaDTO dto : artistasDTO) {
                ArtistaBase artista = new ArtistaBase(dto.getNombre(), dto.getCosto(), dto.getMaxCanciones());

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
    }

    /**
     * Carga canciones desde un archivo JSON que contiene una lista de RecitalDTO.
     *
     * @return conjunto de canciones del modelo completamente transformadas.
     */
    public Set<Cancion> cargarCanciones() throws Exception {
        try (InputStream isRecital = getResource(RUTA_ARCHIVO_RECITAL)) {

            List<RecitalDTO> cancionesDTO = mapper.readValue(
                    isRecital,
                    new TypeReference<List<RecitalDTO>>() {}
            );

            Set<Cancion> canciones = new HashSet<>();

            for (RecitalDTO dto : cancionesDTO) {
                Map<Rol, Integer> rolesContados = new HashMap<>();

                for (String nombreRol : dto.getRolesRequeridos()) {
                    Rol rol = new Rol(nombreRol);

                    if (rolesContados.containsKey(rol)) {
                        rolesContados.compute(rol, (k, actual) -> actual + 1);
                    } else {
                        rolesContados.put(rol, 1);
                    }
                }

                Cancion cancion = new Cancion(dto.getTitulo(), rolesContados);
                canciones.add(cancion);
            }

            return canciones;
        }
    }

    public Set<ArtistaBase> cargarSoloArtistasBase() throws Exception {
        Set<ArtistaBase> todos = cargarArtistas();

        try (InputStream isDiscografica = getResource(RUTA_ARCHIVO_ARTISTAS_DISCOGRAFICA)) {
            List<String> nombresBase = mapper.readValue(
                    isDiscografica,
                    new TypeReference<List<String>>() {}
            );

            Set<ArtistaBase> base = new HashSet<>();

            for (ArtistaBase a : todos) {
                if (nombresBase.contains(a.getNombre())) {
                    base.add(a);
                }
            }

            return base;
        }
    }

    public Set<ArtistaExterno> cargarSoloArtistasExternos() throws Exception {
        Set<ArtistaBase> artistas = cargarArtistas();

        try (InputStream isDiscografica = getResource(RUTA_ARCHIVO_ARTISTAS_DISCOGRAFICA)) {
            List<String> nombresBase = mapper.readValue(
                    isDiscografica,
                    new TypeReference<List<String>>() {}
            );

            Set<ArtistaExterno> externos = new HashSet<>();

            for (ArtistaBase a : artistas) {
                if (!nombresBase.contains(a.getNombre())) {
                    externos.add(new ArtistaExterno(a));
                }
            }

            return externos;
        }
    }

    public static void guardarEstado(List<EstadoRecitalDTO> dtoList) throws Exception {
        File file = new File(RUTA_ARCHIVO_GUARDADO);

        File direccionPadre = file.getParentFile();

        if (direccionPadre != null && !direccionPadre.exists()) {
            if (!direccionPadre.mkdirs()) {
                throw new java.io.IOException("Fallo al crear el directorio: " + direccionPadre.getAbsolutePath());
            }
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, dtoList);
    }

    public static List<EstadoRecitalDTO> cargarEstado() throws Exception {
        File file = new File(RUTA_ARCHIVO_GUARDADO);

        if (!file.exists()) {
            System.out.println("No se encontró el archivo de estado en: " + RUTA_ARCHIVO_GUARDADO);
            return new ArrayList<>();
        }

        return mapper.readValue(
                file,
                new TypeReference<List<EstadoRecitalDTO>>() {}
        );
    }
}