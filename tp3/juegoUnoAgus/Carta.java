package juegoUnoAgus;

public abstract class Carta {
    private boolean cantoUno = false;

    public Carta uno() {
        this.cantoUno = true;
        return this;
    }

    public boolean cantoUno() {
        return cantoUno;
    }

    public abstract boolean aceptaCarta(Carta otra);

    public abstract boolean teGustaMiColor(Color color);

    public abstract boolean teGustaMiNumero(int numero);

    public abstract boolean somosDelMismoTipo(String tipo);

    public abstract Color obtenerColor();

    public abstract int obtenerNumero();

    public abstract void aplicarEfecto(Juego juego, Jugador jugadorActual);

}

//public abstract class Carta {
//    public static final String ROJO = "rojo";
//    public static final String AZUL = "azul";
//    public static final String VERDE = "verde";
//    public static final String AMARILLO = "amarillo";
//
//    private boolean cantoUno = false;
//
//    public Carta uno() {
//        this.cantoUno = true;
//        return this;
//    }
//
//    public boolean cantoUno() {
//        return cantoUno;
//    }
//
//    public abstract boolean aceptaCarta(Carta otra);
//
//    public boolean teGustaMiColor(String color) {
//        return false;
//    }
//
//    public boolean teGustaMiNumero(int numero) {
//        return false;
//    }
//
//    public boolean somosDelMismoTipo(String tipo) {
//        return false;
//    }
//
//    public String obtenerColor() {
//        throw new UnsupportedOperationException("Esta carta no tiene color");
//    }
//
//    public int obtenerNumero() {
//        throw new UnsupportedOperationException("Esta carta no tiene número");
//    }
//
//    public abstract void aplicarEfecto(Juego juego, Jugador jugador);
//}



