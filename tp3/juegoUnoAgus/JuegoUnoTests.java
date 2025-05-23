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

    @BeforeEach
    public void setup() {
        rojo2 = cartaNumero("rojo", 2);
        rojo4 = cartaNumero("rojo", 4);
        azul2 = cartaNumero("azul", 2);
        azul4 = cartaNumero("azul", 4);
        verde4 = cartaNumero("verde", 4);
        verde5 = cartaNumero("verde", 5);

        skipVerde = skip("verde");
        skipRojo = skip("rojo");
        skipAzul = skip("azul");

        reverseRojo = reverse("rojo");
        reverseAzul = reverse("azul");

        masDosRojo = masDos("rojo");
        Carta wildcardRojo = wildcard("rojo");

        List<String> jugadoresABC = List.of("A", "B", "C");
        List<String> jugadoresABCD = List.of("A", "B", "C", "D");
    }

    @Test
    public void test01PozoInicialTieneLaCartaCorrecta() {
        List<Carta> mazo = List.of(rojo2, azul2, verde4, rojo4, rojo4);
        Juego juego = new Juego(mazo, 2, "A", "B");

        assertEquals(rojo4, juego.obtenerCartaDelPozo());
    }

    @Test
    public void test02JugadorPuedeJugarCartaPorColor() {
        List<Carta> mazo = List.of(rojo2, rojo4, azul2, verde4, rojo2);
        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.jugar("A", rojo2);
        assertEquals(rojo2, juego.obtenerCartaDelPozo());
    }


    @Test public void test03JugadorNoPuedeJugarCartaIncompatible() {
        List<Carta> mazo = List.of(
                azul2,                       // A recibe esta
                //CartaNumerada.with(Color.VERDE, 5), // B recibe esta
                verde5,
                rojo4                        // esta va al pozo
        );
        Juego juego = new Juego(mazo, 1, "A", "B");
        assertThrows(RuntimeException.class, () -> juego.jugar("A", azul2));
    }

    @Test public void test04JugadorPuedeJugarCartaPorNumero() {
        List<Carta> mazo = List.of(azul2, rojo4, verde4, rojo2, rojo2);
        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.jugar("A", azul2);
        assertEquals(azul2, juego.obtenerCartaDelPozo());
    }

    @Test public void test05JugadorNoPuedeRepetirLaMismaCarta() {
        Carta r4a = rojo4;
        Carta r4b = rojo4;
        List<Carta> mazo = List.of(r4a, r4b, azul2, verde4, rojo2);
        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.jugar("A", r4a);
        assertEquals(r4a, juego.obtenerCartaDelPozo());

        assertThrows(RuntimeException.class, () -> juego.jugar("A", r4a));
    }

    @Test public void test06JugadorPuedeJugarWildcardAsignandoColor() {
        Juego juego = new Juego(List.of(
                CartaComodin.with().asignarColor(Color.ROJO),    // jugador A recibe esta wildcard ya asignada como roja
                azul2,     // jugador B
                rojo2      // carta inicial del pozo
        ), 1, "A", "B");

        juego.jugar("A", CartaComodin.with().asignarColor(Color.ROJO));

        assertEquals(Color.ROJO, juego.obtenerCartaDelPozo().obtenerColor());

        // B no puede jugar azul2, porque el pozo es rojo
        assertThrows(RuntimeException.class, () -> juego.jugar("B", azul2));
    }
