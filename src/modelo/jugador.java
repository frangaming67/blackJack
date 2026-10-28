package modelo;


public class jugador {
    //atributos del objeto
    private int idJugador;
    private String nombre;
    // se crea el constructor para crear el objeto con sus atributos
    public jugador(int idJugador, String nombre){
        this.idJugador = idJugador;
        this.nombre = nombre;
    }
    //se crean los getter y setters
    public int getidJugador(){
        return idJugador;
    }
    public String getnombre(){
        return nombre;
    }
}
