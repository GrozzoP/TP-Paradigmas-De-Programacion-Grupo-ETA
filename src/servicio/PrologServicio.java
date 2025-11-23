package servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Util;
import org.jpl7.Variable;

import modelo.Recital;
import modelo.Rol;

public class PrologServicio {

    public PrologServicio() {
        // Cargamos el archivo Prolog una sola vez al crear el servicio
        Query q1 = new Query(
                "consult",
                new Term[]{ new Atom("src/prolog/recital_planning.pl") }
        );

        if (!q1.hasSolution()) {
            throw new RuntimeException("No se pudo cargar el archivo Prolog: src/prolog/recital_planning.pl");
        }
    }

    /**
     * Llama a Prolog (min_trainings/2) para calcular
     * la cantidad mínima de entrenamientos necesarios.
     */
    public int calcularEntrenamientosMinimos(Recital recital) {

        Map<Rol, Integer> faltantes = recital.getRolesFaltantesTotales();

        // Si no hay roles faltantes, no hace falta entrenar
        if (faltantes.isEmpty()) {
            return 0;
        }

        List<Term> rolesTerms = new ArrayList<>();

        // Ejemplo: voz=2, guitarra=1 -> [voz, voz, guitarra]
        for (Map.Entry<Rol, Integer> entry : faltantes.entrySet()) {
            Rol rol = entry.getKey();
            int cantidad = entry.getValue();

            for (int i = 0; i < cantidad; i++) {
                rolesTerms.add(new Atom(rol.getNombre()));
            }
        }

        @SuppressWarnings("deprecation")
        Term rolesList = Util.termArrayToList(rolesTerms.toArray(new Term[0]));

        Variable N = new Variable("N");

        Query q = new Query(
                "min_trainings",
                new Term[]{ rolesList, N }
        );

        Map<String, Term> solution = q.oneSolution();

        return solution.get("N").intValue();
    }

    public double calcularCostoTotalEntrenamientos(Recital recital, double costoBase) {
        int minEntrenamientos = calcularEntrenamientosMinimos(recital);
        return minEntrenamientos * costoBase;
    }
}
