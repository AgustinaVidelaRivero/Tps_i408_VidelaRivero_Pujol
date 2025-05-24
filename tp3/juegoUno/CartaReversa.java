package juegoUno;

public class CartaReversa extends CartaEspecial {
    private static final String TIPO = "Reversa";

    private CartaReversa(String color) {
        super(color);
    }

    public static CartaReversa with(String color) {
        return new CartaReversa(color);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo(TIPO);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.cambiarSentido();
        juego.pasarTurno(jugadorActual);  // vuelve a jugar el anterior
    }
}