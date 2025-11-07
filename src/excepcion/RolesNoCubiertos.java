package excepcion;

import java.util.List;
import modelo.Rol;

public class RolesNoCubiertos extends Exception {
private final List<Rol> rolesNoCubiertos;

    public RolesNoCubiertos(String mensaje, List<Rol> rolesNoCubiertos) {
        super(mensaje);
        this.rolesNoCubiertos = rolesNoCubiertos;
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
