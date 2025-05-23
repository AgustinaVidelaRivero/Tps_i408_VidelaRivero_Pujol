package juegoUnoAgus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JuegoUnoTests {
    private Carta rojo2, rojo4, azul2, azul4, verde4, verde5, skipVerde;
    private Carta reverseRojo;
    private Carta reverseAzul;
    private Carta skipRojo;
    private Carta skipAzul;
    private Carta masDosRojo;
    private Carta comodinSinColor;
    private Carta comodinRojo;

    @BeforeEach
    public void setup() {
        rojo2 = cartaNumero(Carta.ROJO, 2);
        rojo4 = cartaNumero(Carta.ROJO, 4);
        azul2 = cartaNumero(Carta.AZUL, 2);
        azul4 = cartaNumero(Carta.AZUL, 4);
        verde4 = cartaNumero(Carta.VERDE, 4);
        verde5 = cartaNumero(Carta.VERDE, 5);

        skipVerde = skip(Carta.VERDE);
        skipRojo = skip(Carta.ROJO);
        skipAzul = skip(Carta.AZUL);

        reverseRojo = reverse(Carta.ROJO);
        reverseAzul = reverse(Carta.AZUL);

        masDosRojo = masDos(Carta.ROJO);


        comodinRojo = wildcard(Carta.ROJO);
        comodinSinColor = CartaComodin.with();
    }

    @Test
    public void test01PozoInicialTieneLaCartaCorrecta() {
        assertEquals(rojo4,
                new Juego(List.of(rojo2, azul2, verde4, rojo4, rojo4), 2, "A", "B").obtenerCartaDelPozo());
    }

    @Test public void test02JugadorPuedeJugarCartaPorColor() {
        assertEquals(rojo2,
                        new Juego(List.of(rojo2, rojo4, azul2, verde4, rojo2), 2, "A", "B")
                                .jugar("A", rojo2).obtenerCartaDelPozo());
    }


    @Test public void test03JugadorNoPuedeJugarCartaIncompatible() {
        assertThrows(RuntimeException.class,
                () -> new Juego(List.of(
                                        azul2,                       // A recibe esta
                                        verde5,
                                        rojo4 )                       // esta va al pozo
                                , 1, "A", "B").jugar("A", azul2));
    }

    @Test public void test04JugadorPuedeJugarCartaPorNumero() {
        assertEquals(azul2,
                        new Juego(List.of(azul2, rojo4, verde4, rojo2, rojo2), 2, "A", "B")
                                .jugar("A", azul2).obtenerCartaDelPozo());
    }

    @Test public void test05JugadorNoPuedeRepetirLaMismaCarta() {
        Juego juego = new Juego(List.of(rojo4, rojo4, azul2, verde4, rojo2), 2, "A", "B");
        juego.jugar("A", rojo4);
        assertEquals(rojo4, juego.obtenerCartaDelPozo());
        assertThrows(RuntimeException.class, () -> juego.jugar("A", rojo4));
    }

    @Test public void test06JugadorPuedeJugarWildcardAsignandoColor() {
        Juego juego = new Juego(List.of(
                comodinRojo, // recibe comodin  asignado como rojo
                azul2,
                rojo2      // carta inicial del pozo
        ), 1, "A", "B");

        juego.jugar("A", CartaComodin.with().asignarColor(Carta.ROJO));
        assertEquals(Carta.ROJO, juego.obtenerCartaDelPozo().obtenerColor());
        // B no puede jugar azul2, porque el pozo es rojo
        assertThrows(RuntimeException.class, () -> juego.jugar("B", azul2));
    }

    @Test public void test07NoSePuedeJugarWildcardSinColor() {
        Juego juego = new Juego(List.of(
                comodinSinColor,
                azul2,
                rojo2
        ), 1, "A", "B");
        // Al no tener color, debe lanzar excepción al intentar jugarla
        assertThrows(RuntimeException.class, () -> juego.jugar("A", comodinSinColor));
    }

    @Test public void test08JugadorPuedeApoyarMasDosSobreMismoTipoODelMismoColor() {
        Juego juego = new Juego(List.of(
                masDosRojo,  // jugador A
                verde4,      // jugador B
                rojo2        // pozo inicial
        ), 1, "A", "B");
        // A juega +2 rojo sobre rojo2 (por color)
        juego.jugar("A", masDosRojo);
        assertEquals(masDosRojo, juego.obtenerCartaDelPozo());
        // B no puede jugar verde4 (ni tipo ni color)
        assertThrows(RuntimeException.class, () -> juego.jugar("B", verde4));
    }

    @Test public void test09NoSePuedeApoyarCartaNormalSobreMasDosIncompatible() {
        assertThrows(RuntimeException.class, () -> new Juego(List.of(
                azul4, //A
                rojo2,//B
                masDosRojo
        ), 1, "A", "B").jugar("A", azul4));
    }

    @Test public void test10JugadorPuedeApoyarReversePorColor() {
        assertEquals(reverseRojo, new Juego(List.of(
                                                    reverseRojo,   // jugador A
                                                    azul2,         // jugador B
                                                    rojo2 )         // pozo inicial
        , 1, "A", "B").jugar("A", reverseRojo).obtenerCartaDelPozo());
    }

    @Test public void test11JugadorNoPuedeApoyarReverseIncompatible() {
        assertThrows(RuntimeException.class, () -> new Juego(List.of(
                reverseAzul,  // jugador A
                rojo2,        //  B
                verde4        // pozo inicial
        ), 1, "A", "B").jugar("A", reverseAzul));
    }

    @Test public void test12JugadorPuedeApoyarSkipPorColor() {
        assertEquals(skipRojo, new Juego(List.of(
                                                skipRojo,
                                                azul2,
                                                rojo2),
                1, "A", "B").jugar("A", skipRojo).obtenerCartaDelPozo());
    }

    @Test public void test13JugadorNoPuedeApoyarSkipIncompatible() {
        assertThrows(RuntimeException.class, () -> new Juego(List.of(
                skipAzul,
                rojo2,
                verde4
        ), 1, "A", "B").jugar("A", skipAzul));
    }

    @Test public void test14ReverseConTresJugadoresInvierteElSentido() {
        Juego juego = new Juego(List.of(
                reverseRojo, //A
                rojo2,  //B
                azul2, //C
                rojo2,    //A
                rojo2, //B
                rojo2,    //C
                rojo4, rojo2, rojo2 //POZO
        ), 2, "A", "B", "C");
        juego.jugar("A", reverseRojo);
        juego.jugar("C", rojo2);

        assertEquals(rojo2, juego.obtenerCartaDelPozo());
    }

    @Test public void test14ReverseConCuatroJugadoresSaltaCorrectamente() {
        assertEquals(rojo2,  new Juego(List.of(reverseRojo, //A
                                                rojo2,   // B
                                                rojo2, //C
                                                rojo2,         // D
                                                rojo2, //A
                                                rojo2,         // B
                                                rojo2, //C
                                                rojo2,         // D
                                                rojo4), //POZO
                                                     2, "A", "B", "C", "D")
                .jugar("A", reverseRojo)
                .jugar("D", rojo2)
                .obtenerCartaDelPozo());
    }

    @Test public void test15SkipConTresJugadoresSaltaJugadorSiguiente() {
        assertEquals(verde5,  new Juego(List.of(skipVerde, //A
                                                rojo2,     // B
                                                azul2, //C
                                                rojo2,         // A
                                                verde4, //B
                                                verde5,        // C
                                                verde5), //POZO
                                 2, "A", "B", "C")
                                        .jugar("A", skipVerde)
                                        .jugar("C", verde5)
                                        .obtenerCartaDelPozo()); // B salteado
    }

    @Test public void test16JugadorRecibePenalidadPorNoCantarUno() {
        assertEquals(3, new Juego(List.of(rojo2, //A
                                                    azul2,   //B
                                                    rojo2, //A
                                                    verde4,  //B
                                                    rojo4, verde4, azul2), //POZO
                                            2, "A", "B")
                                            .jugar("A", rojo2)
                                            .cantidadCartas("A"));
    }

    @Test public void test17JugadorCantaUnoYNoRecibePenalidad() {
        assertEquals(1, new Juego(List.of(rojo2.uno(), //A
                                                    azul2,  //B
                                                    rojo2, //A
                                                    rojo4,  //B
                                                    rojo4, verde4), //POZO
         2, "A", "B").jugar("A", rojo2.uno()).cantidadCartas("A"));
    }

    @Test public void test18JugadorLevantaCartaYNoPuedeJugarla() {

        Juego juego = new Juego(List.of(
                azul4, verde4,
                rojo2, rojo2,
                verde5,                // pozo inicial
                azul2                 // carta que roba A
        ), 2, "A", "B");
        juego.levantaCarta("A");
        // A debe tener 3 cartas ahora
        assertEquals(3, juego.cantidadCartas("A"));
        // Pozo sigue siendo rojo4
        assertEquals(verde5, juego.obtenerCartaDelPozo());
    }

    @Test public void test19JugadorLevantaCartaYPuedeJugarla() {
        Juego juego = new Juego(List.of(
                azul4, //A
                verde4,  //B
                rojo2, //A
                verde5,  // B
                verde5,          // carta del pozo
                verde5          // esta se roba
        ), 2, "A", "B");
        juego.levantaCarta("A");
        // A robó verde5 y la jugó (compatible con rojo2), así que vuelve a tener 2
        assertEquals(2, juego.cantidadCartas("A"));
        assertEquals(verde5, juego.obtenerCartaDelPozo());
    }

    //NO SE SI HACE FALTA HACER ESTO, pero seria para jugar con un mazo real haciendo:
    //Juego juego = new Juego(MazoFactory.generarMazoCompleto(), 7, "A", "B"); por ejemplo
    @Test public void test20CantidadCartasEnMazoCompleto() {
        List<Carta> mazo = MazoReal.generarMazoCompleto();
        assertEquals(104, mazo.size()); // no implementamos las wildcard con +4
    }

    @Test public void test21JugadorGanaCuandoSeQuedaSinCartas() {
        Juego juego = new Juego(List.of(
                                        rojo2, // A
                                        azul2,     // B
                                        rojo4      // pozo
        ), 1, "A", "B");
        juego.jugar("A", rojo2);
        assertTrue(juego.termino());
        assertEquals("A", juego.ganador());
    }

    //VER SI DEJAMOS ESTO SEGUN LO QUE RESPONDA EMILIO
    @Test public void testNoSePuedeJugarLuegoDeTerminadoElJuego() {

        Juego juego = new Juego(List.of(
                                        rojo2, // A
                                        azul2,     // B
                                        rojo4      // pozo
        ), 1, "A", "B");
        juego.jugar("A", rojo2); // A gana

        assertTrue(juego.termino());
        assertEquals("A", juego.ganador());
        // B intenta jugar luego de terminado el juego
        assertThrows(RuntimeException.class, () -> juego.jugar("B", azul2));
    }


    private Carta cartaNumero(String color, int valor) {
        return CartaNumerada.with(color, valor);
    }

    private Carta skip(String color) {
        return CartaSaltea.with(color);
    }

    private Carta reverse(String color) {
        return CartaReversa.with(color);
    }

    private Carta masDos(String color) {
        return CartaMasDos.with(color);
    }

    private Carta wildcard(String color) {
        return CartaComodin.with().asignarColor(color);
    }


}




