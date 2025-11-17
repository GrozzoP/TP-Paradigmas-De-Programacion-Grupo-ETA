package modelo;

import java.util.Set;

/**
 * Representa la participación de un artista en una banda 
 * junto con los roles que ocupó allí. (Banda + roles).
 */
public class Colaboracion {
    private Banda banda;
    private Set<Rol> rolesOcupados;

    public Colaboracion(Banda banda, Set<Rol> rolesOcupados) {
        this.banda = banda;
        this.rolesOcupados = rolesOcupados;
    }

    public Banda getBanda() {
        return banda;
    }
    public Set<Rol> getRolesOcupados() {
        return rolesOcupados;
    }

    @Override
    public String toString() {
        return "Colaboracion{" + banda + ", roles=" + rolesOcupados + "}";
    }
}
