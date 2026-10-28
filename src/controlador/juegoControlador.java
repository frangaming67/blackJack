package controlador;

import javax.swing.JOptionPane;
import vista.juego;
import vista.menu;
import modelo.jugador;
import modelo.mano;
import modelo.mazo;
import baseDatos.partidaDAO;
import modelo.jugadorMesa;
import modelo.crupier;
import modelo.participante;

public class juegoControlador {

    private juego vista;
    private jugador jugadorActual;
    private mazo mazoJuego;
    private jugadorMesa jugadorEnMesa; // el jugador: puede pedir mientras tenga menos de 21
    private crupier crupierMesa;  // el crupier: pide mientras tenga menos de 17 si o si 
    
    private int ganadas = 0;
    private partidaDAO dao = new partidaDAO();

    public juegoControlador(juego vista, jugador jugadorActual) {
        this.vista = vista;
        this.jugadorActual = jugadorActual;
        vista.esperarInicio();   // Pedir y Plantarse desactivados hasta tocar Empezar
        vista.mostrarMensaje("Tocá Empezar para jugar");
    }
    
    // Prepara una mano nueva: mazo mezclado y 2 cartas para cada uno
    public void iniciarPartida() {
        vista.empezarPartida();   // activa Pedir y Plantarse, y esconde Empezar

        // 1. Mazo nuevo de 52 cartas, mezclado
        mazoJuego = new mazo();
        mazoJuego.mezclar();

        // 2. Los dos participantes, con la mano vacía
        jugadorEnMesa = new jugadorMesa();
        crupierMesa = new crupier();

        // 3. Reparto inicial: 2 cartas para cada uno
        darCarta(jugadorEnMesa);
        darCarta(jugadorEnMesa);
        darCarta(crupierMesa);
        darCarta(crupierMesa);

        // 4. Mostrar todo en la pantalla
        mostrarMesa();
        vista.mostrarMensaje("Tu turno");
    }
    // El jugador toca "Pedir carta": recibe una carta más
    public void pedirCarta() {
        darCarta(jugadorEnMesa);
        mostrarMesa();

        if (jugadorEnMesa.getPuntos() > 21) {
            // Se pasó de 21: pierde la mano y la racha vuelve a 0
            vista.mostrarMensaje("Te pasaste. Perdiste");
            ganadas = 0;
            vista.terminarPartida();
            preguntarSeguir();
        } else if (!jugadorEnMesa.debePedirCarta()) {
            // Llegó a 21 justo: no puede mejorar, así que se planta solo.
            // POLIMORFISMO: debePedirCarta() usa la regla del jugador (menos de 21)
            plantarse();
        }
    }
    // El jugador se planta: juega el crupier y se decide quién gana
    public void plantarse() {
        // El crupier pide cartas mientras su regla se lo indique.
        // POLIMORFISMO: debePedirCarta() usa la regla del crupier (menos de 17)
        while (crupierMesa.debePedirCarta()) {
            darCarta(crupierMesa);
        }
        mostrarMesa();

        // Comparar los puntos de los dos
        int puntosJugador = jugadorEnMesa.getPuntos();
        int puntosCrupier = crupierMesa.getPuntos();
        // Decidir quién gana. Gana el que está más cerca de 21 sin pasarse
        if (puntosCrupier > 21) {
            // El crupier se pasó: gana el jugador y la racha sube
            vista.mostrarMensaje("El crupier se pasó. ¡Ganaste!");
            ganadas = ganadas + 1;
        } else if (puntosJugador > puntosCrupier) {
            // El jugador tiene más puntos: gana y la racha sube
            vista.mostrarMensaje("¡Ganaste!");
            ganadas = ganadas + 1;
        } else if (puntosJugador < puntosCrupier) {
            // El crupier tiene más puntos: pierde y la racha vuelve a 0
            vista.mostrarMensaje("Perdiste");
            ganadas = 0;
        } else {
            // Mismos puntos: empate, la racha no cambia
            vista.mostrarMensaje("Empate");
        }

        // La mano terminó: se bloquean los botones y se pregunta si sigue
        vista.terminarPartida();
        preguntarSeguir();
    }
    //// Al terminar una mano: si tiene ganadas, decide si arriesgarlas o retirarse
    private void preguntarSeguir(){ 
        if (ganadas > 0){
            int opcion = JOptionPane.showConfirmDialog(
                vista, "Llevás " + ganadas + " partida/s ganadas" + "¿Seguís jugando? (Si) si perdes la partida, perdes los puntos.\n (No) = te cagoneas y se guardan)",
                "¿Seguir o retirarse?",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION){
            iniciarPartida();// arriesga: mano nueva
        }else{
            retirarse();// se guarda lo ganado
            volverMenu();
        }
        }else{
            int opcion = JOptionPane.showConfirmDialog(vista,"¿Querés jugar otra mano?","Nueva mano",JOptionPane.YES_NO_OPTION);
            if(opcion == JOptionPane.YES_OPTION){
                iniciarPartida();
            }else{
                volverMenu();
            }
        }
    }
    private void retirarse(){
        boolean guardado = dao.guardar(jugadorActual.getidJugador(), ganadas);
        
        if(guardado){
            JOptionPane.showMessageDialog(vista, "Te retiras con " + ganadas + " ganadas. Quedo Guardado!");
        }else{
            JOptionPane.showMessageDialog(vista, "No se pudo guardar la partida");
        }
        ganadas = 0;
    }
    

    public void volverMenu() {
            menu ventanaMenu = new menu(jugadorActual);
            ventanaMenu.setLocationRelativeTo(null);
            ventanaMenu.setVisible(true);
            vista.dispose();
    }
    // Le da UNA carta del mazo a un participante.
    // POLIMORFISMO: recibe un "participante", así que sirve tanto para
    // el jugador (jugadorMesa) como para el crupier (crupier),
    // porque los dos heredan de participante.
    private void darCarta(participante p){
        p.recibirCarta(mazoJuego.repartir());
    }
    
    // Muestra en la pantalla las cartas y los puntos de los dos.
    // Se usa cada vez que cambia algo en la mesa, así no repetimos
    // estas 4 líneas en varios lugares (clean code).
    private void mostrarMesa() {
        vista.mostrarCartasJugador(jugadorEnMesa.getMano().toString());
        //                         └──── la mano ────┘ └─ a texto ─┘
//                                                    "K♠  7♥  "
        vista.mostrarPuntosJugador(jugadorEnMesa.getPuntos());
        vista.mostrarCartasCrupier(crupierMesa.getMano().toString());
        vista.mostrarPuntosCrupier(crupierMesa.getPuntos());
    }
}