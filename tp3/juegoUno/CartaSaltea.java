package juegoUno;


import java.util.Objects;

public class CartaSaltea extends Carta {
    private final String color;
    private static final String TIPO = "Saltea";

    private CartaSaltea(String color) {
        if (!esColorValido(color)) {
            throw new IllegalArgumentException("Color inválido: " + color);
        }
        this.color = color;
    }

    public static CartaSaltea with(String color) {
        return new CartaSaltea(color);
    }

    private boolean esColorValido(String color) {
        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
    }

    public boolean teGustaMiColor(String otroColor) {
        return this.color.equals(otroColor);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean teGustaMiNumero(int numero) {
        return false; // No tiene número
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo(TIPO);
    }

    public String obtenerColor() {
        return color;
    }

    public int obtenerNumero() {
        throw new UnsupportedOperationException("La carta Skip no tiene número");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaSaltea otra)) return false;
        return this.color.equals(otra.color);
    }

    public int hashCode() {
        return Objects.hash(color);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.saltarSiguienteJugador(jugadorActual);
    }
}
