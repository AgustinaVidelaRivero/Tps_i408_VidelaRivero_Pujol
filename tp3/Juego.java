import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import Carta;
import Jugador;

class Juego {
    private Stack<Carta> pozo = new Stack<>();
    private List<Jugador> jugadores = new ArrayList<>();
    private Stack<Carta> mazo = new Stack<>();
    private int turnoActual = 0;

    public Juego(List<String> nombres) {
        if (nombres.size() < 2) {
            System.out.println("Error: Debe haber al menos 2 jugadores para iniciar el juego.");
            return; 
        }
        // creamos el mazo mezclado
        this.mazo = crearMazo();  

        // instancio a los jugadores con sus cartas iniciales 
        for (int i = 0; i < nombres.size(); i++) { 
            List<Carta> mano = Juego.darManoInicial(mazo);         
            this.jugadores.add(new Jugador(nombres.get(i), mano));
        }        
        // inicializamos el pozo con la primera carta del mazo, osea nunca va a estar vacio
        this.pozo.add(mazo.pop());   
    }

    public static Stack<Carta> crearMazo() {
        Stack<Carta> mazo = new Stack<>();
        String[] colores = {"azul", "rojo", "verde", "amarillo"};

        // 1. Cartas numeradas 0-9 en cada color
        for (String color : colores) {
            // 0 solo una vez por color
            mazo.push(new CartaNumerada(color, 0));
            // 1-9 dos veces por cada color
            for (int num = 1; num <= 9; num++) {
                mazo.push(new CartaNumerada(color, num));
                mazo.push(new CartaNumerada(color, num));
            }
        }

        // 2. Cartas especiales: MasDos, Reversa, Saltea - 2 de cada en cada color
        for (String color : colores) {
            // MasDos
            mazo.push(new MasDos(color));
            mazo.push(new MasDos(color));
            // Reversa
            mazo.push(new Reversa(color));
            mazo.push(new Reversa(color));
            // Saltea
            mazo.push(new Saltea(color));
            mazo.push(new Saltea(color));
        }

        // 3. Comodines: 4 cartas
        for (int i = 0; i < 4; i++) {
            mazo.push(new Comodin());
        }

        // Mezclamos el mazo
        Collections.shuffle(mazo);

        return mazo;
    }

    public static List<Carta> darManoInicial(Stack<Carta> mazo) {
        List<Carta> mano = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mano.add(mazo.pop());
        }
        return mano;
    }

    public Carta getTopeDePozo() {
        return this.pozo.peek();   // muestra la ultima carta tirada en el pozo, nunca va a estar vacio xq en el constructor del juego ponemos de entrada una carta en el pozo
    }

/* 
    public boolean ultimaCartaTiene(String color, int numero, Class<? extends Carta> tipo) {
        Carta ultimaCarta = getTopeDePozo();

        // Verificar si la carta en el pozo es del mismo tipo exacto
        boolean tipoCorrecto = ultimaCarta.getClass().equals(tipo);

        // Caso si es una carta numerada
        if (tipoCorrecto && tipo.equals(CartaNumerada.class)) {
            CartaNumerada c = (CartaNumerada) ultimaCarta;
            boolean colorMatch = c.getColor().equalsIgnoreCase(color);
            boolean numeroMatch = c.getNumero() == numero;
            return colorMatch && numeroMatch;
        }

        // Caso comodin
        else if (tipoCorrecto && tipo.equals(Comodin.class)) {
            return true;
        }

        // Caso cartas especiales
        else { 
            return tipoCorrecto &&    
        }
    }
*/
}




