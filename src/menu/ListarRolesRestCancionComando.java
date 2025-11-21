package menu;

import modelo.Recital;

import java.util.Scanner;

public class ListarRolesRestCancionComando implements Comando, Descriptor {
    private Recital recital;
    private Scanner scanner;

    public ListarRolesRestCancionComando(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Listar los roles faltantes para UNA cancion";
    }
}
