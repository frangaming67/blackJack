package modelo;

// =====================================================================
// CLASE HIJA: crupier
// "extends participante" = el crupier ES un participante.
// HERENCIA: recibe todo lo de participante (la mano, recibirCarta,
// getPuntos y getMano) sin escribirlo de nuevo.
// Lo único que agrega es su propia forma de decidir si pide carta.
// =====================================================================
public class crupier extends participante {

    // POLIMORFISMO: completa el método abstracto del padre a su manera.
    // @Override = "este método es la versión del crupier del método del padre".
    //
    // Regla del casino: el crupier pide carta mientras tenga menos de 17.
    // Si tiene 16 o menos devuelve true (pide); con 17 o más, false (se planta).
    @Override
    public boolean debePedirCarta() {
        return getPuntos() < 17;   // getPuntos() lo heredó de participante
    }
}