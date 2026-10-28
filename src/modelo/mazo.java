package modelo;
import java.util.ArrayList;
import java.util.Collections;

public class mazo {
    private ArrayList<carta> mazo;

    public mazo(){
        mazo = new ArrayList<carta>();

        for(pinta p : pinta.values()){
            for(rango r : rango.values()){
                mazo.add(new carta(p, r));
            }
        }
    }

    public ArrayList<carta> getCartas(){
        return mazo;
    }
    public void mezclar(){
        Collections.shuffle(mazo);
    }
    public carta repartir(){
        //validar que el mazo no este vacio a la hora de sacar la carta de arrib si no tira error
        if(mazo.isEmpty()){
            return null;
        }
        return mazo.remove(0);
    }
    
    
}