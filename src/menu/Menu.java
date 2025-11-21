package menu;

import cargador_datos.CargadorDatos;
import modelo.Recital;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final List<Comando> comandos;
    private final CargadorDatos cargadorDatos;
    private final Scanner scanner;
    private Recital recital;
    private boolean estaEnEjecucion = true;
    public static final int MINIMA_OPCION = 1;

    public Menu() throws Exception {
        this.comandos = new ArrayList<>();
        this.cargadorDatos = new CargadorDatos();
        this.scanner = new Scanner(System.in);
        this.crearRecital();
        this.definirOpciones();
    }

    private void crearRecital() throws Exception {
        this.recital = new Recital(this.cargadorDatos);
    }

    private void definirOpciones() {
        comandos.add(new ListarArtistasBaseComando(recital));
        comandos.add(new ListasArtistasExternosComando(recital));
        comandos.add(new ListarRolesRestCancionComando(recital, scanner));
        comandos.add(new ListarRolesRestRecitalComando(recital));
        comandos.add(new ContratarArtistasCancionComando(recital, scanner));
        comandos.add(new ContratarArtistasRecitalComando(recital));
        comandos.add(new EntrenarArtistaComando(recital, scanner));
        comandos.add(new ListarArtistasAsignadosComando(recital));
        comandos.add(new ListarEstadoCancionesComando(recital));
        comandos.add(new InteraccionPrologComando(recital));
        comandos.add(new SalirComando(recital, this));
    }

    public void iniciarMenu() {
        while(this.estaEnEjecucion) {
            this.mostrarMenu();
            int eleccion = this.obtenerEleccion();
            this.ejecutarOpcion(eleccion);
        }

        this.scanner.close();
        System.out.println(ANSI_PURPLE + "Se ha finalizado la aplicación!" + ANSI_RESET);
    }

    public void mostrarMenu() {
        final int ANCHO = 74;
        String titulo = ANSI_GREEN + "ARMADO DE RECITAL" + ANSI_RESET;

        int tamTitulo = 17;
        int paddingTitle = (ANCHO - tamTitulo) / 2;

        String separador = "-".repeat(ANCHO);

        System.out.println(separador);

        System.out.println(" ".repeat(paddingTitle) + titulo);
        System.out.println(separador);

        for (int i = 0; i < comandos.size(); i++) {
            int numero = i + 1;
            Descriptor comandoDescriptor = (Descriptor) comandos.get(i);
            String descripcion = comandoDescriptor.getDescripcion();

            String lineaOpcion = String.format(ANSI_CYAN + "%d." + ANSI_RESET + " %s", numero, descripcion);
            System.out.println(lineaOpcion);
        }

        System.out.println(separador);
        System.out.print("Seleccionar opción: ");
    }

    private int obtenerEleccion() {
        int eleccion = -1;

        do {
            try {
                if(scanner.hasNextInt()) {
                    eleccion = scanner.nextInt();

                    if(eleccion < MINIMA_OPCION || eleccion > comandos.size())
                        System.err.printf("Opcion no valida. Ingrese una opcion que este entre %d y %d: ", MINIMA_OPCION, comandos.size());
                }
            } catch (InputMismatchException e) {
                System.err.println("Error inesperado en la entrada.");
            }
        } while (eleccion < MINIMA_OPCION || eleccion > comandos.size());

        scanner.nextLine();
        return eleccion;
    }

    public void ejecutarOpcion(int opcion) {
        Comando comando = this.comandos.get(opcion - 1);

        try {
            comando.ejecutar();
        } catch (Exception e) {
            System.err.print("ERROR DE EJECUCION! " + e.getMessage());
        }
    }
}
