package excepcion;

import modelo.ArtistaBase;

public class ArtistaNoEntrenable extends Exception {
    public ArtistaNoEntrenable(String mensaje) {
        super(mensaje);
    }
}
