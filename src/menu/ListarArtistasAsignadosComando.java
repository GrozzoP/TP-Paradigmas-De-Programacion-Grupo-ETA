package menu;

import modelo.ArtistaBase;
import modelo.Recital;

import java.util.Set;

public class ListarArtistasAsignadosComando implements Comando {
    private Recital recital;

    public ListarArtistasAsignadosComando(Recital recital) {
        this.recital = recital;
    }

    @Override
    public void ejecutar() {
        Set<ArtistaBase> artistasContratados = this.recital.getArtistasContratados();

        if(artistasContratados.isEmpty()) {
            System.out.println("Actualmente, no hay artistas asignados!");
        }
        else {
            System.out.println("\n--- 🎤 ARTISTAS ASIGNADOS AL RECITAL 🎸 ---");
            int contador = 1;

            for(ArtistaBase artista : artistasContratados) {
                System.out.println(contador++ + "° ARTISTA\n" + artista);
                System.out.println("----------------------------------------------");
            }
        }
    }

    @Override
    public String getDescripcion() {
        return "Listar los artistas presentes en las asignaciones";
    }
}
