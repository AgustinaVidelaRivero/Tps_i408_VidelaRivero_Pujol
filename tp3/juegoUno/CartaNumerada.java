package juegoUno;

import java.util.Objects;

public class CartaNumerada extends Carta {
    private final String color;
    private final int numero;
    private static final String TIPO = "CartaNumero";

    private CartaNumerada(String color, int numero) {
        if (!esColorValido(color)) throw new IllegalArgumentException("Color inválido: " + color);
        if (numero < 0 || numero > 9) throw new IllegalArgumentException("Número inválido: " + numero);
        this.color = color;
        this.numero = numero;
    }

    public static CartaNumerada with(String color, int numero) {
        return new CartaNumerada(color, numero);
    }

    private boolean esColorValido(String color) {
        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
    }

    public boolean teGustaMiColor(String otroColor) {
        return this.color.equals(otroColor);
    }

    public boolean teGustaMiNumero(int otroNumero) {
        return this.numero == otroNumero;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.teGustaMiNumero(this.numero);
    }

    public String obtenerColor() {
        return color;
    }

    public int obtenerNumero() {
        return numero;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaNumerada otra)) return false;
        return this.color.equals(otra.color) && this.numero == otra.numero;
    }

    public int hashCode() {
        return Objects.hash(color, numero);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.pasarTurno(jugadorActual);
    }
}