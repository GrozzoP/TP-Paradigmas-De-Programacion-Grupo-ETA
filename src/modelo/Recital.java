package modelo;

import java.util.*;

public class Recital {
    private List<Cancion> canciones = new ArrayList<>();
    private Set<ArtistaBase> artistasBase = new HashSet<>();
    private List<Asignacion> asignaciones = new ArrayList<>();

    public void agregarCancion(Cancion c) {
        canciones.add(c);
    }

    public void agregarArtistaBase(ArtistaBase a) {
        artistasBase.add(a);
    }

    public void agregarAsignacion(Asignacion a) {
        asignaciones.add(a);
    }

    public boolean removerAsignacion(Asignacion a) {
        return asignaciones.remove(a);
    }

    public Set<ArtistaExterno> getArtistasContratados() {
        Set<ArtistaExterno> contratados = new HashSet<>();
        for (Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            if (artista instanceof ArtistaExterno) {
                contratados.add((ArtistaExterno) artista);
            }
        }
        return contratados;
    }

    public Map<Rol, Integer> getRolesFaltantes(Cancion c) {
        // Retorna roles que aún no están cubiertos para la canción
        return null;
    }

    public Map<Rol, Integer> getRolesFaltantesTotales() {
        // Devuelve los roles faltantes de todo el recital
        return null;
    }

    public void contratarParaRecitalCompleto(List<ArtistaExterno> candidatos) {
        // Asigna artistas externos a los roles faltantes en todas las canciones
    }

    public void entrenarArtista(ArtistaExterno artista, Rol nuevoRol) {
        // Permite entrenar a un artista externo en un nuevo rol
        if(artista.puedeEntrenarse()){
            artista.entrenar(nuevoRol);
        }
        else{
            System.out.println("El artista " + artista + " ya no puede entrenarse en más roles");
        }
    }

    public void contratarParaCancion(Cancion c, List<ArtistaExterno> candidatos) {
        // Asigna artistas externos faltantes solo para una canción específica
    }
}
