package modelo;

// Una fila del ranking: el nombre del jugador y sus partidas ganadas
public class filaRanking {

    private String nombre;
    private int ganadas;

    public filaRanking(String nombre, int ganadas) {
        this.nombre = nombre;
        this.ganadas = ganadas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getGanadas() {
        return ganadas;
    }
}