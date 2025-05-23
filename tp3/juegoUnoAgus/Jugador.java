package juegoUnoAgus;

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

    public void setDerecha(Jugador jugador) {

        this.derecha = jugador;
    }

    public void setIzquierda(Jugador jugador) {
        this.izquierda = jugador;
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

    public boolean tieneAlMenosUnaCartaJugable(Carta cartaDelPozo) {
        for (Carta carta : this.cartas) {
            if (carta.aceptaCarta(cartaDelPozo)) return true;
        }
        return false;
    }

}


