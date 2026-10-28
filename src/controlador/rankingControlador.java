package controlador;

import baseDatos.partidaDAO;
import java.util.ArrayList;
import modelo.filaRanking;
import modelo.jugador;
import vista.menu;
import vista.ranking;

public class rankingControlador {

    private ranking vista;
    private jugador jugadorActual;
    private partidaDAO dao = new partidaDAO();

    public rankingControlador(ranking vista, jugador jugadorActual) {
        this.vista = vista;
        this.jugadorActual = jugadorActual;
        cargarRanking();   // al abrir la pantalla, se llena la tabla
    }

    // Pide el ranking a la base de datos y lo muestra en la tabla
    private void cargarRanking() {
        ArrayList<filaRanking> lista = dao.obtenerRanking();
        vista.limpiarTabla();

        int puesto = 1;
        for (filaRanking fila : lista) {
            // agregar la fila a la vista: puesto, nombre y ganadas
            vista.agregarFila(puesto, fila.getNombre(), fila.getGanadas());
            puesto = puesto + 1;
        }
    }

    public void volverMenu() {
        
        menu ventanaMenu = new menu(jugadorActual);
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
        vista.dispose();
    }
}