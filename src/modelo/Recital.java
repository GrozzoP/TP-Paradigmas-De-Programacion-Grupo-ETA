package modelo;

import java.util.*;

import excepcion.ArtistaNoEntrenable;
import excepcion.RolesNoCubiertos;

/**
 * Representa un recital compuesto por un conjunto de 
 * canciones, artistas base y artistas externos.
 * 
 * La clase gestiona:
 *  - Asignaciones de artistas a roles dentro de cada canción.
 *  - Contratación automática o manual de artistas externos.
 *  - Entrenamiento de artistas externos (si corresponde).
 *  - Cálculo del costo total del recital o por canción.
 *  - Identificación de roles faltantes.
 *
 * Esta clase es el "núcleo operativo": coordina el armado completo del recital.
 */
public class Recital {
    private Set<Cancion> canciones = new HashSet<>();
    private Set<ArtistaBase> artistasBase = new HashSet<>();
    // Cuando carguemos los datos, capaz nos conviene analizarlo desde aca a los artistas externos...
    private Set<ArtistaExterno> artistaExternos = new HashSet<>();
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

    /**
     * Devuelve el conjunto de artistas externos efectivamente contratados.
     * Se determina revisando todas las asignaciones del recital.
     */
    public Set<ArtistaExterno> getArtistasContratados() {
        Set<ArtistaExterno> contratados = new HashSet<>();
        for (Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            artista.agregarSiContratable(contratados);
        }
        return contratados;
    }

    /**
     * Devuelve los roles faltantes 
     * de una canción en particular 
     */
    public Map<Rol, Integer> getRolesFaltantes(Cancion c) {
        return c.getRolesFaltantes(this.asignaciones);
    }

    /**
     * Devuelve los roles faltantes 
     * de TODAS las canciones del recital.
     */
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
     * Entrena un artista externo en un nuevo rol.
     * Regla: No se puede entrenar un artista ya contratado para alguna canción. 
     */
    public void entrenarArtista(ArtistaExterno artista, Rol nuevoRol) throws ArtistaNoEntrenable {
        boolean estaContratado = asignaciones.stream().anyMatch(a -> a.getArtista().equals(artista));

        if (estaContratado) {
            throw new ArtistaNoEntrenable("El artista " + artista.getNombre() + " no es entrenable!");
        }
        if(artista.puedeEntrenarse()){
            artista.entrenar(nuevoRol);
        } else{
            System.out.println("El artista: " + artista + " ya no puede entrenarse en más roles");
        }
    }
    
