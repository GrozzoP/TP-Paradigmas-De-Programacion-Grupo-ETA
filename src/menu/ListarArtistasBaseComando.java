package menu;

import modelo.ArtistaBase;
import modelo.Recital;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListarArtistasBaseComando implements Comando, Descriptor {
    private Recital recital;

    public ListarArtistasBaseComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        Set<ArtistaBase> artistasBase = new HashSet<>();
        artistasBase.addAll(recital.getArtistasBase());

        for(ArtistaBase artistaBase : artistasBase) {
            System.out.println(artistaBase);
        }
    }

    @Override
    public String getDescripcion() {
        return "Listar artistas base (pertenecientes a la discografica)";
    }
}
