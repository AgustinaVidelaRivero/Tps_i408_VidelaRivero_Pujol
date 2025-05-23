package juegoUnoAgus;


import java.util.Objects;

public class CartaSaltea extends Carta {
    private final Color color;

    private CartaSaltea(Color color) {
        this.color = color;
    }

    public static CartaSaltea with(Color color) {
        if (color == null) throw new IllegalArgumentException("Color no puede ser null");
        return new CartaSaltea(color);
    }

    public boolean teGustaMiColor(Color otroColor) {
        return this.color.equals(otroColor);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return "CartaSkip".equals(tipo);
    }

    public boolean teGustaMiNumero(int numero) {
        return false; // No tiene número
    }

    public boolean aceptaCarta(Carta otra) {

        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo("Skip");
    }

    public Color obtenerColor() {
        return color;
    }

    public int obtenerNumero() {
        throw new UnsupportedOperationException("La carta Skip no tiene número");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaSaltea otra)) return false;
        return this.color == otra.color;
        }

    public int hashCode() {

        return Objects.hash(color);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.saltarSiguienteJugador(jugadorActual);
    }

}