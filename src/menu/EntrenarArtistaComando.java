package menu;

import modelo.Recital;

import java.util.Scanner;

public class EntrenarArtistaComando implements Comando, Descriptor {
    private Recital recital;
    private Scanner scanner;

    public EntrenarArtistaComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Entrenar un artista capacitado en un rol determinado";
    }
}
