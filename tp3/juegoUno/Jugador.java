package juegoUno;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final String nombre;
    private Jugador derecha;
    private Jugador izquierda;
    private final List<Carta> cartas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.cartas = new ArrayList<>();
    }

    public String nombre() {
        return nombre;
    }

    public void conectarCon(Jugador derecha, Jugador izquierda) {
        this.derecha = derecha;
        this.izquierda = izquierda;
    }

    public Jugador derecha() {
        return derecha;
    }

    public Jugador izquierda() {
        return izquierda;
    }

    public void recibirCarta(Carta carta) {

        cartas.add(carta);
    }

    public void jugarCarta(Carta carta) {

        cartas.remove(carta);
    }

    public boolean tiene(Carta carta) {

        return cartas.contains(carta);
    }

    public boolean sinCartas() {
        return cartas.isEmpty();
    }

    public int cantidadCartas() {
        return cartas.size();
    }

    public boolean puedeJugarSobre(Carta carta) {
        return cartas.stream().anyMatch(c -> c.aceptaCarta(carta));
    }
}


