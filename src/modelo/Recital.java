package modelo;

import java.util.*;

import excepcion.ArtistaNoEntrenable;
import excepcion.RolesNoCubiertos;

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

    public Set<ArtistaExterno> getArtistasContratados() {
        Set<ArtistaExterno> contratados = new HashSet<>();
        for (Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            artista.agregarSiContratable(contratados);
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

        if (estaContratado) {
            throw new ArtistaNoEntrenable("El artista " + artista.getNombre() + " no es entrenable!");
        }
        if(artista.puedeEntrenarse()){
            artista.entrenar(nuevoRol);
        } else{
            System.out.println("El artista: " + artista + " ya no puede entrenarse en más roles");
        }
    }

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
    
    public void contratarParaCancion(Cancion c, List<ArtistaExterno> artistaExternos) throws RolesNoCubiertos {
        Map<Rol, Integer> rolesFaltantes = c.getRolesFaltantes(this.asignaciones);
        List<Asignacion> nuevasAsignaciones = new ArrayList<>();
        Set<ArtistaExterno> artistasYaUsadosEnCancion = new HashSet<>();

        if(rolesFaltantes.isEmpty()) {
            System.out.println("Los roles para la cancion " + c.getTitulo() + " estan completamente cubiertos!");
            return;
        }

        for (Map.Entry<Rol, Integer> rolCantidad : rolesFaltantes.entrySet()) {
            Rol rol = rolCantidad.getKey();
            int cantidad = rolCantidad.getValue();

            List<ArtistaExterno> candidatos = artistaExternos.stream()
                    .filter(a -> a.puedeCubrir(rol))
                    .filter(a -> !artistasYaUsadosEnCancion.contains(a))
                    .sorted(Comparator.comparingDouble(a -> a.getCostoFinal(artistasBase)))
                    .toList();

            if(candidatos.size() < cantidad)
                throw new RolesNoCubiertos("No hay suficientes artistas para cubrir los roles!", rol);

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

    public void listarCancionesEstado() {
        for (Cancion c : canciones) {
            boolean completa = c.getRolesFaltantes(asignaciones).isEmpty();
            double costo = c.calcularCosto(artistasBase, asignaciones);
            System.out.println("Canción: " + c.getTitulo() + " | Completa: " + completa + " | Costo: " + costo);
        }
    }

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
