package juegoUno;

public class ControladorIzquierda extends ControladorTurnos {
    public Jugador siguiente(Jugador actual) {
        return actual.izquierda();
    }
}
