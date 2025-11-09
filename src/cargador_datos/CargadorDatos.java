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

public class CargadorDatos {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ArtistaBase> cargarArtistas(String rutaArtistas, String rutaDiscografica) throws Exception {
        // Archivos basicos para abrir ambos .json
        File fileArtistas = new File(rutaArtistas);
        File fileArtistasDiscografica = new File(rutaArtistas);

        // Leo el archivo artistas.json
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
}
