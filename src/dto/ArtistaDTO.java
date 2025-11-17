package dto;

import java.util.List;

/** 
 * DTO utilizado únicamente para importar artistas desde JSON.
 * Solo se usa para importar los datos del JSON, porque los artistas externos y base
 * vienen en un mismo .json, despues hay que diferenciarlos por el archivo de artistas-discografica.json
 */
public class ArtistaDTO {
    private String nombre;
    private List<BandaDTO> bandas;
    private double costo;
    private int maxCanciones;

    public ArtistaDTO(String nombre, List<BandaDTO> bandas, double costo, int maxCanciones) {
        this.nombre = nombre;
        this.bandas = bandas;
        this.costo = costo;
        this.maxCanciones = maxCanciones;
    }

    public String getNombre() {
        return nombre;
    }

    public List<BandaDTO> getBandas() {
        return bandas;
    }

    public double getCosto() {
        return costo;
    }

    public int getMaxCanciones() {
        return maxCanciones;
    }
}
