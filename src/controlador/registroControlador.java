package controlador;

import javax.swing.JOptionPane;
import vista.registro;
import vista.menu;
import baseDatos.jugadorDAO;
import modelo.jugador;

public class registroControlador {
    
    private registro vista;
    private jugadorDAO dao = new jugadorDAO();
    
    public registroControlador(registro vista) {
        this.vista = vista;
    }
    
    public void crearCuenta(String nombre, String contra){
        
       if (nombre == null || nombre.trim().isEmpty() || contra == null || contra.trim().isEmpty() ) {
            JOptionPane.showMessageDialog(vista,"Alguno de los campos ingresados esta vacio");
            return;
        }
       if(dao.existeNombre(nombre)){
           JOptionPane.showMessageDialog(vista,"Ese usuario ya existe");
           return;
       }
       jugador nuevo = dao.registrar(nombre, contra);
       if(nuevo == null){
           JOptionPane.showMessageDialog(vista,"No se pudo crear la cuenta");
          return; 
       }    
      
        menu ventanaMenu = new menu(nuevo);
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
        //cierra la pagina de menu
        vista.dispose();
    }  
    
}
