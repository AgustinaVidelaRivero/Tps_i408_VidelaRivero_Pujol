package org.udesa.unoback.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.udesa.unoback.service.Dealer;
import org.udesa.unoback.service.UnoService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class UnoServiceTest {

    @Autowired
    UnoService service;

    @MockBean
    Dealer dealer;

    @BeforeEach
    public void setup() {
        doReturn(baseDeckWithExtra(null)).when(dealer).fullDeck();
    }

    private List<Card> baseDeckWithExtra(Card extraCard) {
        List<Card> base = List.of(
                new NumberCard("Red", 0),
                new NumberCard("Red", 1), new NumberCard("Red", 2), new NumberCard("Red", 3),
                new NumberCard("Red", 4), new NumberCard("Red", 5), new NumberCard("Red", 6), new NumberCard("Red", 7),
                new NumberCard("Blue", 1), new NumberCard("Blue", 2), new NumberCard("Blue", 3),
                new NumberCard("Blue", 4), new NumberCard("Blue", 5), new NumberCard("Blue", 6), new NumberCard("Blue", 7)
        );
        if (extraCard != null) {
            return new java.util.ArrayList<>() {{
                addAll(base);
                add(extraCard);
            }};
        }
        return base;
    }

    @Test
    public void test01ShouldReturnSevenCardsWhenMatchStarts() {
        UUID id = service.newMatch(List.of("Agus", "Trini"));
        Collection<JsonCard> hand = service.playerHand(id);
        assertEquals(7, hand.size());
    }

    @Test
    public void test02PlayingMatchingCardShouldUpdateActiveCardToIt() {
        UUID id = service.newMatch(List.of("Agus", "Trini"));
        JsonCard card = service.playerHand(id).iterator().next();
        service.play(id, "Agus", card);
        JsonCard active = service.activeCard(id);
        assertEquals(card.getColor(), active.getColor());
        assertEquals(card.getType(), active.getType());
    }

    @Test
    public void test03ShouldThrowExceptionWhenPlayingCardNotInHand() {
        UUID id = service.newMatch(List.of("Agus", "Trini"));
        JsonCard notInHand = new JsonCard("Red", 99, "NumberCard", false);
        assertThrows(RuntimeException.class, () -> {
            service.play(id, "Agus", notInHand);
        });
    }

    @Test
    public void test04ShouldThrowWhenPlayerTriesToPlayOutOfTurn() {
        UUID matchId = service.newMatch(List.of("Agus", "Trini"));
        JsonCard cardFromAgus = service.playerHand(matchId).iterator().next();
        assertThrows(RuntimeException.class, () -> {
            service.play(matchId, "Trini", cardFromAgus);
        });
    }

    @Test
    public void test05ShouldIncreaseHandAfterDraw() {
        doReturn(baseDeckWithExtra(new NumberCard("Green", 9))).when(dealer).fullDeck();
        UUID id = service.newMatch(List.of("Agus", "Trini"));
        int before = service.playerHand(id).size();
        service.draw(id, "Agus");
        int after = service.playerHand(id).size();
        assertEquals(before + 1, after);
    }

    @Test
    public void test06ShouldEndGameWhenPlayerHasNoCardsLeft() {
        doReturn(List.of(
                new NumberCard("Red", 0),
                new NumberCard("Red", 1),
                new NumberCard("Blue", 1)
        )).when(dealer).fullDeck();

        UUID id = service.newCustomMatch(dealer.fullDeck(), 1, List.of("Agus", "Trini"));
        JsonCard card = service.playerHand(id).iterator().next();
        service.play(id, "Agus", card);
        assertTrue(service.isOver(id));
    }

    @Test
    public void test07ShouldThrowWhenDrawingWithEmptyDeck() {
        UUID id = service.newMatch(List.of("Agus", "Trini"));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            service.draw(id, "Agus");
        });
    }
}