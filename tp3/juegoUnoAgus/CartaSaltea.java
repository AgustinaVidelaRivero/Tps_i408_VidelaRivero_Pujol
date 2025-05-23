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

    //NUEVO
    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.saltarSiguienteJugador(jugadorActual);
    }

}

//public class CartaSaltea extends Carta {
//    private final String color;
//
//    private CartaSaltea(String color) {
//        this.color = color.toLowerCase();
//    }
//
//    public static CartaSaltea with(String color) {
//        return new CartaSaltea(color);
//    }
//
//    @Override
//    public boolean aceptaCarta(Carta otra) {
//        return otra.teGustaMiColor(color) || otra.somosDelMismoTipo("CartaSaltea");
//    }
//
//    @Override
//    public boolean teGustaMiColor(String otroColor) {
//        return this.color.equalsIgnoreCase(otroColor);
//    }
//
//    @Override
//    public boolean somosDelMismoTipo(String tipo) {
//        return "CartaSaltea".equals(tipo);
//    }
//
//    @Override
//    public String obtenerColor() {
//        return color;
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        juego.saltarSiguienteJugador(jugadorActual);
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (!(o instanceof CartaSaltea)) return false;
////        CartaSaltea that = (CartaSaltea) o;
////        return color.equals(that.color);
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(color);
////    }
//
//    public boolean equals(Object obj) {
//        if (!(obj instanceof CartaSaltea otra)) return false;
//        return Objects.equals(this.color, otra.color);
//        }
//
//    public int hashCode() {
//
//        return Objects.hash(color);
//    }
//}


