package juegoUno;

public abstract class Carta {
    public static final String ROJO = "rojo";
    public static final String AZUL = "azul";
    public static final String VERDE = "verde";
    public static final String AMARILLO = "amarillo";
    public static final String SIN_COLOR = "SIN_COLOR";

    private boolean cantoUno = false;

    public Carta uno() {
        this.cantoUno = true;
        return this;
    }

    public boolean cantoUno() {
        return cantoUno;
    }

    public abstract boolean aceptaCarta(Carta otra);

    public abstract boolean teGustaMiColor(String color);

    public abstract boolean teGustaMiNumero(int numero);

    public abstract boolean somosDelMismoTipo(String tipo);

    public abstract String obtenerColor();

    public abstract void aplicarEfecto(Juego juego, Jugador jugadorActual);

    public void validarAntesDeJugar() {
        //no hago nada por defecto
    }
}