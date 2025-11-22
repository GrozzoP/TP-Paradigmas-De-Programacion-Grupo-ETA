package dto;

import modelo.Asignacion;

public class EstadoRecitalDTO {
    private String artista;
    private double costo;
    private String cancion;
    private String rol;

    public EstadoRecitalDTO(String artista, double costo, String cancion, String rol) {
        this.artista = artista;
        this.costo = costo;
        this.cancion = cancion;
        this.rol = rol;
    }

    public EstadoRecitalDTO() {

    }

    public EstadoRecitalDTO(Asignacion asignacion, double costoAplicado) {
        this.artista = asignacion.getArtista().getNombre();
        this.costo = costoAplicado;
        this.cancion = asignacion.getCancion().getTitulo();
        this.rol = asignacion.getRolAsignado().getNombre();
    }

    public String getArtista() {
        return artista;
    }

    public double getCosto() {
        return costo;
    }

    public String getCancion() {
        return cancion;
    }

    public String getRol() {
        return rol;
    }
}
