package modelo;

import java.util.*;

/**
 * Representa una canción y los roles que requiere (por ejemplo: voz principal, 1 guitarra, 1 bajo).
 * rolesRequeridos: Map(Rol, Integer) donde el entero indica cuántas instancias de ese rol se necesitan.
 * 
 * La clase NO mantiene asignaciones propias; recibe la lista de Asignacion desde el Recital
 * para calcular roles faltantes o costo. Esto reduce acoplamiento.
 */
public class Cancion {
    private final String titulo;
    private Map<Rol, Integer> rolesRequeridos;

    public Cancion(String titulo, Map<Rol, Integer> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = rolesRequeridos;
    }

    public String getTitulo() {
        return titulo;
    }
    
    /** 
     * Devuelve un mapa inmutable de roles requeridos 
     * para evitar modificaciones externas. 
     */
    public Map<Rol, Integer> getRolesRequeridos() {
        return Collections.unmodifiableMap(rolesRequeridos);
    }

    /**
     * Calcula los roles faltantes para esta canción, 
     * considerando las asignaciones que ya existen.
     * Resta 1 por cada asignación con el rol correspondiente.
     */
    public Map<Rol, Integer> getRolesFaltantes(List<Asignacion> asignaciones) {
        Map<Rol, Integer> rolesFaltantes = new HashMap<>(rolesRequeridos);

        for(Asignacion a : asignaciones) {
            if(a.getCancion().equals(this)) {
                Rol rol = a.getRolAsignado();
                if(rolesFaltantes.containsKey(rol)) {
                    int restantes = rolesFaltantes.get(rol) - 1;
                    if(restantes <= 0) {
                        rolesFaltantes.remove(rol);
                    }
                    else {
                        rolesFaltantes.put(rol, restantes);
                    }
                }
            }
        }

        return rolesFaltantes;
    }
    
    /**
     * Calcula el costo total de la canción sumando el costo final por canción 
     * de cada artista que participa en la misma (según las asignaciones provistas).
     */
    public double calcularCosto(Set<ArtistaBase> artistasBase, List<Asignacion> asignaciones) {
        Set<ArtistaBase> artistasEnCancion = new HashSet<>();
 
        for (Asignacion a : asignaciones) {
            if (a.getCancion().equals(this)) {
                artistasEnCancion.add(a.getArtista());
            }
        }
        return artistasEnCancion.stream()
                .mapToDouble(a -> a.getCostoFinal(artistasBase))
                .sum();
    }

    /**
     * Indica si un artista ya fue asignado 
     * a esta canción (según la lista de asignaciones).
     */
    public boolean artistaAsignadoCancion(ArtistaBase artista, List<Asignacion> asignaciones) {
        return asignaciones.stream()
                .anyMatch(a -> a.getCancion().equals(this) && a.getArtista().equals(artista));
    }

    @Override
    public String toString() {
        return "Cancion{" + "titulo=" + titulo + '}';
    }
    
}
