package menu;

import java.util.Scanner;

import modelo.Recital;
import servicio.PrologServicio;

public class InteraccionPrologComando implements Comando {
	private Recital recital;
	private final PrologServicio prologServicio;

	public InteraccionPrologComando(Recital recital) {
		this.recital = recital;
		this.prologServicio = new PrologServicio();
	}

	@Override
	public void ejecutar() {
		if (recital == null) {
            System.out.println("Primero debe cargar un recital antes de consultar a Prolog.");
            return;
        }

		@SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);

        System.out.println("Consulta a Prolog: entrenamientos mínimos y costo total.");
        System.out.print("Ingrese el costo base por entrenamiento: ");

        double costoBase = sc.nextDouble();

        int entrenamientosMinimos = prologServicio.calcularEntrenamientosMinimos(recital);
        double costoTotal = prologServicio.calcularCostoTotalEntrenamientos(recital, costoBase);

        System.out.println("--------------------------------------------");
        System.out.println("Entrenamientos mínimos necesarios: " + entrenamientosMinimos);
        System.out.println("Costo total estimado: " + costoTotal);
        System.out.println("--------------------------------------------");
	}

	@Override
	public String getDescripcion() {
		return "(PROLOG) Retornar cantidad de entrenamientos mínimos";
	}
}
