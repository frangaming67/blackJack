package modelo;

// =====================================================================
// CLASE HIJA: jugadorMesa
// Es el jugador sentado en la mesa durante una mano.
// No confundir con "jugador", que es el usuario de la base de datos
// (id y nombre).
//
// HERENCIA: igual que el crupier, recibe todo lo de participante.
// =====================================================================
public class jugadorMesa extends participante {

    // POLIMORFISMO: el mismo método que el crupier, pero con otra regla.
    //
    // El jugador puede pedir carta mientras tenga menos de 21.
    // Con 21 ya no puede mejorar, así que devuelve false.
    @Override
    public boolean debePedirCarta() {
        return getPuntos() < 21;   // getPuntos() lo heredó de participante
    }
}