package juegoUnoAgus;

public class ControladorDerecha extends ControladorTurnos {
    public Jugador siguiente(Jugador actual) {
        return actual.derecha();
    }
}
