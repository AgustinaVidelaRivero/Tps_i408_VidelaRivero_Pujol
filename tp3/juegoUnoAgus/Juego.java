package juegoUnoAgus;

import java.util.*;

public class Juego {
    private final Map<String, Jugador> jugadores;
    private final Deque<Carta> pilaDeCartas;
    private Carta cartaDelPozo;
    private Jugador jugadorActual;
    private ControladorTurnos controlador;

    public Juego(List<Carta> mazo, int cartasPorJugador, String... nombres) {
        this.jugadores = new LinkedHashMap<>();
        this.pilaDeCartas = new ArrayDeque<>(mazo);

        List<Jugador> lista = new ArrayList<>();
        for (String nombre : nombres) {
            Jugador jugador = new Jugador(nombre);
            jugadores.put(nombre, jugador);
            lista.add(jugador);
        }

        for (int i = 0; i < lista.size(); i++) {
            Jugador actual = lista.get(i);
            Jugador derecha = lista.get((i + 1) % lista.size());
            Jugador izquierda = lista.get((i - 1 + lista.size()) % lista.size());
            actual.setDerecha(derecha);
            actual.setIzquierda(izquierda);
        }

        this.jugadorActual = lista.getFirst();
        this.controlador = crearControladores();

        for (int i = 0; i < cartasPorJugador; i++) {
            for (Jugador j : lista) {
                j.recibirCarta(pilaDeCartas.removeFirst());
            }
        }

        this.cartaDelPozo = pilaDeCartas.removeFirst();
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
            throw new RuntimeException("El juego ya terminó. No se pueden jugar más cartas.");
        }

        validarTurno(nombreJugador);

        if (!jugadorActual.tiene(carta)) {
            throw new RuntimeException("El jugador no tiene esa carta");
        }

        if (!carta.aceptaCarta(cartaDelPozo)) {
            throw new RuntimeException("La carta no es compatible con el pozo");
        }

        if (carta instanceof CartaComodin wildcard && wildcard.obtenerColor() == null) {
            throw new RuntimeException("Wildcard sin color asignado");
        }

        jugadorActual.jugarCarta(carta);
        cartaDelPozo = carta;

        if (jugadorActual.cantidadCartas() == 1 && !carta.cantoUno()) {
            robarCartas(jugadorActual, 2);
        }

        carta.aplicarEfecto(this, jugadorActual);
        return this;
    }

    private void validarTurno(String nombre) {
        if (!jugadorActual.nombre().equals(nombre)) {
            throw new RuntimeException("No es el turno de " + nombre);
        }
    }

    public Carta obtenerCartaDelPozo() {
        return cartaDelPozo;
    }

    public int cantidadCartas(String jugador) {
        return jugadores.get(jugador).cantidadCartas();
    }

    public boolean termino() {
        return jugadores.values().stream().anyMatch(Jugador::sinCartas);
    }

    public Juego levantaCarta(String nombreJugador) {
        if (termino()) {
            throw new RuntimeException("El juego ya terminó.");
        }

        validarTurno(nombreJugador);

        if (jugadorActual.tieneAlMenosUnaCartaJugable(cartaDelPozo)) {
            throw new RuntimeException("El jugador tiene cartas jugables, no puede pedir una.");
        }

        if (pilaDeCartas.isEmpty()) {
            throw new RuntimeException("No hay más cartas para robar");
        }

        Carta cartaRobada = pilaDeCartas.removeFirst();
        jugadorActual.recibirCarta(cartaRobada);

        if (cartaRobada.aceptaCarta(cartaDelPozo)) {
            jugadorActual.jugarCarta(cartaRobada);
            cartaDelPozo = cartaRobada;

            if (jugadorActual.cantidadCartas() == 1 && !cartaRobada.cantoUno()) {
                robarCartas(jugadorActual, 2);
            }

            cartaRobada.aplicarEfecto(this, jugadorActual);
        } else {
            jugadorActual = controlador.siguiente(jugadorActual);
        }

        return this;
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
        for (int i = 0; i < cantidad && !pilaDeCartas.isEmpty(); i++) {
            jugador.recibirCarta(pilaDeCartas.removeFirst());
        }
    }
}


