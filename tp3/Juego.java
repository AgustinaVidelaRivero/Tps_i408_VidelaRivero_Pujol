import java.util.ArrayList;
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

        for (int i = 0; i < nombres.size(); i++) {          // instancio a los jugadores con sus cartas iniciales 
            this.jugadores.add(new Jugador(nombres.get(i)));
        }

        mazo = crearMazo();          // creamos el mazo

        this.pozo.add(mazo.pop());   // inicializamos el pozo con la primera carta del mazo
    }

    public static Stack<Carta> crearMazo(){   //vamos a crear un mazo mezclado
        return;
    }

    public static List<Carta> darManoInicial(){
        return;
    }

}





