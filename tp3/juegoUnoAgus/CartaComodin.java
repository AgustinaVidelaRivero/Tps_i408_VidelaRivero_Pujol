package juegoUnoAgus;

import java.util.Objects;

public class CartaComodin extends Carta {
    private String colorAsignado;
    private static final String TIPO = "Comodín";

    private CartaComodin() {}

    public static CartaComodin with() {
        return new CartaComodin();
    }

    public CartaComodin asignarColor(String color) {
        if (!esColorValido(color)) {
            throw new IllegalArgumentException("Color inválido: " + color);
        }
        this.colorAsignado = color;
        return this;
    }

    private boolean esColorValido(String color) {
        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
    }

    public boolean teGustaMiColor(String color) {
        return colorAsignado != null && colorAsignado.equals(color);
    }

    public boolean teGustaMiNumero(int numero) {
        return false;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return true; // Siempre puede jugarse
    }

    public String obtenerColor() {
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
        juego.pasarTurno(jugadorActual); // Solo cambia el color, no salta ni roba
    }
}