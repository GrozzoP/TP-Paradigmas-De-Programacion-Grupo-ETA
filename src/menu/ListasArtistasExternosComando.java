package menu;

import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Recital;

import java.util.HashSet;
import java.util.Set;

public class ListasArtistasExternosComando implements Comando {
    private Recital recital;

    public ListasArtistasExternosComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        Set<ArtistaExterno> artistasExterno = new HashSet<>();
        artistasExterno.addAll(recital.getArtistaExternos());

        for(ArtistaExterno artistaExterno : artistasExterno) {
            System.out.println(artistaExterno);
        }
    }

    @Override
    public String getDescripcion() {
        return "Listar todos los artistas externos";
    }
}
