package juegoUno;

import java.util.Objects;

public abstract class CartaEspecial extends Carta {
    protected final String color;

    protected CartaEspecial(String color) {
        if (!esColorValido(color)) {
            throw new RuntimeException("Color inválido: " + color);
        }
        this.color = color;
    }

    protected boolean esColorValido(String color) {
        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
    }

    public boolean teGustaMiColor(String otroColor) {

        return this.color.equals(otroColor);
    }

    public boolean teGustaMiNumero(int numero) {
        return false;
    }

    public String obtenerColor() {

        return this.color;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaEspecial otra)) return false;
        return this.color.equals(otra.color);
    }

    public int hashCode() {

        return Objects.hash(color);
    }
}