//    @Test public void test06JugadorPuedeJugarWildcardAsignandoColor() {
//        Juego juego = new Juego(List.of(
//                CartaComodin.with().asignarColor(Carta.ROJO), // jugador A
//                azul2,
//                rojo2
//        ), 1, "A", "B");
//
//        juego.jugar("A", CartaComodin.with().asignarColor(Carta.ROJO));
//
//        assertEquals(Carta.ROJO, juego.obtenerCartaDelPozo().obtenerColor());
//
//        assertThrows(RuntimeException.class, () -> juego.jugar("B", azul2));
//    }

    @Test public void test07NoSePuedeJugarWildcardSinColor() {
        List<Carta> mazo = List.of(
                CartaComodin.with(),
                azul2,
                rojo2
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        // Al no tener color, debe lanzar excepción al intentar jugarla
        assertThrows(RuntimeException.class, () -> juego.jugar("A", CartaComodin.with()));
    }

    @Test public void test08JugadorPuedeApoyarMasDosSobreMismoTipoODelMismoColor() {
        List<Carta> mazo = List.of(
                masDosRojo,  // jugador A
                verde4,      // jugador B
                rojo2        // pozo inicial
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        // A juega +2 rojo sobre rojo2 (por color)
        juego.jugar("A", masDosRojo);
        assertEquals(masDosRojo, juego.obtenerCartaDelPozo());

        // B no puede jugar verde4 (ni tipo ni color)
        assertThrows(RuntimeException.class, () -> juego.jugar("B", verde4));
    }

    @Test public void test09NoSePuedeApoyarCartaNormalSobreMasDosIncompatible() {
        List<Carta> mazo = List.of(
                azul4, //A
                rojo2,//B
                masDosRojo
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        // A intenta jugar azul4 sobre +2 rojo, pero no coincide en tipo ni color
        assertThrows(RuntimeException.class, () -> juego.jugar("A", azul4));
    }

    @Test public void test10JugadorPuedeApoyarReversePorColor() {
        List<Carta> mazo = List.of(
                reverseRojo,   // jugador A
                azul2,         // jugador B
                rojo2          // pozo inicial
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        juego.jugar("A", reverseRojo);
        assertEquals(reverseRojo, juego.obtenerCartaDelPozo());
    }

    @Test public void test11JugadorNoPuedeApoyarReverseIncompatible() {
        List<Carta> mazo = List.of(
                reverseAzul,  // jugador A
                rojo2,        // jugador B
                verde4        // pozo inicial
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        // Reverse azul no coincide ni en color ni en tipo con verde4
        assertThrows(RuntimeException.class, () -> juego.jugar("A", reverseAzul));
    }

    @Test
    public void test12JugadorPuedeApoyarSkipPorColor() {
        List<Carta> mazo = List.of(
                skipRojo,
                azul2,
                rojo2
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        juego.jugar("A", skipRojo);
        assertEquals(skipRojo, juego.obtenerCartaDelPozo());
    }

    @Test public void test13JugadorNoPuedeApoyarSkipIncompatible() {
        List<Carta> mazo = List.of(
                skipAzul,
                rojo2,
                verde4
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        // Skip azul no coincide ni en color ni en tipo con verde4
        assertThrows(RuntimeException.class, () -> juego.jugar("A", skipAzul));
    }

    @Test public void test14ReverseConTresJugadoresInvierteElSentido() {
        List<Carta> mazo = List.of(
                reverseRojo, //A
                rojo2,  //B
                azul2, //C
                rojo2,    //A
                rojo2, //B
                rojo2,    //C
                rojo4, rojo2, rojo2 //POZO
        );
        Juego juego = new Juego(mazo, 2, "A", "B", "C");

        juego.jugar("A", reverseRojo);
        juego.jugar("C", rojo2);

        assertEquals(rojo2, juego.obtenerCartaDelPozo());
    }

    @Test public void test14ReverseConCuatroJugadoresSaltaCorrectamente() {
        List<Carta> mazo = List.of(
                reverseRojo, //A
                rojo2,   // B
                rojo2, //C
                rojo2,         // D
                rojo2, //A
                rojo2,         // B
                rojo2, //C
                rojo2,         // D
                rojo4 //POZO
        );
        Juego juego = new Juego(mazo, 2, "A", "B", "C", "D");

        juego.jugar("A", reverseRojo);
        juego.jugar("D", rojo2);

        assertEquals(rojo2, juego.obtenerCartaDelPozo());
    }

    @Test public void test15SkipConTresJugadoresSaltaJugadorSiguiente() {
        List<Carta> mazo = List.of(
                skipVerde, //A
                rojo2,     // B
                azul2, //C
                rojo2,         // A
                verde4, //B
                verde5,        // C
                verde5 //POZO
        );
        Juego juego = new Juego(mazo, 2, "A", "B", "C");

        juego.jugar("A", skipVerde); // B salteado
        juego.jugar("C", verde5);

        assertEquals(verde5, juego.obtenerCartaDelPozo());
    }

    @Test
    public void test16JugadorRecibePenalidadPorNoCantarUno() {


        List<Carta> mazo = List.of(
                rojo2, azul2,     // A: c0, B: c1
                rojo2, verde4,  // A: c2, B: c3
                rojo4, verde4, azul2    // c4 → pozo (rojo4), c5 extra
        );

        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.jugar("A", rojo2); // le queda una sola carta y no cantó UNO

        assertEquals(3, juego.cantidadCartas("A"));
    }

    @Test
    public void test17JugadorCantaUnoYNoRecibePenalidad() {


        List<Carta> mazo = List.of(
                rojo2.uno(), azul2,  // jugador A: rojo2a, azul2
                rojo2, rojo4,  // jugador B: rojo2b, rojo4
                rojo4, verde4 // cartas extra
        );

        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.jugar("A", rojo2.uno());

        assertEquals(1, juego.cantidadCartas("A"));
    }

    @Test public void test18JugadorLevantaCartaYNoPuedeJugarla() {
        List<Carta> mazo = List.of(
                azul4, verde4,        // A: cartas no compatibles
                rojo2, rojo2,     // B: cualquiera
                rojo4,                // pozo inicial (rojo)
                azul2                 // carta que roba A (incompatible con pozo rojo4)
        );

        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.levantaCarta("A");

        // A debe tener 3 cartas ahora
        assertEquals(3, juego.cantidadCartas("A"));

        // Pozo sigue siendo rojo4
        assertEquals(rojo4, juego.obtenerCartaDelPozo());
    }

    @Test public void test19JugadorLevantaCartaYPuedeJugarla() {

        List<Carta> mazo = List.of(
                azul4, verde4,        // A: cartas no compatibles
                rojo2, rojo2,     // B: cualquiera
                azul2,                // carta que roba A (sí compatible con pozo azul4)
                azul4                 // pozo inicial (azul4)
        );

        Juego juego = new Juego(mazo, 2, "A", "B");

        juego.levantaCarta("A");

        // A jugó la carta robada, por lo tanto sigue con 2
        assertEquals(2, juego.cantidadCartas("A"));

        // Pozo debe actualizarse a azul4 --> la que levanto y tiro A
        assertEquals(azul4, juego.obtenerCartaDelPozo());
    }

    //NO SE SI HACE FALTA HACER ESTO, pero seria para jugar con un mazo real haciendo:
    //Juego juego = new Juego(MazoFactory.generarMazoCompleto(), 7, "A", "B"); por ejemplo
    @Test
    public void test20CantidadCartasEnMazoCompleto() {
        List<Carta> mazo = MazoReal.generarMazoCompleto();
        assertEquals(104, mazo.size()); // no implementamos las wildcard con +4
    }

    @Test
    public void test21JugadorGanaCuandoSeQuedaSinCartas() {
        List<Carta> mazo = List.of(
                rojo2, // A
                azul2,     // B
                rojo4      // pozo
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        juego.jugar("A", rojo2);

        assertTrue(juego.termino());
        assertEquals("A", juego.ganador());
    }

    //VER SI DEJAMOS ESTO SEGUN LO QUE RESPONDA EMILIO
    @Test
    public void testNoSePuedeJugarLuegoDeTerminadoElJuego() {
        List<Carta> mazo = List.of(
                rojo2, // A
                azul2,     // B
                rojo4      // pozo
        );

        Juego juego = new Juego(mazo, 1, "A", "B");

        juego.jugar("A", rojo2); // A gana

        assertTrue(juego.termino());
        assertEquals("A", juego.ganador());

        // B intenta jugar luego de terminado el juego
        assertThrows(RuntimeException.class, () -> juego.jugar("B", azul2));
    }

    private Carta cartaNumero(String color, int valor) {
        return CartaNumerada.with(Color.valueOf(color.toUpperCase()), valor);
    }

    private Carta skip(String color) {
        return CartaSaltea.with(Color.valueOf(color.toUpperCase()));
    }

    private Carta reverse(String color) {
        return CartaReversa.with(Color.valueOf(color.toUpperCase()));
    }

    private Carta masDos(String color) {
        return CartaMasDos.with(Color.valueOf(color.toUpperCase()));
    }

    private Carta wildcard(String color) {
        return CartaComodin.with().asignarColor(Color.valueOf(color.toUpperCase()));
    }

//    private Carta cartaNumero(String color, int valor) {
//        return CartaNumerada.with(color.toLowerCase(), valor);
//    }
//
//    private Carta skip(String color) {
//        return CartaSaltea.with(color.toLowerCase());
//    }
//
//    private Carta reverse(String color) {
//        return CartaReversa.with(color.toLowerCase());
//    }
//
//    private Carta masDos(String color) {
//        return CartaMasDos.with(color.toLowerCase());
//    }
//
//    private Carta wildcard(String color) {
//        return CartaComodin.with().asignarColor(color.toLowerCase());
//    }

}




