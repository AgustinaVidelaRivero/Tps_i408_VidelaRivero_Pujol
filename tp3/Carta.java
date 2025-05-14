public abstract class Carta {
    public abstract boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo);
    public abstract String getColor();
    public abstract int getNumero();
}

public class CartaNumerada extends Carta {
    private String color;
    private int numero;

    public CartaNumerada(String color, int numero) {
        this.color = color;
        this.numero = numero;
    }

    public boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo) {
        return this.color.equalsIgnoreCase(color) || this.numero == numero;
    }

    public String getColor() { return color; }
    public int getNumero() { return numero; }
}

public class Comodin extends Carta {     
    private String color;

    public boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo) {
        return true;
    }

    public boolean jugar(String color) {    //solo cuando la jugamos le asignamos el color 
        this.color = color;
    }

    public String getColor() { return color; }
    public int getNumero() { return -1; }
}

public class Reversa extends Carta {

    private String color;

    public Reversa(String color) {
        this.color = color;
    }

    public boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo) {    
        return this.color.equalsIgnoreCase(color) || tipoEnPozo.equals(Reversa.class);
    }

    public String getColor() { return color; }
    public int getNumero() { return -1; }
}

public class Saltea extends Carta {

    private String color;

    public Saltea(String color) {
        this.color = color;
    }

    public boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo) {    
        return this.color.equalsIgnoreCase(color) || tipoEnPozo.equals(Saltea.class);
    }

    public String getColor() { return color; }
    public int getNumero() { return -1; }
}

public class MasDos extends Carta {

    private String color;

    public MasDos(String color) {
        this.color = color;
    }

    public boolean esJugableSobre(String color, int numero, Class<? extends Carta> tipoEnPozo) {    
        return this.color.equalsIgnoreCase(color) || tipoEnPozo.equals(MasDos.class);
    }

    public String getColor() { return color; }
    public int getNumero() { return -1; }
}

