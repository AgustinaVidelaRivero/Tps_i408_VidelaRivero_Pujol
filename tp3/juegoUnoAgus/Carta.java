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