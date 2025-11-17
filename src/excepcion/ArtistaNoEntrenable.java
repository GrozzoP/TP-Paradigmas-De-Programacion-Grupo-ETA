package excepcion;

/**
 * Excepción lanzada cuando se intenta entrenar a un artista
 * que no está habilitado para ser entrenado.
 * Construye la excepción con un mensaje explicativo.
 */
public class ArtistaNoEntrenable extends Exception {
    public ArtistaNoEntrenable(String mensaje) {
        super(mensaje);
    }
}
