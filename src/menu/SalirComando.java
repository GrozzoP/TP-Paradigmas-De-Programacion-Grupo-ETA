package menu;

import modelo.Recital;

public class SalirComando implements Comando, Descriptor{
    private Recital recital;
    private Menu menu;

    public SalirComando(Recital recital, Menu menu) {
        this.recital = recital;
        this.menu = menu;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "Salir del programa";
    }
}
