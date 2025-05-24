package juegoUno;

import java.util.*;

public class Juego {
    private final Map<String, Jugador> jugadores;
    private final Deque<Carta> mazoDeCartas;
    private Carta cartaVisible;
    private Jugador jugadorActual;
    private ControladorTurnos controlador;

    public static final String EXCEPTION_JUEGO_TERMINADO = "El juego ya terminó. No se pueden jugar ni robar más cartas.";
    public static final String EXCEPTION_INTENTA_JUGAR_CARTA_QUE_NO_TIENE = "El jugador no posee la carta indicada.";
    public static final String EXCEPTION_INTENTA_JUGAR_CARTA_INCOMPATIBLE = "La carta no es compatible con la carta visible en la mesa.";
    public static final String EXCEPTION_INTENTA_ROBAR_PERO_PUEDE_JUGAR = "El jugador tiene cartas jugables, no puede pedir una.";
    public static final String EXCEPTION_SE_ACABO_POZO = "No hay más cartas para robar";

    public Juego(List<Carta> mazo, int cartasPorJugador, String... nombres) {
        this.jugadores = new LinkedHashMap<>();
        this.mazoDeCartas = new ArrayDeque<>(mazo);

        List<Jugador> lista = Arrays.stream(nombres)
                .map(Jugador::new)
                .toList();
        lista.forEach(j -> jugadores.put(j.nombre(), j));

        for (int i = 0; i < lista.size(); i++) {
            Jugador actual = lista.get(i);
            actual.conectarCon(lista.get((i + 1) % lista.size()), lista.get((i - 1 + lista.size()) % lista.size()));
        }

        this.jugadorActual = lista.getFirst();
        this.controlador = crearControladores();

        for (int i = 0; i < cartasPorJugador; i++) {
            for (Jugador j : lista) {
                j.recibirCarta(mazoDeCartas.removeFirst());
            }
        }

        this.cartaVisible = mazoDeCartas.removeFirst();
    }

    private ControladorTurnos crearControladores() {
        ControladorDerecha d = new ControladorDerecha();
        ControladorIzquierda i = new ControladorIzquierda();
        d.setOpuesto(i);
        i.setOpuesto(d);
        return d;
    }

    public Juego jugar(String nombreJugador, Carta carta) {
        if (termino()) {
            throw new RuntimeException(EXCEPTION_JUEGO_TERMINADO);
        }

        validarTurno(nombreJugador);

        if (!jugadorActual.tiene(carta)) {
            throw new RuntimeException(EXCEPTION_INTENTA_JUGAR_CARTA_QUE_NO_TIENE);
        }

        if (!carta.aceptaCarta(cartaVisible)) {
            throw new RuntimeException(EXCEPTION_INTENTA_JUGAR_CARTA_INCOMPATIBLE);
        }

        carta.validarAntesDeJugar();
        aplicarCartaJugable(carta);
        return this;
    }

    private void validarTurno(String nombre) {
        if (!jugadorActual.nombre().equals(nombre)) {
            throw new RuntimeException("No es el turno de " + nombre);
        }
    }

    public Carta obtenerCartaDelPozo() {
        return cartaVisible;
    }

    public int cantidadCartas(String jugador) {
        return jugadores.get(jugador).cantidadCartas();
    }

    public boolean termino() {
        return jugadores.values().stream().anyMatch(Jugador::sinCartas);
    }

    public void robarCartaSiNoTiene(String nombreJugador) {
        if (termino()) {
            throw new RuntimeException(EXCEPTION_JUEGO_TERMINADO);
        }
        validarTurno(nombreJugador);

        if (jugadorActual.puedeJugarSobre(cartaVisible)) {
            throw new RuntimeException(EXCEPTION_INTENTA_ROBAR_PERO_PUEDE_JUGAR);
        }

        if (mazoDeCartas.isEmpty()) {
            throw new RuntimeException(EXCEPTION_SE_ACABO_POZO);
        }

        Carta cartaRobada = mazoDeCartas.removeFirst();
        jugadorActual.recibirCarta(cartaRobada);

        if (cartaRobada.aceptaCarta(cartaVisible)) {
            aplicarCartaJugable(cartaRobada);
        } else {
            jugadorActual = controlador.siguiente(jugadorActual);
        }
    }

    private void aplicarCartaJugable(Carta cartaRobada) {
        jugadorActual.jugarCarta(cartaRobada);
        cartaVisible = cartaRobada;
        penalizarSiNoCantoUno(cartaRobada);
        cartaRobada.aplicarEfecto(this, jugadorActual);
    }

    private void penalizarSiNoCantoUno(Carta cartaRobada) {
        if (jugadorActual.cantidadCartas() == 1 && !cartaRobada.cantoUno()) {
            robarCartas(jugadorActual, 2);
        }
    }

    public void cambiarSentido() {
        this.controlador = this.controlador.opuesto();
    }

    public void pasarTurno(Jugador jugadorActual) {
        this.jugadorActual = controlador.siguiente(jugadorActual);
    }

    public void saltarSiguienteJugador(Jugador jugadorActual) {
        this.jugadorActual = controlador.siguiente(controlador.siguiente(jugadorActual));
    }

    public Jugador obtenerSiguienteJugador(Jugador jugadorActual) {
        return controlador.siguiente(jugadorActual);
    }

    public void robarCartas(Jugador jugador, int cantidad) {
        for (int i = 0; i < cantidad && !mazoDeCartas.isEmpty(); i++) {
            jugador.recibirCarta(mazoDeCartas.removeFirst());
        }
    }
}


