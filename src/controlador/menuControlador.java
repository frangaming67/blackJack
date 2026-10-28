package controlador;


import baseDatos.conexionBD;
import javax.swing.JOptionPane;
import vista.menu;
import vista.login;
import java.sql.SQLException;
import modelo.jugador;
import vista.cuenta;
import vista.juego;
import vista.ranking;

public class menuControlador {
   
    private menu vista; 
    
    public menuControlador(menu vista) {
       this.vista = vista;
    }
    // Botón "Jugar": abre la pantalla del juego con el jugador logueado y cierra el menú
    public void abrirJuego(jugador jugadorActual){
        juego ventanaJuego = new juego(jugadorActual);
            ventanaJuego.setLocationRelativeTo(null);
            ventanaJuego.setVisible(true);
            
        vista.dispose();
    }
    // Botón "Ver Ranking": abre el ranking con el jugador logueado y cierra el menú
    public void abrirRanking(jugador jugadorActual){
            ranking ventanaRanking = new ranking(jugadorActual);
            ventanaRanking.setLocationRelativeTo(null);
            ventanaRanking.setVisible(true);
            
            vista.dispose();
    }
    // Botón "Mi cuenta": abre la pantalla de la cuenta con el jugador logueado y cierra el menú
    public void abrirMiCuenta(jugador jugadorActual){
            cuenta ventanacuenta = new cuenta(jugadorActual);
            ventanacuenta.setLocationRelativeTo(null);
            ventanacuenta.setVisible(true);
            
            vista.dispose();
    }
    public void cerrarSesion(){
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "Estas seguro de que queres cerrar sesion?",
                "Confirmar cierre de sesion",
                JOptionPane.YES_NO_OPTION
        );
        // Si el usuario selecciona la opción afirmativa (Sí),
        // se cierra completamente la aplicación.
        if(opcion == JOptionPane.YES_OPTION){
            login ventanaLogin = new login();
       
            ventanaLogin.setLocationRelativeTo(null);
            ventanaLogin.setVisible(true);
        
            vista.dispose();
        } 
    }
    
    public void salirAplicacion(){
        // Muestra una ventana de confirmación con las opciones Si y No.
        // El resultado de la opción elegida se guarda en la variable "opcion".
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "Estas seguro de que queres salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );
        // Si el usuario selecciona la opción afirmativa (Sí),
        // se cierra completamente la aplicación.
        if(opcion == JOptionPane.YES_OPTION){
            try{
                conexionBD.cerrarConexion();
            }catch(SQLException error){
                System.out.println("Error al cerrar la conexión: " + error.getMessage());
            }
           System.exit(0); 
        } 
    }
}
