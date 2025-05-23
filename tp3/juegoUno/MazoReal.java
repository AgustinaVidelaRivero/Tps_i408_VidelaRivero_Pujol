package juegoUno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazoReal {
    public static List<Carta> generarMazoCompleto() {
        List<Carta> mazo = new ArrayList<>();

        List<String> colores = List.of(Carta.ROJO, Carta.AZUL, Carta.VERDE, Carta.AMARILLO);

        for (String color : colores) {
            // 1 carta 0
            mazo.add(CartaNumerada.with(color, 0));

            // 2 de cada número 1 al 9
            for (int n = 1; n <= 9; n++) {
                mazo.add(CartaNumerada.with(color, n));
                mazo.add(CartaNumerada.with(color, n));
            }

            // 2 Skips, 2 Reverse, 2 +2
            mazo.add(CartaSaltea.with(color));
            mazo.add(CartaSaltea.with(color));
            mazo.add(CartaReversa.with(color));
            mazo.add(CartaReversa.with(color));
            mazo.add(CartaMasDos.with(color));
            mazo.add(CartaMasDos.with(color));
        }

        // 4 Wildcards
        for (int i = 0; i < 4; i++) {
            mazo.add(CartaComodin.with());
        }

        Collections.shuffle(mazo);
        return mazo;
    }
}