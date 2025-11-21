package menu;

import modelo.Recital;

public class ListarRolesRestRecitalComando implements Comando, Descriptor {
    private Recital recital;

    public ListarRolesRestRecitalComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Listar los roles faltantes para las canciones del recital";
    }
}
