package modelo;

import java.util.*;
import java.util.stream.Collectors;

import cargador_datos.CargadorDatos;
import excepcion.ArtistaNoEntrenable;
import excepcion.RolesNoCubiertos;
import servicio.AsignacionServicio;
import servicio.EntrenarServicio;

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
    private Set<Cancion> canciones;
    private Set<ArtistaBase> artistasBase;
    private Set<ArtistaExterno> artistaExternos;
    private List<Asignacion> asignaciones;
    private final AsignacionServicio asignacionServicio;
    private final EntrenarServicio entrenamientoServicio;


    public Recital(CargadorDatos cargadorDatos) throws Exception {
        this.canciones = cargadorDatos.cargarCanciones();
        this.artistasBase = cargadorDatos.cargarSoloArtistasBase();
        this.artistaExternos = cargadorDatos.cargarSoloArtistasExternos();
        this.asignaciones = new ArrayList<>();
        this.asignacionServicio = new AsignacionServicio();
        this.entrenamientoServicio = new EntrenarServicio();
    }

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
    public Set<ArtistaBase> getArtistasContratados() {
        Set<ArtistaBase> contratados = new HashSet<>();
        for (Asignacion a : this.asignaciones) {
            ArtistaBase artista = a.getArtista();
            contratados.add(artista);
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
        if(artista.esEntrenable()){
            artista.entrenar(nuevoRol);
        } else{
            System.out.println("El artista: " + artista + " ya no puede entrenarse en más roles");
        }
    }

    /**
     * Contrata artistas SOLO para una canción específica.
     * Elige los más baratos que cubran cada rol faltante.
     * Si no hay suficientes artistas para un rol, lanza una excepción.
     */
    public void contratarParaCancion(Cancion c) throws RolesNoCubiertos {
        Map<Rol, Integer> rolesFaltantes = c.getRolesFaltantes(this.asignaciones);
        List<Asignacion> nuevasAsignaciones = new ArrayList<>();

        if (rolesFaltantes.isEmpty()) {
            System.out.println("Los roles para la cancion " + c.getTitulo() + " estan completamente cubiertos!");
            return;
        }

        Set<ArtistaBase> artistasAsignadosEnEstaCancion = new HashSet<>();
        List<Rol> rolesNoCubiertos = new ArrayList<>();

        for (Map.Entry<Rol, Integer> entry : rolesFaltantes.entrySet()) {
            Rol rol = entry.getKey();
            int cantidadFaltante = entry.getValue();

            for (int i = 0; i < cantidadFaltante; i++) {
                ArtistaBase artistaElegido = this.artistasBase.stream()
                        .filter(artista -> artista.puedeCubrir(rol))
                        .filter(artista -> !artistasAsignadosEnEstaCancion.contains(artista))
                        .findFirst()
                        .orElse(null);

                if (artistaElegido == null)
                    artistaElegido = buscarArtistaExternoMasBarato(c, rol, artistaExternos, artistasAsignadosEnEstaCancion);

                if (artistaElegido != null) {
                    nuevasAsignaciones.add(new Asignacion(artistaElegido, rol, c));
                    artistasAsignadosEnEstaCancion.add(artistaElegido);
                } else {
                    rolesNoCubiertos.add(rol);
                }
            }
        }

        if (!rolesNoCubiertos.isEmpty()) {
            Set<Rol> rolesTiposFaltantes = new HashSet<>(rolesNoCubiertos);

            Map<String, Long> conteoRoles = rolesNoCubiertos.stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.groupingBy(nombre -> nombre, Collectors.counting()));

            Map<ArtistaExterno, Set<Rol>> recomendaciones = entrenamientoServicio.recomendarEntrenamiento(
                    rolesTiposFaltantes, this.artistaExternos, this.asignaciones
            );

            if (!recomendaciones.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder();
                errorMsg.append("Contratación incompleta. Faltan cubrir los siguientes roles:\n");

                conteoRoles.forEach((nombre, count) -> {
                    errorMsg.append("- **").append(nombre).append("**: Faltan **").append(count).append("** artistas.\n");
                });

                errorMsg.append("\nSe recomienda entrenar a los siguientes artistas para cubrir los roles faltantes (optimizado por costo):\n");

                for (Map.Entry<ArtistaExterno, Set<Rol>> entry : recomendaciones.entrySet()) {
                    errorMsg.append("- **").append(entry.getKey().getNombre()).append("**: Puede entrenarse en ").append(entry.getValue().stream().map(Rol::getNombre).collect(Collectors.joining(", "))).append("\n");
                }

                throw new RolesNoCubiertos(errorMsg.toString(), rolesTiposFaltantes.iterator().next());
            } else {
                String faltantes = conteoRoles.entrySet().stream()
                        .map(e -> e.getKey() + " (" + e.getValue() + ")")
                        .collect(Collectors.joining(", "));

                throw new RolesNoCubiertos("No se pudieron cubrir todos los roles y no hay artistas externos entrenables disponibles. Faltan: " + faltantes, rolesTiposFaltantes.iterator().next());
            }
        }

        this.asignaciones.addAll(nuevasAsignaciones);
    }

    /**
     * Contrata artistas externos para TODO el recital.
     * Similar al método anterior, pero recorriendo todas las canciones.
     * Respeta el límite máximo de canciones de cada artista.
     */
    public void contratarParaRecitalCompleto(List<ArtistaExterno> candidatos) throws RolesNoCubiertos {
    }

    /**
     * Devuelve el artista externo más barato que pueda cubrir un rol
     * en una canción dada, teniendo en cuenta límites de canciones
     * y evitando duplicaciones dentro de la misma canción.
     */
    private ArtistaExterno buscarArtistaExternoMasBarato(Cancion c, Rol rol, Set<ArtistaExterno> artistaExternos, Set<ArtistaBase> artistasAsignados) {
        ArtistaExterno mejorArtista = null;
        double costoMinimo = Double.MAX_VALUE;

        for(ArtistaExterno candidato : artistaExternos) {
            if(asignacionServicio.esArtistaElegibleParaRol(this.asignaciones, candidato, rol, artistasAsignados)) {
                double costo = candidato.getCostoBase();

                if(costo < costoMinimo) {
                    mejorArtista = candidato;
                    costoMinimo = costo;
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
            double costo = asignacionServicio.calcularCostoTotalCancion(c, this.asignaciones, artistasBase);
            System.out.println("Canción: " + c.getTitulo() + " | Completa: " + completa + " | Costo: " + costo);
        }
    }



    /**
     * Calcula el costo total del recital.
     * Se multiplica para cada artista externo:
     * (cantidad de canciones en las que participa) × (costo final del artista).
     */
    public double getCostoTotalRecital() {
        double total = 0.0;

        for(Asignacion asignacion : this.asignaciones) {
            ArtistaBase artista = asignacion.getArtista();

            double costoAsignacion = asignacionServicio.calcularCostoFinalPorAsignacion(
                    artista,
                    asignacion.getCancion(),
                    this.asignaciones,
                    this.artistasBase
            );

            total += costoAsignacion;
        }

        return total;
    }

    public Set<Cancion> getCanciones() {
        return canciones;
    }

    public Set<ArtistaBase> getArtistasBase() {
        return artistasBase;
    }

    public Set<ArtistaExterno> getArtistaExternos() {
        return artistaExternos;
    }

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }
}
