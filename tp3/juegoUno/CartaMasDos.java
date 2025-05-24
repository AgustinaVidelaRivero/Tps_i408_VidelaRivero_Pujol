package juegoUno;

public class CartaMasDos extends CartaEspecial {
    private static final String TIPO = "MasDos";

    private CartaMasDos(String color) {
        super(color);
    }

    public static CartaMasDos with(String color) {
        return new CartaMasDos(color);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo(TIPO);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        Jugador siguiente = juego.obtenerSiguienteJugador(jugadorActual);
        juego.robarCartas(siguiente, 2);
        juego.saltarSiguienteJugador(jugadorActual);
    }
}