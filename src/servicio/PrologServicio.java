package servicio;

import modelo.Recital;
import modelo.Rol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrologServicio {
    public PrologServicio() {
        /*
        //Cargamos el archivo Prolog
        Query q1 = new Query(
                "consult",
                new Term[]{ new Atom("src/prolog/recital_planning.pl") }
        );

        if (!q1.hasSolution()) {
            throw new RuntimeException("No se pudo cargar el archivo Prolog: prolog/recital_planning.pl");
        }
        */
    }


    public int calcularEntrenamientosMinimos(Recital recital) {
   /*
        Map<Rol, Integer> faltantes = recital.getRolesFaltantesTotales();

        //Convertimos ese map a una lista de Term con repetidos
        //con el fin de que: voz principal = 2 y guitarra = 1  nos quede [voz principal, voz principal, guitarra]
        List<Term> rolesTerms = new ArrayList<>();

        for (Map.Entry<Rol, Integer> entry : faltantes.entrySet()) {
            Rol rol = entry.getKey();
            int cantidad = entry.getValue();

            for (int i = 0; i < cantidad; i++) {
                //Lo instanciamos en prolog creando un atom
                rolesTerms.add(new Atom(rol.getNombre()));
            }
        }

        //Convertimos la lista java a una lista prolog
        Term rolesList = Util.termArrayToList(rolesTerms.toArray(new Term[0]));

        //Creamos la variable a utulizar en prolog
        Variable N = new Variable("N");

        //Llamamos a min_trainings
        Query q = new Query(
                "min_trainings",
                new Term[]{ rolesList, N }
        );

        Map<String, Term> solution = q.oneSolution();

        
        return solution.get("N").intValue();*/
        return 0;
    }
}
