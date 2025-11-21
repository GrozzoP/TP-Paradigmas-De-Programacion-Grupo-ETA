package menu;

import modelo.Recital;

public class ContratarArtistasRecitalComando implements Comando, Descriptor {
    private Recital recital;

    public ContratarArtistasRecitalComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Contratar a TODOS los artistas necesarios para el recital";
    }
}
