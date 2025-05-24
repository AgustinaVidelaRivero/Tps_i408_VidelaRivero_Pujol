package juegoUno;

public class CartaSaltea extends CartaEspecial {
    private static final String TIPO = "Saltea";

    private CartaSaltea(String color) {
        super(color);
    }

    public static CartaSaltea with(String color) {
        return new CartaSaltea(color);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo(TIPO);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.saltarSiguienteJugador(jugadorActual);
    }
}
