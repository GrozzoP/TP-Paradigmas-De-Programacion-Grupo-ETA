package modelo;

import java.util.HashSet;
import java.util.Set;

public class ArtistaExterno extends ArtistaBase {
    private Set<Rol> rolesAdquiridos;

    public ArtistaExterno(String nombre, double costoBase, int maxCanciones) {
        super(nombre, costoBase, maxCanciones);
        this.rolesAdquiridos = new HashSet<>();
    }

    public Set<Rol> getRolesAdquiridos() {
        return rolesAdquiridos;
    }

    public boolean puedeEntrenarse() {
        return rolesAdquiridos.size() < 3;
    }
    
    /**
     * Abro hilo...
     * Entrena un nuevo rol: sólo agrega el rol a rolesAdquiridos.
     * NO multiplicamos costoBase aca para evitar acumulaciones dobles.
     * La subida de costo se aplica en getCostoFinal() con la fórmula (1.5 ^ n).
     */
    public void entrenar(Rol rol) {
        if(puedeEntrenarse()) {
            rolesAdquiridos.add(rol);
        }
    }
    
    /** 
     * Abro hilo...
     * Cálculo del costo final para un artista externo:
     * 1) Se toma el costo base.
     * 2) Se aplica el incremento por entrenamientos: por cada rol entrenado,
     *    el costo se multiplica por 1.5 (es decir: costoBase * 1.5 ^ n).
     * 3) Si comparte banda con AL MENOS UN artista base (del set pasado),
     *    se aplica un descuento del 50% (multiplicador 0.5).
     *
     * Nota: aplico el descuento después del incremento por entrenamientos.
     */
    @Override
    public double getCostoFinal(Set<ArtistaBase> artistasBase) {
       //Incremento por entrenamientos
       int nEntrenamientos = rolesAdquiridos.size();
       double costoConEntrenamientos = costoBase * Math.pow(1.5, nEntrenamientos);
       
       //Descuento por compartir banda con algún artista base ()
       boolean comparte = (artistasBase != null) && (artistasBase.stream().anyMatch(base -> this.comparteBanda(base)));
       if(comparte){
            return costoConEntrenamientos * 0.5;
       }else{
            return costoConEntrenamientos;
       }
    }
    
}
