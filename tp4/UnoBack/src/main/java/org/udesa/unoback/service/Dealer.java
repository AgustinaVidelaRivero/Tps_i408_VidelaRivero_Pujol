package org.udesa.unoback.service;

import org.springframework.stereotype.Component;
import org.udesa.unoback.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Dealer {

    public List<Card> fullDeck() {
        List<Card> deck = new ArrayList<>();
//        for (String color : List.of("Red", "Blue", "Green", "Yellow")) {
//            deck.addAll(cardsOn(color));
//        }

        //generar cartas Red
        deck.addAll(cardsOn("Red"));
        //generar cartas Blue
        deck.addAll(cardsOn("Blue"));
        //generar cartas Yellow
        deck.addAll(cardsOn("Yellow"));
        //generar cartas Green
        deck.addAll(cardsOn("Green"));
        // 4 comodines sin color
        for (int i = 0; i < 4; i++) {
            deck.add(new WildCard());
        }

        Collections.shuffle(deck); //las mezclo
        return deck;
    }

    private List<Card> cardsOn(String color) { // mazoreal segun las reglas
        List<Card> cards = new ArrayList<>();
        // 1 cero
        cards.add(new NumberCard(color, 0));

        //2para cada número del 1 al 9
        for (int i = 1; i <= 9; i++) {
            cards.add(new NumberCard(color, i));
            cards.add(new NumberCard(color, i));
        }
        //2 Skip
        cards.add(new SkipCard(color));
        cards.add(new SkipCard(color));
        //2 Draw 2
        cards.add(new Draw2Card(color));
        cards.add(new Draw2Card(color));
        //2Reverse
        cards.add(new ReverseCard(color));
        cards.add(new ReverseCard(color));

        return cards;
    }
}