    /**
     * Asigna artistas base automáticamente.
     * Recorre todas las canciones y asigna un artista base que pueda cubrir
     * algún rol faltante (si no está ya asignado a esa canción).
     */
    public void asignarArtistasBase() {
        for(Cancion cancion : canciones) {
            for(ArtistaBase artista : artistasBase) {
                boolean yaAsignadoCancion = cancion.artistaAsignadoCancion(artista, asignaciones);

                if(!yaAsignadoCancion) {
                    Map<Rol, Integer> rolesFaltantes = cancion.getRolesFaltantes(asignaciones);

                    for(Rol rol : rolesFaltantes.keySet()) {
                        if(!yaAsignadoCancion && artista.puedeCubrir(rol)) {
                            Asignacion asignacion = new Asignacion(artista, rol, cancion);
                            asignaciones.add(asignacion);
                            yaAsignadoCancion = true;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Contrata artistas externos SOLO para una canción específica.
     * Elige los más baratos que cubran cada rol faltante.
     * Si no hay suficientes artistas para un rol, lanza una excepción.
     */
    public void contratarParaCancion(Cancion c, List<ArtistaExterno> artistaExternos) throws RolesNoCubiertos {
        Map<Rol, Integer> rolesFaltantes = c.getRolesFaltantes(this.asignaciones);
        List<Asignacion> nuevasAsignaciones = new ArrayList<>();
        Set<ArtistaExterno> artistasYaUsadosEnCancion = new HashSet<>();

        if(rolesFaltantes.isEmpty()) {
            System.out.println("Los roles para la cancion " + c.getTitulo() + " estan completamente cubiertos!");
            return;
        }
        // Para cada rol faltante...
        for (Map.Entry<Rol, Integer> rolCantidad : rolesFaltantes.entrySet()) {
            Rol rol = rolCantidad.getKey();
            int cantidad = rolCantidad.getValue();

             // Filtra artistas capaces, no repetidos y los ordena por costo
            List<ArtistaExterno> candidatos = artistaExternos.stream()
                    .filter(a -> a.puedeCubrir(rol))
                    .filter(a -> !artistasYaUsadosEnCancion.contains(a))
                    .sorted(Comparator.comparingDouble(a -> a.getCostoFinal(artistasBase)))
                    .toList();

            if(candidatos.size() < cantidad)
                throw new RolesNoCubiertos("No hay suficientes artistas para cubrir los roles!", rol);

            // Asigna los más baratos
            for (int i = 0; i < cantidad; i++) {
                ArtistaExterno elegido = candidatos.get(i);
                nuevasAsignaciones.add(new Asignacion(elegido, rol, c));
                artistasYaUsadosEnCancion.add(elegido);
            }
        }

        this.asignaciones.addAll(nuevasAsignaciones);
    
        double costoCancion = c.calcularCosto(artistasBase, nuevasAsignaciones);
        System.out.println("Contrataciones realizadas para '" + c.getTitulo() + "'. Costo total: " + costoCancion);
    }

    /**
     * Contrata artistas externos para TODO el recital.
     * Similar al método anterior, pero recorriendo todas las canciones.
     * Respeta el límite máximo de canciones de cada artista.
     */
    public void contratarParaRecitalCompleto(List<ArtistaExterno> candidatos) throws RolesNoCubiertos {
        List<Asignacion> nuevasAsignaciones = new ArrayList<>();

        for(Cancion cancion : canciones) {
            Map<Rol, Integer> rolesFaltantes = cancion.getRolesFaltantes(asignaciones);

            if (rolesFaltantes.isEmpty()) {
                System.out.println("La canción '" + cancion.getTitulo() + "' ya está completa");
            }
            else
            {
                for(Map.Entry<Rol, Integer> rolCantidad : rolesFaltantes.entrySet()) {
                    Rol rol = rolCantidad.getKey();
                    int cantidad = rolCantidad.getValue(), asignados = 0;

                    for(ArtistaExterno artistaExterno : candidatos) {
                        boolean puedeCubrir = false, yaAsignadoCancion = false;
                        int cantidadCancionesActuales = 0 ;

                        puedeCubrir = artistaExterno.puedeCubrir(rol);
                        yaAsignadoCancion = cancion.artistaAsignadoCancion(artistaExterno, asignaciones);
                        cantidadCancionesActuales = Asignacion.contarCancionesDeArtista(asignaciones, artistaExterno);

                        boolean disponible = puedeCubrir && !yaAsignadoCancion &&
                                cantidadCancionesActuales < artistaExterno.getMaxCanciones();

                        if(disponible && asignados < cantidad) {
                            nuevasAsignaciones.add(new Asignacion(artistaExterno, rol, cancion));
                            asignados++;
                        }
                    }

                    if(asignados < cantidad)
                        throw new RolesNoCubiertos("No hay suficientes artistas para cubrir el rol: " + rol + " en " + cancion.getTitulo(), rol);
                }
             }
        }

        this.asignaciones.addAll(nuevasAsignaciones);
    }
    
    /** 
     * Devuelve el artista externo más barato que pueda cubrir un rol
     * en una canción dada, teniendo en cuenta límites de canciones
     * y evitando duplicaciones dentro de la misma canción.
     */
    private ArtistaExterno buscarArtistaMasBarato(Cancion cancion, Rol rol, List<ArtistaExterno> artistaExternos) {
        ArtistaExterno mejorArtista = null;
        double costoMinimo = Double.MAX_VALUE;

        for(ArtistaExterno candidato : artistaExternos) {
            if(candidato.puedeCubrir(rol)) {

                boolean yaAsignadoCancion = asignaciones.stream().
                    anyMatch(a -> a.getArtista().equals(candidato) && a.getCancion().equals(cancion));

                int cantidadCanciones = Asignacion.contarCancionesDeArtista(asignaciones, candidato);
                
                if(!yaAsignadoCancion && cantidadCanciones < candidato.getMaxCanciones())
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

    /**
     * Muestra el estado de cada canción:
     * - Si está completa
     * - Su costo total actual
     */
    public void listarCancionesEstado() {
        for (Cancion c : canciones) {
            boolean completa = c.getRolesFaltantes(asignaciones).isEmpty();
            double costo = c.calcularCosto(artistasBase, asignaciones);
            System.out.println("Canción: " + c.getTitulo() + " | Completa: " + completa + " | Costo: " + costo);
        }
    }

    /**
     * Calcula el costo total del recital.
     * Se multiplica para cada artista externo:
     * (cantidad de canciones en las que participa) × (costo final del artista).
     */
    public double getCostoTotalRecital() {
        Map<ArtistaExterno, Set<Cancion>> mapaArtistaCanciones = new HashMap<>();
        double total = 0.0;

        for(Asignacion asignacion : asignaciones) {
            ArtistaBase artista = asignacion.getArtista();
            if(!artistasBase.contains(artista))
                mapaArtistaCanciones
                        .computeIfAbsent((ArtistaExterno) artista, k -> new HashSet<>())
                        .add(asignacion.getCancion());
        }

        for(ArtistaExterno artista : mapaArtistaCanciones.keySet()) {
            int cantidadCanciones = mapaArtistaCanciones.get(artista).size();
            total += cantidadCanciones * artista.getCostoFinal(artistasBase);
        }

        return total;
    }
}
