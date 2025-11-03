package modelo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cancion {
    private String titulo;
    private Map<Rol, Integer> rolesRequeridos;

    public Cancion(String titulo, Map<Rol, Integer> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = rolesRequeridos;
    }

    public String getTitulo() {
        return titulo;
    }

    public Map<Rol, Integer> getRolesRequeridos() {
        return rolesRequeridos;
    }

    public Map<Rol, Integer> getRolesFaltantes(List<Asignacion> totales, Set<ArtistaBase> artistasBase) {
        return null;
    }
}
