package blackjackproyecto;

import vista.login;
import baseDatos.conexionBD;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {

        try {
            conexionBD.crearTablas();
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo iniciar la base de datos:\n" + error.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // sin las base de datos no sigue
        }   
        login ventanaLogin = new login();
        ventanaLogin.setLocationRelativeTo(null); //centra la pantalla 
        ventanaLogin.setVisible(true);//muestra la pantalla
    }
}
