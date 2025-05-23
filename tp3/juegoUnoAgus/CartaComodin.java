package juegoUnoAgus;


import java.util.Objects;

public class CartaComodin extends Carta {
    private Color colorAsignado;

    private CartaComodin() {}

    public static CartaComodin with() {
        return new CartaComodin();
    }

    public CartaComodin asignarColor(Color color) {
        if (color == null) throw new IllegalArgumentException("Color no puede ser null");
        this.colorAsignado = color;
        return this;
    }

    public boolean teGustaMiColor(Color color) {
        return colorAsignado != null && colorAsignado == color;
    }

    public boolean teGustaMiNumero(int numero) {
        return false;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return "CartaWildcard".equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return true;
    }
    public Color obtenerColor() {
        if (colorAsignado == null) {
            throw new RuntimeException("La carta no tiene un color asignado aún");
        }
        return colorAsignado;
    }

    public int obtenerNumero() {
        throw new UnsupportedOperationException("La carta Wildcard no tiene número");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaComodin otra)) return false;
        return Objects.equals(this.colorAsignado, otra.colorAsignado);
    }

    public int hashCode() {
        return Objects.hash(colorAsignado);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.pasarTurno(jugadorActual);
    }
}
