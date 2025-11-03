package modelo;

public class Asignacion {
    private ArtistaBase artista;
    private Rol rolAsignado;
    private Cancion cancion;

    public Asignacion(ArtistaBase artista, Rol rolAsignado, Cancion cancion) {
        this.artista = artista;
        this.rolAsignado = rolAsignado;
        this.cancion = cancion;
    }

    public ArtistaBase getArtista() {
        return artista;
    }

    public Rol getRolAsignado() {
        return rolAsignado;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public double getCostoEfectivo() {
        return 0.0;
    }
}
