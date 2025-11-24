package menu;

import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Recital;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MostrarHistorialColaboracionesComando implements Comando {
    private Recital recital;

    public MostrarHistorialColaboracionesComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        System.out.println("--- MAPA DE COLABORACIONES ---");

        Set<String> relaciones = new HashSet<>();

        Set<ArtistaBase> artistasBase = recital.getArtistasBase();
        Set<ArtistaExterno> artistasExternos = recital.getArtistaExternos();

        Set<ArtistaBase> todosLosArtistas = new HashSet<>(artistasBase);
        todosLosArtistas.addAll(artistasExternos);

        if(todosLosArtistas.size() <= 1) {
            System.out.println("No hay suficientes artistas para establecer relaciones entre ellos!");
            return;
        } else {
            ArtistaBase[] artistas = todosLosArtistas.toArray(new ArtistaBase[0]);

            for(int i = 0; i < artistas.length; i++) {
                for(int j = i + 1; j < artistas.length; j++) {
                    ArtistaBase artistaA = artistas[i];
                    ArtistaBase artistaB = artistas[j];

                    if(artistaA.comparteBanda(artistaB)) {
                        String relacionA = artistaA.getNombre() + "::" + artistaB.getNombre();
                        String relacionB = artistaB.getNombre() + "::" + artistaA.getNombre();

                        if(!relaciones.contains(relacionA) && !relaciones.contains(relacionB)) {
                            String bandas = getBandasCompartidas(artistaA, artistaB);

                            System.out.printf("    %s <-> %s (Comparten banda: %s)\n",
                                    artistaA.getNombre(),
                                    artistaB.getNombre(),
                                    bandas);

                            relaciones.add(relacionA);
                        }
                    }
                }
            }
        }

        if(relaciones.isEmpty()) {
            System.out.println("No se encontraron relaciones!");
        }

        System.out.println("-----------------------------------------------------------------");
    }

    private String getBandasCompartidas(ArtistaBase a, ArtistaBase b) {
        return a.getHistorialBandas().stream()
                .filter(colabA -> b.getHistorialBandas().stream()
                        .anyMatch(colabB -> colabB.getBanda().equals(colabA.getBanda())))
                .map(colab -> colab.getBanda().getNombre())
                .collect(Collectors.joining(", "));

    }

    @Override
    public String getDescripcion() {
        return "(BONUS) Se visualiza un grafo simple entre artistas";
    }
}
