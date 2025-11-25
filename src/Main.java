import cargador_datos.CargadorDatos;
import menu.Menu;
import modelo.ArtistaBase;
import modelo.ArtistaExterno;
import modelo.Cancion;
import modelo.Recital;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        //System.setProperty("SWI_HOME_DIR", "C:\\Program Files\\swipl");
        
        System.out.println("Iniciando aplicación de gestión de recitales...");

        Menu menuApp = new Menu();

        menuApp.iniciarMenu();
    }
}
