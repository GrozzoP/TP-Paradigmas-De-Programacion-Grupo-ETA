package modelo;

import java.util.*;

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

    public Map<Rol, Integer> getRolesRequeridos() {
        return Collections.unmodifiableMap(rolesRequeridos);
    }

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

    public boolean artistaAsignadoCancion(ArtistaBase artista, List<Asignacion> asignaciones) {
        return asignaciones.stream()
                .anyMatch(a -> a.getCancion().equals(this) && a.getArtista().equals(artista));
    }

    @Override
    public String toString() {
        return "Cancion{" + "titulo=" + titulo + '}';
    }
    
}
