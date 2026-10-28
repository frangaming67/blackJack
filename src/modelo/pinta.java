package modelo;

public enum pinta {
    // se crean conjunto fijo y limitado de constantes pueden tomar esos valores específicos y ningún otro
    CORAZON("♥"),
    DIAMANTE("♦"),
    TREBOL("♣"),
    ESPADA("♠");

    private final String simbolo;
    //constructor 
    pinta(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
