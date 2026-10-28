package controlador;

import vista.login;
import vista.menu;
import javax.swing.JOptionPane;
import baseDatos.jugadorDAO;
import modelo.jugador;
import vista.registro;

public class loginControlador {

    private login vista;
    private jugadorDAO dao = new jugadorDAO();
    
    public loginControlador(login vista) {
        this.vista = vista;
    }

    public void iniciarSesion(String nombre, String contra) {

        if (nombre == null || nombre.trim().isEmpty() || contra == null || contra.trim().isEmpty() ) {
            JOptionPane.showMessageDialog(vista,"Alguno de los campos ingresados esta vacio");
            return;
        }
        jugador encontrado = dao.autenticar(nombre, contra);
        if(encontrado == null){
            JOptionPane.showMessageDialog(vista,"Usuario o contraseña incorrectos");
            return;
        }
        menu ventanaMenu = new menu(encontrado);
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
        //cierra la pagina de login
        vista.dispose();
    }
    public void registrarse(){
        registro ventanaRegistro = new registro();
            ventanaRegistro.setLocationRelativeTo(null);
            ventanaRegistro.setVisible(true);
            
        vista.dispose();
    }
}