package org.udesa.unoback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.udesa.unoback.model.JsonCard;
import org.udesa.unoback.service.UnoService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
public class UnoController {

    @Autowired
    UnoService unoService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonError(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid JSON input: malformed or missing fields");
    }

    @PostMapping("/newmatch")
    public ResponseEntity<UUID> newMatch(@RequestParam List<String> players) {
        return ResponseEntity.ok(unoService.newMatch(players));
    }

    @GetMapping("/playerhand/{matchId}")
    public ResponseEntity<Collection<JsonCard>> playerHand(@PathVariable UUID matchId) {
        return ResponseEntity.ok(unoService.playerHand(matchId));
    }

    @PostMapping("/play/{matchId}/{player}")
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
