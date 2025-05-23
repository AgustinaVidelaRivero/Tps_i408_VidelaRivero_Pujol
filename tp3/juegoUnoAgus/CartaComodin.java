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

    //NUEVO
    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.pasarTurno(jugadorActual);
    }
}

//public class CartaComodin extends Carta {
//    private String colorAsignado;
//
//    private CartaComodin() {}
//
//    public static CartaComodin with() {
//        return new CartaComodin();
//    }
//
//    public CartaComodin asignarColor(String color) {
//        if (color == null || color.isEmpty()) {
//            throw new IllegalArgumentException("Color no válido");
//        }
//        this.colorAsignado = color.toLowerCase();
//        return this;
//    }
//
//    @Override
//    public boolean aceptaCarta(Carta otra) {
//        return true; // siempre se puede jugar una comodín
//    }
//
//    @Override
//    public boolean teGustaMiColor(String otroColor) {
//        return colorAsignado != null && colorAsignado.equalsIgnoreCase(otroColor);
//    }
//
//    @Override
//    public boolean somosDelMismoTipo(String tipo) {
//        return "CartaComodin".equals(tipo);
//    }
//
//    @Override
//    public String obtenerColor() {
//        if (colorAsignado == null) {
//            throw new RuntimeException("La carta comodín no tiene color asignado");
//        }
//        return colorAsignado;
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        juego.pasarTurno(jugadorActual);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CartaComodin)) return false;
//        CartaComodin that = (CartaComodin) o;
//        return colorAsignado != null && that.colorAsignado != null &&
//                colorAsignado.equalsIgnoreCase(that.colorAsignado);
//    }
//
//    @Override
//    public int hashCode() {
//        return colorAsignado == null ? 0 : colorAsignado.toLowerCase().hashCode();
//    }
//}


