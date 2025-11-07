package modelo;

import java.util.*;

import excepcion.ArtistaNoEntrenable;
import excepcion.RolesNoCubiertos;

public class Recital {
    private Set<Cancion> canciones = new HashSet<>();
    private Set<ArtistaBase> artistasBase = new HashSet<>();
    private List<Asignacion> asignaciones = new ArrayList<>();

    public void agregarCancion(Cancion c) {
        canciones.add(c);
    }

    public void agregarArtistaBase(ArtistaBase a) {
        artistasBase.add(a);
    }

    public void agregarAsignacion(Asignacion a) {
        asignaciones.add(a);
    }

    public boolean removerAsignacion(Asignacion a) {
        return asignaciones.remove(a);
    }

    public Set<ArtistaExterno> getArtistasContratados() {
        Set<ArtistaExterno> contratados = new HashSet<>();
        for(Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            if(artista.esContratable()) {
                contratados.add((ArtistaExterno) artista);
            }
        }
        return contratados;
    }

    // ¿Qué roles (con cantidad) me faltan para tocar una canción X del recital?
    public Map<Rol, Integer> getRolesFaltantes(Cancion c) {
        return c.getRolesFaltantes(this.asignaciones);
    }

    // ¿Qué roles (con cantidad) me faltan para tocar todas las canciones?
    public Map<Rol, Integer> getRolesFaltantesTotales() {
        Map<Rol, Integer> faltantesTotales = new HashMap<>();
        
        for(Cancion c : this.canciones){
            Map<Rol, Integer> faltantesCancion = c.getRolesFaltantes(this.asignaciones);
            
            for(Map.Entry<Rol, Integer> entry : faltantesCancion.entrySet()) {
                faltantesTotales.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }        
        }

        return faltantesTotales;
    }
    
    /**
     * Abro hilo...
     * La regla dice: No se puede entrenar un artista ya contratado para alguna canción. 
     */
    public void entrenarArtista(ArtistaExterno artista, Rol nuevoRol) throws ArtistaNoEntrenable {
        boolean estaContratado = asignaciones.stream().anyMatch(a -> a.getArtista().equals(artista));
        
        // TODO: Crear excepcion
        if (estaContratado) {
            throw new ArtistaNoEntrenable("El artista " + artista.getNombre() + " no es entrenable!");
        }
        if(artista.puedeEntrenarse()){
            artista.entrenar(nuevoRol);
        } else{
            System.out.println("El artista: " + artista + " ya no puede entrenarse en más roles");
        }
    }
    
    public void contratarParaCancion(Cancion c, List<ArtistaExterno> candidatos) throws RolesNoCubiertos {
        Map<Rol, Integer> rolesFaltantes = c.getRolesFaltantes(this.asignaciones);

        if(rolesFaltantes.isEmpty()) {
            System.out.println("Los roles para la cancion " + c.getTitulo() + " estan completamente cubiertos!");
            return;
        }

        // Guardo en una lista los roles por cubrir (los repito porque pueden necesitarse mas de uno)
        List<Rol> rolesPorCubrir = new ArrayList<>();
        for(Map.Entry<Rol, Integer> entry : rolesFaltantes.entrySet()) {
            Rol rol = entry.getKey();
            int cantidad = entry.getValue();
            for(int i = 0; i < cantidad; i++) {
                rolesPorCubrir.add(rol);
            }
        }

        List<Rol> rolesNoCubiertos = new ArrayList<>();
        List<Asignacion> nuevasAsignaciones = new ArrayList<>();

        for(Rol rol : rolesPorCubrir) {
            ArtistaExterno mejorOpcion = buscarArtistaMasBarato(c, rol, candidatos);
            
            if(mejorOpcion == null)
                rolesNoCubiertos.add(rol);
            else
                nuevasAsignaciones.add(new Asignacion(mejorOpcion, rol, c));
        }

        if(!rolesNoCubiertos.isEmpty()) {
            throw new RolesNoCubiertos("No se pueden cubrir los roles para la cancion seleccionada!", rolesNoCubiertos);
        }

        this.asignaciones.addAll(nuevasAsignaciones);
    
        double costoCancion = c.calcularCosto(artistasBase, nuevasAsignaciones);

        System.out.println("Contrataciones realizadas para '" + c.getTitulo() + "'. Costo total: " + costoCancion);
    }

    private ArtistaExterno buscarArtistaMasBarato(Cancion cancion, Rol rol, List<ArtistaExterno> artistaExternos) {
        ArtistaExterno mejorArtista = null;
        double costoMinimo = Double.MIN_VALUE;

        for(ArtistaExterno candidato : artistaExternos) {
            if(candidato.puedeCubrir(rol)) {

                boolean yaAsignadoCancion = asignaciones.stream().
                    anyMatch(a -> a.getArtista().equals(candidato) && a.getCancion().equals(cancion));

                int cantidadCanciones = Asignacion.contarCancionesDeArtista(asignaciones, mejorArtista);

                if(yaAsignadoCancion && cantidadCanciones <= candidato.getMaxCanciones())
                {
                    double costo = candidato.getCostoFinal(artistasBase);

                    if(costo < costoMinimo) {
                        mejorArtista = candidato;
                        costoMinimo = costo;
                    }
                }
            }
        }

        return mejorArtista;
    }

    public void listarCancionesEstado() {
        for (Cancion c : canciones) {
            boolean completa = c.getRolesFaltantes(asignaciones).isEmpty();
            double costo = c.calcularCosto(artistasBase, asignaciones);
            System.out.println("Canción: " + c.getTitulo() + " | Completa: " + completa + " | Costo: " + costo);
        }
    }

    public void contratarParaRecitalCompleto(List<ArtistaExterno> candidatos) {
    }

    public double getCostoTotalRecital() {
        double total = 0.0;
        for(Asignacion a : asignaciones) {
            total += a.getCostoEfectivo(this.artistasBase);
        }
        return total;
    }
}
