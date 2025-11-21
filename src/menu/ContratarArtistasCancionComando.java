package menu;

import modelo.Recital;

import java.util.Scanner;

public class ContratarArtistasCancionComando implements Comando, Descriptor {
    private Recital recital;
    private Scanner scanner;

    public ContratarArtistasCancionComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Contratar artistas para UNA cancion";
    }
}
