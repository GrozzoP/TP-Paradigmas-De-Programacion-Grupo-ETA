package menu;

import modelo.Recital;

public class InteraccionPrologComando implements Comando, Descriptor {
    private Recital recital;

    public InteraccionPrologComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {

    }

    @Override
    public String getDescripcion() {
        return "(PROLOG) Retornar cantidad de entrenamientos mínimos";
    }
}
