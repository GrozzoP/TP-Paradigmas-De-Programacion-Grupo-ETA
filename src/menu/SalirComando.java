package menu;

import modelo.Recital;

public class SalirComando implements Comando {
    private Recital recital;
    private Menu menu;

    public SalirComando(Recital recital, Menu menu) {
        this.recital = recital;
        this.menu = menu;
    }

    @Override
    public void ejecutar() {
        this.menu.setEstaEnEjecucion(false);
    }

    @Override
    public String getDescripcion() {
        return "Salir del programa";
    }
}
