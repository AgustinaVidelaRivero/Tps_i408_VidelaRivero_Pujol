package juegoUnoAgus;

import java.util.Objects;

public class CartaMasDos extends Carta {
    private final Color color;

    private CartaMasDos(Color color) {
        this.color = color;
    }

    public static CartaMasDos with(Color color) {
        if (color == null) throw new IllegalArgumentException("Color no puede ser null");
        return new CartaMasDos(color);
    }

    public boolean teGustaMiColor(Color otroColor) {
        return this.color.equals(otroColor);
    }

    public boolean somosDelMismoTipo(String tipo) {
        return "CartaMasDos".equals(tipo);
    }

    public boolean teGustaMiNumero(int numero) {
        return false; // No tiene número
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo("MasDos");
        //return this.teGustaMiColor(otra.obtenerColor())|| this.somosDelMismoTipo("MasDos");
    }

    public Color obtenerColor() {
        return color;
    }

    public int obtenerNumero() {
        throw new UnsupportedOperationException("La carta MasDos no tiene número");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaMasDos otra)) return false;
        return this.color == otra.color;
    }

    public int hashCode() {
        return Objects.hash(color);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        Jugador siguiente = juego.obtenerSiguienteJugador(jugadorActual);
        juego.robarCartas(siguiente, 2);
        juego.saltarSiguienteJugador(jugadorActual);
    }
}