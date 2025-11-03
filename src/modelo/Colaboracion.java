package modelo;

import java.util.Set;

public class Colaboracion {
    private Banda banda;
    private Set<Rol> rolesOcupados;

    public Colaboracion(Banda banda, Set<Rol> rolesOcupados) {
        this.banda = banda;
        this.rolesOcupados = rolesOcupados;
    }

    public Set<Rol> getRolesOcupados() {
        return rolesOcupados;
    }

}
