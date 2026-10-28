package modelo;

public abstract class participante {

    protected mano mano;

    // Constructor: se ejecuta al crear un crupier o un jugadorMesa.
    // Le da a cada participante una mano vacía.
    public participante() {
        mano = new mano();
    }

    // Recibe una carta (que viene del mazo) y la guarda en su mano.
    public void recibirCarta(carta c) {
        mano.agregarCarta(c);
    }

    // Devuelve los puntos de su mano (la mano ya sabe la regla del As).
    public int getPuntos() {
        return mano.getPuntos();
    }

    // Devuelve la mano, para poder mostrar las cartas en la pantalla.
    public mano getMano() {
        return mano;
    }

    //
    // MÉTODO ABSTRACTO: acá no se escribe cómo se decide, porque
    // el crupier y el jugador deciden DISTINTO. Cada clase hija
    // está obligada a escribir su propia versión.
    // Eso es POLIMORFISMO: mismo método, distinto comportamiento.
    public abstract boolean debePedirCarta();
}