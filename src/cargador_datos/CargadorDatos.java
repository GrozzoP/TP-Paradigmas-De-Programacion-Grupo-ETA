package cargador_datos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ArtistaDTO;
import dto.BandaDTO;
import modelo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clase utilitaria encargada de cargar artistas desde dos archivos JSON.
 *  - artistas.json : información completa de cada artista.
 *  - artistas-discografica.json : lista de nombres que pertenecen a la discográfica.
 * 
 * Convierte los DTO leídos en objetos del modelo (ArtistaBase / ArtistaExterno).
 * Según el archivo de discográfica, decide si un artista es base o externo.
 */
public class CargadorDatos {
    
    /** Mapper para deserializar JSON. */
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ArtistaBase> cargarArtistas(String rutaArtistas, String rutaDiscografica) throws Exception {
        
        // Archivos JSON
        File fileArtistas = new File(rutaArtistas);
        File fileArtistasDiscografica = new File(rutaDiscografica); // lo corregi, antes decia rutaArtistas

        // Leer el archivo artistas.json
        List<ArtistaDTO> artistasDTO = mapper.readValue(
                fileArtistas,
                new TypeReference<List<ArtistaDTO>>() {}
        );
        
        // Leer el archivo artistas-discografica.json
        List<String> artistasDiscografica = mapper.readValue(
                fileArtistasDiscografica,
                new TypeReference<List<String>>() {}
        );
        
        List<ArtistaBase> artistas = new ArrayList<>();

        // Construir los artistas reales del modelo
        for (ArtistaDTO dto : artistasDTO) {
            
            // Decidir tipo concreto de artista
            ArtistaBase artista = null;
            boolean esBase = artistasDiscografica.contains(dto.getNombre());
            if (esBase) {
                artista = new ArtistaBase(dto.getNombre(), dto.getCosto(), dto.getMaxCanciones());
            } else {
                artista = new ArtistaExterno(dto.getNombre(), dto.getCosto(), dto.getMaxCanciones());
            }

            // Cargar colaboraciones (bandas + roles desempeñados)
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
