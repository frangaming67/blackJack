package modelo;

import java.util.ArrayList;

public class mano {

    // Las cartas que tiene el jugador (o el crupier) en la mano
    private ArrayList<carta> cartas;

    // Constructor: la mano empieza vacía, sin cartas
    public mano() {
        cartas = new ArrayList<carta>();
    }

    // Agrega a la mano una carta que viene de afuera (normalmente, del mazo)
    public void agregarCarta(carta c) {
        cartas.add(c);
    }

    // Devuelve las cartas que hay en la mano
    public ArrayList<carta> getCartas() {
        return cartas;
    }
    // Suma los puntos de la mano. El As vale 11, o 1 si te pasás de 21
    public int getPuntos() {
        int total = 0;   // suma de puntos
        int ases = 0;    // ases que todavía valen 11

        // Parte 1: sumar todo, contando cada As como 11
        for (carta c : cartas) {
           total = total + c.getValor();
            if (c.getRango() == rango.AS) {
             ases = ases + 1;
            }
        }

        // Parte 2: mientras te pases de 21 y quede algún As en 11, ese As pasa a valer 1
        while (total > 21 && ases > 0) {
            total = total - 10;
            ases = ases - 1;
        }

        return total;
    }
    @Override
    public String toString(){
        String texto = "";
        for (carta c : cartas){
            texto = texto + c + " ";
        }
        return texto;
    }
}