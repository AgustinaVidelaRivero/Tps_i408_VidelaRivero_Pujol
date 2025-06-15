package org.udesa.unoback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udesa.unoback.model.*;

import java.util.*;

@Service
public class UnoService {

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
        if (!matchPlayers.getOrDefault(matchId, List.of()).contains(player)) {
            throw new IllegalArgumentException("Unknown player: " + player);
        }
        getMatch(matchId).play(player, card.asCard());
    }

    public void draw(UUID matchId, String player) {
        getMatch(matchId).drawCard(player);
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
            throw new IllegalArgumentException("Match with ID %s not found.".formatted(matchId));
        }
        return match;
    }
}