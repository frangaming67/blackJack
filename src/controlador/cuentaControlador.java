package controlador;

import baseDatos.jugadorDAO;
import baseDatos.partidaDAO;
import javax.swing.JOptionPane;
import modelo.jugador;
import vista.cuenta;
import vista.login;
import vista.menu;

public class cuentaControlador {

    private cuenta vista;
    private jugador jugadorActual;
    private jugadorDAO jugadorDao = new jugadorDAO();
    private partidaDAO partidaDao = new partidaDAO();

    public cuentaControlador(cuenta vista, jugador jugadorActual) {
        this.vista = vista;
        this.jugadorActual = jugadorActual;
    }

    // Botón "Cambiar nombre"
    public void cambiarNombre() {
        String nuevoNombre = vista.getNuevoNombre();

        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Escribí el nombre nuevo");
            return;
        }
        if (nuevoNombre.equals(jugadorActual.getnombre())) {
            JOptionPane.showMessageDialog(vista, "Ese ya es tu nombre");
            return;
        }
        if (jugadorDao.existeNombre(nuevoNombre)) {
            JOptionPane.showMessageDialog(vista, "Ese nombre ya lo usa otro jugador");
            return;
        }

        if (jugadorDao.modificarNombre(jugadorActual.getidJugador(), nuevoNombre)) {
            // jugador no tiene setters: se crea uno nuevo con el mismo id y el nombre nuevo
            jugadorActual = new jugador(jugadorActual.getidJugador(), nuevoNombre);
            vista.mostrarUsuario(nuevoNombre);
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "Nombre cambiado");
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo cambiar el nombre");
        }
    }

    // Botón "Cambiar contraseña"
    public void cambiarContra() {
        String nuevaContra = vista.getNuevaContra();

        if (nuevaContra.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Escribí la contraseña nueva");
            return;
        }

        if (jugadorDao.modificarContra(jugadorActual.getidJugador(), nuevaContra)) {
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "Contraseña cambiada");
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo cambiar la contraseña");
        }
    }

    // Botón "Eliminar cuenta"
    public void eliminarCuenta() {
        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Seguro que querés eliminar tu cuenta?\nSe borran también todas tus partidas.",
                "Eliminar cuenta",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        // Primero las partidas (apuntan al jugador), después el jugador
        boolean partidasBorradas = partidaDao.eliminarPartidasDeJugador(jugadorActual.getidJugador());
        if (partidasBorradas && jugadorDao.eliminarJugador(jugadorActual.getidJugador())) {
            JOptionPane.showMessageDialog(vista, "Tu cuenta fue eliminada");
            login ventanaLogin = new login();
            ventanaLogin.setLocationRelativeTo(null);
            ventanaLogin.setVisible(true);
            vista.dispose();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo eliminar la cuenta");
        }
    }

    // Botón "Volver al menú"
    public void volverMenu() {
        menu ventanaMenu = new menu(jugadorActual);
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
        vista.dispose();
    }
}