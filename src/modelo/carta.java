package modelo;


public class carta {
    
    private final pinta pinta;
    private final rango rango;
   
    public carta(pinta pinta, rango rango){
        this.pinta = pinta;
        this.rango = rango;
    }

    public pinta getPinta() {
        return pinta;
    }

    public rango getRango() {
        return rango;
    }
    
    public int getValor(){
        return rango.getValor();
    }
    
    @Override
    
    public String toString(){
        return rango.getNombre() +  pinta.getSimbolo();
    }
}
