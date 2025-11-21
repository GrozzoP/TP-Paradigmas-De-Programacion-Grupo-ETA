package menu;

import modelo.Recital;
import servicio.PrologServicio;

public class InteraccionPrologComando implements Comando, Descriptor {
	private Recital recital;
	private final PrologServicio prologServicio;

	public InteraccionPrologComando(Recital recital) {
		this.recital = recital;
		this.prologServicio = new PrologServicio();
	}

	@Override
	public void ejecutar() {
		System.out.println("Consulta a Prolog: entrenamientos mínimos necesarios");

		int entrenamientos = prologServicio.calcularEntrenamientosMinimos(recital);

		System.out.println("Cantidad mínima de entrenamientos (según Prolog): " + entrenamientos);

	}

	@Override
	public String getDescripcion() {
		return "(PROLOG) Retornar cantidad de entrenamientos mínimos";
	}
}
