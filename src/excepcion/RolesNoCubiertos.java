package excepcion;

import java.util.ArrayList;
import java.util.List;
import modelo.Rol;

public class RolesNoCubiertos extends Exception {
private List<Rol> rolesNoCubiertos = new ArrayList<>();

    public RolesNoCubiertos(String mensaje, List<Rol> rolesNoCubiertos) {
        super(mensaje);
        this.rolesNoCubiertos = rolesNoCubiertos;
    }

    public RolesNoCubiertos(String mensaje, Rol rol) {
        super(mensaje);
        this.rolesNoCubiertos.add(rol);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        sb.append("\n Los siguientes roles no fueron cubiertos:\n");

        for(Rol rol : rolesNoCubiertos) {
            sb.append("- ").append(rol.getNombre()).append("\n");
        }

        return sb.toString();
    }
}
