package dto;

import java.util.List;

/**
 * DTO (Data Transfer Object) para representar la participación de 
 * un artista en una banda, junto con los roles que cumplió dentro de ella.
 * Representamos a las bandas de la siguiente manera, el nombre y los roles que tuvo en esa banda.
 */
public class BandaDTO {
    private String nombre;
    private List<String> roles;

    public BandaDTO() {
    }

    public BandaDTO(String nombre, List<String> roles) {
        this.nombre = nombre;
        this.roles = roles;
    }
    
    public String getNombre() {
        return nombre;
    }
    public List<String> getRoles() {
        return roles;
    }
}
