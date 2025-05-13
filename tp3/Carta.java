

public class Carta {
    enum Color {ROJO, VERDE, AZUL, AMARILLO, SIN_COLOR} // sin color para comodines

    private Color color;
    private int numero; 

    public Carta(Color color, Tipo tipo, int numero) {
        this.color = color;
        this.numero = numero;
    }

    public boolean esJugableSobre(Carta cartaEnJuego) {
        return this.color == cartaEnJuego.color || this.numero == cartaEnJuego.numero;
    }
}
