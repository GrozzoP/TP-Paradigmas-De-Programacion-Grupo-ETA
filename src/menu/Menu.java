package menu;

import cargador_datos.CargadorDatos;
import modelo.Recital;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
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
        comandos.add(new ContratarArtistasRecitalComando(recital, scanner));
        comandos.add(new EntrenarArtistaComando(recital, scanner));
        comandos.add(new ListarArtistasAsignadosComando(recital));
        comandos.add(new ListarEstadoCancionesComando(recital));
        comandos.add(new InteraccionPrologComando(recital));
        comandos.add(new MostrarHistorialColaboracionesComando(recital));
        comandos.add(new GuardarEstadoComando(recital));
        comandos.add(new CargarEstadoAnteriorComando(recital));
        comandos.add(new SalirComando(recital, this));
    }

    public void iniciarMenu() {
        while(this.estaEnEjecucion) {
            this.mostrarMenu();
            int eleccion = this.obtenerEleccion();
            this.ejecutarOpcion(eleccion);

            if(this.estaEnEjecucion) {
                System.out.println("Presione ENTER para continuar...");
                this.scanner.nextLine();
            }
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
            String descripcion = comandos.get(i).getDescripcion();

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
                if (scanner.hasNextInt()) {
                    eleccion = scanner.nextInt();

                    this.scanner.nextLine();

                    if (eleccion < MINIMA_OPCION || eleccion > comandos.size()) {
                        System.err.printf(ANSI_RED + "Opción no válida. Ingresá una opción que esté entre %d y %d: " + ANSI_RESET, MINIMA_OPCION, comandos.size());
                    }
                } else {
                    String entradaInvalida = this.scanner.next();
                    this.scanner.nextLine();

                    System.err.printf(ANSI_RED + "Entrada inválida ('%s'). Ingresá un número entre %d y %d: " + ANSI_RESET, entradaInvalida, MINIMA_OPCION, comandos.size());

                    eleccion = -1;
                }
            } catch (Exception e) {
                System.err.println(ANSI_RED + "Error inesperado en la entrada...h" + ANSI_RESET);
                this.scanner.nextLine();
                eleccion = -1;
            }
        } while (eleccion < MINIMA_OPCION || eleccion > comandos.size());

        return eleccion;
    }

    public void ejecutarOpcion(int opcion) {
        Comando comando = this.comandos.get(opcion - 1);

        try {
            comando.ejecutar();
        } catch (Exception e) {
            System.err.print("ERROR DE EJECUCION! " + e.getMessage() + "\n");
        }
    }

    public void setEstaEnEjecucion(boolean estaEnEjecucion) {
        this.estaEnEjecucion = estaEnEjecucion;
    }
}
