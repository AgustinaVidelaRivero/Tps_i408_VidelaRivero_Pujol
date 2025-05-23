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

    //NUEVO
    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        Jugador siguiente = juego.obtenerSiguienteJugador(jugadorActual);
        juego.robarCartas(siguiente, 2);
        juego.saltarSiguienteJugador(jugadorActual);
    }
}

//public class CartaMasDos extends Carta {
//    private final String color;
//
//    private CartaMasDos(String color) {
//        this.color = color.toLowerCase();
//    }
//
//    public static CartaMasDos with(String color) {
//        return new CartaMasDos(color);
//    }
//
//    @Override
//    public boolean aceptaCarta(Carta otra) {
//        return otra.teGustaMiColor(color) || otra.somosDelMismoTipo("CartaMasDos");
//    }
//
//    @Override
//    public boolean teGustaMiColor(String otroColor) {
//        return this.color.equalsIgnoreCase(otroColor);
//    }
//
//    @Override
//    public boolean somosDelMismoTipo(String tipo) {
//        return "CartaMasDos".equals(tipo);
//    }
//
//    public String obtenerColor() {
//        return color;
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        Jugador siguiente = juego.obtenerSiguienteJugador(jugadorActual);
//        juego.robarCartas(siguiente, 2);
//        juego.saltarSiguienteJugador(jugadorActual);
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (!(o instanceof CartaMasDos)) return false;
////        CartaMasDos that = (CartaMasDos) o;
////        return color.equals(that.color);
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(color);
////    }
//
//    public boolean equals(Object obj) {
//        if (!(obj instanceof CartaMasDos otra)) return false;
//        return Objects.equals(this.color, otra.color);
//    }
//
//    public int hashCode() {
//        return Objects.hash(color);
//    }
//}



