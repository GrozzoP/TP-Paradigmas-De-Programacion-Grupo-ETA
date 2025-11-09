package dto;

import java.util.List;

/*
Vamos a representar a las bandas de la siguiente manera, el nombre y los roles que tuvo en esa banda,
para cumplir mejor con nuestro diseño segun lo que le gustaba al profe
 */
public class BandaDTO {
    private String nombre;
    private List<String> roles;

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
