package modelo;

import java.util.Objects;

/**
 * Representa una banda/colaboración.
 * Igualdad basada en nombre.
 */
public class Banda {
    private final String nombre;

    public Banda(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Banda banda = (Banda) o;
        return Objects.equals(nombre, banda.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }

    @Override
    public String toString() {
        return "Banda{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
