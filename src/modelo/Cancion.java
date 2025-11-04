package modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cancion {
    private final String titulo;
    private Map<Rol, Integer> rolesRequeridos;

    public Cancion(String titulo, Map<Rol, Integer> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = rolesRequeridos;
    }

    public String getTitulo() {
        return titulo;
    }

    public Map<Rol, Integer> getRolesRequeridos() {
        return rolesRequeridos;
    }

    public Map<Rol, Integer> getRolesFaltantes(List<Asignacion> asignaciones) { 
        Map<Rol, Integer> rolesFaltantes = new HashMap<>(rolesRequeridos);
        
        // Recorremos todas las asignaciones y vamos "restando" las que ya estan cubiertas para esta cancion. 
        for(Asignacion a : asignaciones){
            if(a.getCancion().equals(this)){
                Rol rol = a.getRolAsignado();
                if(rolesFaltantes.containsKey(rol)){
                    
                    // El metodo get() devuelve el valor asociado a la clave (porque es un Map)
                    int restantes = rolesFaltantes.get(rol) - 1;
                    if(restantes <= 0){
                        // Quita por completo la dupla <Rol,Integer>, eliminando por ejemplo: {"Guitarra", 1}
                        rolesFaltantes.remove(rol);                 
                    }else{
                        // Sobreescribe la dupla con la nueva cantidad, por ejemplo: {"Guitarra", 2} -> {"Guitarra", 1}
                        rolesFaltantes.put(rol, restantes);    
                    }
                }
            }
        }
        return rolesFaltantes;
    }

    @Override
    public String toString() {
        return "Cancion{" + "titulo=" + titulo + '}';
    }
    
}
