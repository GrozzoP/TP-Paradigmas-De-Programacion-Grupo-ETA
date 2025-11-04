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
        for(Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            if(artista instanceof ArtistaExterno) {
                contratados.add((ArtistaExterno) artista);
            }
        }
        return contratados;
    }

    public Map<Rol, Integer> getRolesFaltantes(Cancion c) {
        // Retorna los roles que aún no están cubiertos para la canción teniendo en cuenta las asignaciones hechas.
        return c.getRolesFaltantes(this.asignaciones);
    }
    
    public Map<Rol, Integer> getRolesFaltantesTotales() {
        // Devuelve los roles faltantes de todo el recital (de todas las canciones)
        Map<Rol, Integer> faltantesTotales = new HashMap<>();
        
        for(Cancion c : this.canciones){
            Map<Rol, Integer> faltantesCancion = c.getRolesFaltantes(this.asignaciones);
            
            for(Map.Entry<Rol, Integer> entry : faltantesCancion.entrySet()){
                // El merge() agrega el par <Rol,Integer> y sino suma las cantidades si ya existía ese rol en el mapa
                faltantesTotales.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }        
        }
        return faltantesTotales;
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
        Map<Rol, Integer> rolesFaltantes = c.getRolesFaltantes(this.asignaciones);
        
        for(Rol rol : new HashSet<>(rolesFaltantes.keySet())){
            for(ArtistaExterno artistaExt : candidatos){
                // Verificamos si el artista puede cubrir ese rol
                if(artistaExt.puedeCubrir(rol)){
                    
                    // Creamos una nueva asignacion
                    asignaciones.add(new Asignacion(artistaExt, rol, c));
                    
                    int restantes = rolesFaltantes.get(rol) - 1;
                    if(restantes <= 0) rolesFaltantes.remove(rol);
                    else rolesFaltantes.put(rol, restantes);
                    
                    // No tiene sentido seguir buscando más artistas para el mismo rol en esta iteración.
                    break;
                }
            }
        }
    }
    
    public void contratarParaRecitalCompleto(List<ArtistaExterno> candidatos) {
        // Asigna artistas externos a los roles faltantes en todas las canciones del recital
        for(Cancion c : this.canciones){
            contratarParaCancion(c, candidatos);
        }
    }

}
