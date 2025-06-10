package org.udesa.unoback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.udesa.unoback.model.JsonCard;
import org.udesa.unoback.service.UnoService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Controller
public class UnoController {
    @Autowired
    UnoService unoService;

    @PostMapping ("newmatch") public ResponseEntity<UUID> newMatch(@RequestParam List<String> players) {
        return ResponseEntity.ok(unoService.newMatch(players));
    }

//    @GetMapping ("playerhand/{matchId}")
//    public ResponseEntity playerHand(@PathVariable UUID matchId) {
//        return ResponseEntity.ok(unoService.playerHand(matchId).stream().map(each -> each.asJson()));
//    }
    @GetMapping("playerhand/{matchId}")
    public ResponseEntity<Collection<JsonCard>> playerHand(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.playerHand(matchId));
    }

//    @PostMapping("/play/{matchId}/{player}")
//    public ResponseEntity<Void> play(@PathVariable UUID matchId,
//                                         @PathVariable String player,
//                                         @RequestBody JsonCard card) {
//        unoService.play(matchId, player, card);
//        return ResponseEntity.ok().build(); // no devuelve cuerpo, solo status 200
//    }
    @PostMapping("play/{matchId}/{player}")
    public ResponseEntity<Void> play(@PathVariable UUID matchId,
                                     @PathVariable String player,
                                     @RequestBody JsonCard card) {
        unoService.play(matchId, player, card);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/draw/{matchId}/{player}")
    public ResponseEntity<Void> draw(@PathVariable UUID matchId,
                                     @PathVariable String player) {
        unoService.draw(matchId, player);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activecard/{matchId}")
    public ResponseEntity<JsonCard> activeCard(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.activeCard(matchId));
    }

}
