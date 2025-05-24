package juegoUno;

import java.util.Objects;

public class CartaComodin extends Carta {
    private String colorAsignado = SIN_COLOR;
    private static final String TIPO = "Comodín";

    public static CartaComodin with() {
        return new CartaComodin();
    }

    private CartaComodin() {}

    public CartaComodin asignarColor(String color) {
        if (!esColorValido(color)) {
            throw new RuntimeException("Color inválido: " + color);
        }
        this.colorAsignado = color;
        return this;
    }

    private boolean esColorValido(String color) {
        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
    }

    public boolean estaAsignada() {
        return !SIN_COLOR.equals(this.colorAsignado);
    }

    public boolean teGustaMiColor(String otroColor) {
        return estaAsignada() && this.colorAsignado.equals(otroColor);
    }

    public boolean teGustaMiNumero(int numero) {
        return false;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return TIPO.equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return true; // Siempre se puede jugar
    }

    public String obtenerColor() {
        if (!estaAsignada()) {
            throw new RuntimeException("El comodín no tiene un color asignado aún.");
        }
        return colorAsignado;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaComodin otra)) return false;
        return Objects.equals(this.colorAsignado, otra.colorAsignado);
    }

    public int hashCode() {
        return Objects.hash(colorAsignado);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.pasarTurno(jugadorActual); // No roba ni saltea
    }

    public void validarAntesDeJugar() {
        if (!estaAsignada()) {
            throw new RuntimeException("No se puede jugar un comodín sin color asignado.");
        }
    }
}

//public class CartaComodin extends Carta {
//    private String colorAsignado;
//    private static final String TIPO = "Comodín";
//
//    public static final String EXCEPTION_INTENTA_JUAGR_COMODIN_SIN_ASIGNAR_COLOR = "No se puede jugar un comodín sin color asignado.";
//
//
//    private CartaComodin() {}
//
//    public static CartaComodin with() {
//        return new CartaComodin();
//    }
//
//    public CartaComodin asignarColor(String color) {
//        if (!esColorValido(color)) {
//            throw new IllegalArgumentException("Color inválido: " + color);
//        }
//        this.colorAsignado = color;
//        return this;
//    }
//
//    private boolean esColorValido(String color) {
//        return ROJO.equals(color) || AZUL.equals(color) || VERDE.equals(color) || AMARILLO.equals(color);
//    }
//
//    public boolean teGustaMiColor(String color) {
//        return colorAsignado != null && colorAsignado.equals(color);
//    }
//
//    public boolean teGustaMiNumero(int numero) {
//        return false;
//    }
//
//    public boolean somosDelMismoTipo(String tipo) {
//        return TIPO.equals(tipo);
//    }
//
//    public boolean aceptaCarta(Carta otra) {
//        return true; // Siempre puede jugarse
//    }
//
//    public String obtenerColor() {
//        if (colorAsignado == null) {
//            throw new RuntimeException("La carta no tiene un color asignado aún");
//        }
//        return colorAsignado;
//    }
//
//    public int obtenerNumero() {
//        throw new UnsupportedOperationException("La carta Wildcard no tiene número");
//    }
//
//    public boolean equals(Object obj) {
//        if (!(obj instanceof CartaComodin otra)) return false;
//        return Objects.equals(this.colorAsignado, otra.colorAsignado);
//    }
//
//    public int hashCode() {
//        return Objects.hash(colorAsignado);
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        juego.pasarTurno(jugadorActual); // Solo cambia el color, no salta ni roba
//    }
//
//    public boolean estaAsignada() {
//        return this.colorAsignado != null;
//    }
//
//    public void validarAntesDeJugar() {
//        if (this.obtenerColor() == null) {
//            throw new RuntimeException(EXCEPTION_INTENTA_JUAGR_COMODIN_SIN_ASIGNAR_COLOR);
//        }
//    }
//}