package menu;

import modelo.Recital;

public class ListarEstadoCancionesComando implements Comando {
    private Recital recital;

    public ListarEstadoCancionesComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Listar el estado actual de las canciones";
    }
}
