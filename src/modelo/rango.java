package modelo;

public enum rango {
    // se crean conjunto fijo y limitado de constantes pueden tomar esos valores específicos y ningún otro
    AS("A", 11),
    DOS("2", 2),
    TRES("3", 3),
    CUATRO("4", 4),
    CINCO("5", 5),
    SEIS("6", 6),
    SIETE("7", 7),
    OCHO("8", 8),
    NUEVE("9", 9),
    DIEZ("10", 10),
    JOTA("J", 10),
    REINA("Q", 10),
    REY("K", 10);
    
    
    private final String nombre;
    private final int valor;
    
    rango(String nombre, int valor){
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }
    
}
