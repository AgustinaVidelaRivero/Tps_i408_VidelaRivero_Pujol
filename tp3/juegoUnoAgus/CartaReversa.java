package juegoUnoAgus;

import java.util.Objects;

public class CartaReversa extends Carta {
    private final Color color;

    private CartaReversa(Color color) {
        this.color = color;
    }

    public static CartaReversa with(Color color) {
        if (color == null) throw new IllegalArgumentException("Color no puede ser null");
        return new CartaReversa(color);
    }

    public boolean teGustaMiColor(Color otroColor) {
        return this.color.equals(otroColor);
    }

    public boolean teGustaMiNumero(int numero) {
        return false;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return "CartaReverse".equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.somosDelMismoTipo("Reverse");
    }
    public Color obtenerColor() {
        return color;
    }

    public int obtenerNumero() {
        throw new UnsupportedOperationException("La carta Reverse no tiene número");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CartaReversa otra)) return false;
        return this.color == otra.color;
    }

    public int hashCode() {
        return Objects.hash(color);
    }

    //NUEVO
    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.cambiarSentido();
        juego.pasarTurno(jugadorActual); // Para que el jugador anterior vuelva a jugar
    }
}

//public class CartaReversa extends Carta {
//    private final String color;
//
//    private CartaReversa(String color) {
//        this.color = color.toLowerCase();
//    }
//
//    public static CartaReversa with(String color) {
//        return new CartaReversa(color);
//    }
//
//    @Override
//    public boolean aceptaCarta(Carta otra) {
//        return otra.teGustaMiColor(color) || otra.somosDelMismoTipo("CartaReversa");
//    }
//
//    @Override
//    public boolean teGustaMiColor(String otroColor) {
//        return this.color.equalsIgnoreCase(otroColor);
//    }
//
//    @Override
//    public boolean somosDelMismoTipo(String tipo) {
//        return "CartaReversa".equals(tipo);
//    }
//
//    @Override
//    public String obtenerColor() {
//        return color;
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        juego.cambiarSentido();
//        juego.pasarTurno(jugadorActual); // Para que el jugador anterior vuelva a jugar
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (!(o instanceof CartaReversa)) return false;
////        CartaReversa that = (CartaReversa) o;
////        return color.equals(that.color);
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(color);
////    }
//
//    public boolean equals(Object obj) {
//        if (!(obj instanceof CartaReversa otra)) return false;
//        return Objects.equals(this.color, otra.color);
//    }
//
//    public int hashCode() {
//        return Objects.hash(color);
//    }
//}

