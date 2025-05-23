package juegoUnoAgus;


import java.util.Objects;

public class CartaNumerada extends Carta {
    private final Color color;
    private final int numero;

    private CartaNumerada(Color color, int numero) {
        this.color = color;
        this.numero = numero;
    }


    public static CartaNumerada with(Color color, int numero) {
        if (color == null) throw new IllegalArgumentException("Color no puede ser null");
        if (numero < 0 || numero > 9) throw new IllegalArgumentException("Número inválido: " + numero);
        return new CartaNumerada(color, numero);
    }


    public boolean teGustaMiColor(Color otroColor) {
        return this.color.equals(otroColor);
    }


    public boolean teGustaMiNumero(int otroNumero) {
        return this.numero == otroNumero;
    }

    public boolean somosDelMismoTipo(String tipo) {
        return "CartaNumero".equals(tipo);
    }

    public boolean aceptaCarta(Carta otra) {
        return otra.teGustaMiColor(this.color) || otra.teGustaMiNumero(this.numero);
    }

    public Color obtenerColor() {
        return color;
    }


    public int obtenerNumero() {
        return numero;
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof CartaNumerada)) return false;
        CartaNumerada otra = (CartaNumerada) obj;
        return this.obtenerColor().equals(otra.obtenerColor()) &&
                this.obtenerNumero() == otra.obtenerNumero();
    }

    public int hashCode() {
        return Objects.hash(color, numero);
    }

    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
        juego.pasarTurno(jugadorActual);
    }
}

//public class CartaNumerada extends Carta {
//    private final String color;
//    private final int numero;
//
//    private CartaNumerada(String color, int numero) {
//        this.color = color.toLowerCase();
//        this.numero = numero;
//    }
//
//    public static CartaNumerada with(String color, int numero) {
//        return new CartaNumerada(color, numero);
//    }
//
//    @Override
//    public boolean aceptaCarta(Carta otra) {
//        return otra.teGustaMiColor(color)
//                || otra.teGustaMiNumero(numero)
//                || otra.somosDelMismoTipo("CartaNumerada");
//    }
//
//    @Override
//    public boolean teGustaMiColor(String otroColor) {
//        return this.color.equalsIgnoreCase(otroColor);
//    }
//
//    @Override
//    public boolean teGustaMiNumero(int otroNumero) {
//        return this.numero == otroNumero;
//    }
//
//    @Override
//    public boolean somosDelMismoTipo(String tipo) {
//        return "CartaNumerada".equals(tipo);
//    }
//
//    @Override
//    public String obtenerColor() {
//        return color;
//    }
//
//    @Override
//    public int obtenerNumero() {
//        return numero;
//    }
//
//    public void aplicarEfecto(Juego juego, Jugador jugadorActual) {
//        juego.pasarTurno(jugadorActual);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof CartaNumerada)) return false;
//        CartaNumerada that = (CartaNumerada) o;
//        return numero == that.numero && color.equalsIgnoreCase(that.color);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(color.toLowerCase(), numero);
//    }
//}