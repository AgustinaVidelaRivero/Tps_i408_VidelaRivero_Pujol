import java.util.ArrayList;
import java.util.List;

import Juego;


public class Jugador {
    private String nombre;
    private List<Carta> mano = new ArrayList<>();

    public Jugador(String nombre, List<Carta> mano) {
        this.nombre = nombre; 
        this.mano = mano; 
    }

    public boolean tieneCarta(Carta carta) {
        return mano.contains(carta);
    }
/* 
    public boolean puedeJugar(Carta carta, Carta cartaEnJuego) {        //cartaEnJuego es la de arriba del pozo
        return carta.esJugableSobre(cartaEnJuego);
    }
*/  //resolver esto
    public void quitarCarta(Carta carta) {
        mano.remove(carta);
    }

    public void agarrarCarta(Carta carta) {
        mano.add(carta);
    }

}