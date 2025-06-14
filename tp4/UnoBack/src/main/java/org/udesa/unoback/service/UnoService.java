package org.udesa.unoback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udesa.unoback.model.*;

import java.util.*;

@Service
public class UnoService {

    private static final String MSG_CARD_NOT_IN_HAND = "Card is not in the hand of player ";
    private static final String MSG_WRONG_TURN = "It is not the turn of player ";
    private static final String MSG_CARD_DOES_NOT_MATCH = "Card does not match the current card's color, number, or kind";
    private static final String MSG_MATCH_NOT_FOUND = "Match with ID %s not found.";
    private static final String MSG_UNKNOWN_PLAYER = "Unknown player: ";

    private final Map<UUID, Match> sessions = new HashMap<>();
    private final Map<UUID, List<String>> matchPlayers = new HashMap<>();

    @Autowired
    private Dealer dealer;

    public UUID newMatch(List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put(newKey, Match.fullMatch(dealer.fullDeck(), players));
        matchPlayers.put(newKey, players);
        return newKey;
    }

    public UUID newCustomMatch(List<Card> deck, int cardsPerPlayer, List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put(newKey, new Match(new ArrayList<>(deck), cardsPerPlayer, players));
        matchPlayers.put(newKey, players);
        return newKey;
    }

    public Collection<JsonCard> playerHand(UUID matchId) {
        return getMatch(matchId).playerHand().stream().map(Card::asJson).toList();
    }

    public void play(UUID matchId, String player, JsonCard card) {
        Match match = getMatch(matchId);

        if (!matchPlayers.getOrDefault(matchId, List.of()).contains(player)) {
            throw new IllegalArgumentException(MSG_UNKNOWN_PLAYER + player);
        }

        try {
            match.play(player, card.asCard());
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals(Player.NotPlayersTurn + player)) {
                throw new IllegalArgumentException(MSG_WRONG_TURN + player);
            } else if (msg.equals(Match.NotACardInHand + player)) {
                throw new IllegalArgumentException(MSG_CARD_NOT_IN_HAND + player);
            } else if (msg.equals(Match.CardDoNotMatch)) {
                throw new IllegalArgumentException(MSG_CARD_DOES_NOT_MATCH);
            } else {
                throw new IllegalArgumentException("Unexpected error: " + msg);
            }
        }
    }

    public void draw(UUID matchId, String player) {
        Match match = getMatch(matchId);
        try {
            match.drawCard(player);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.equals(Player.NotPlayersTurn + player)) {
                throw new IllegalArgumentException(MSG_WRONG_TURN + player);
            } else {
                throw e;
            }
        }
    }
    public JsonCard activeCard(UUID matchId) {
        return getMatch(matchId).activeCard().asJson();
    }

    public boolean isOver(UUID matchId) {
        return getMatch(matchId).isOver();
    }

    private Match getMatch(UUID matchId) {
        Match match = sessions.get(matchId);
        if (match == null) {
            throw new IllegalArgumentException(String.format(MSG_MATCH_NOT_FOUND, matchId));
        }
        return match;
    }
}