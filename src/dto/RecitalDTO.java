package dto;

import java.util.List;

/**
 * DTO (Data Transfer Object) que representa una 
 * canción tal como se encuentra en el archivo JSON. 
 * Su único propósito es transportar datos desde JSON,
 * para luego convertirlos al modelo real (clase Cancion).
 */
public class RecitalDTO {
    private String titulo;
    private List<String> rolesRequeridos;

    public RecitalDTO() {
    }

    public RecitalDTO(String titulo, List<String> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = rolesRequeridos;
    }

    public String getTitulo() {
        return titulo;
    }
    public List<String> getRolesRequeridos() {
        return rolesRequeridos;
    }

}
