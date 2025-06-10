package org.udesa.unoback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.udesa.unoback.model.JsonCard;
import org.udesa.unoback.model.NumberCard;
import org.udesa.unoback.service.Dealer;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
public class UnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Dealer dealer;

//    @BeforeEach
//    public void setup() {
//        doReturn(List.of(
//                new NumberCard("Red", 0), // activa
//                // Agus
//                new NumberCard("Red", 1), new NumberCard("Red", 2), new NumberCard("Red", 3),
//                new NumberCard("Red", 4), new NumberCard("Red", 5), new NumberCard("Red", 6), new NumberCard("Red", 7),
//                // Trini
//                new NumberCard("Blue", 1), new NumberCard("Blue", 2), new NumberCard("Blue", 3),
//                new NumberCard("Blue", 4), new NumberCard("Blue", 5), new NumberCard("Blue", 6), new NumberCard("Blue", 7),
//                // Carta extra para robar:
//                new NumberCard("Green", 9)
//        )).when(dealer).fullDeck();
//    }

    @BeforeEach
    public void setup() {
        doReturn(List.of(
                new NumberCard("Red", 0), // activa
                // Agus (le damos Blue 9 para que esté en mano pero no matchee)
                new NumberCard("Blue", 9), new NumberCard("Red", 2), new NumberCard("Red", 3),
                new NumberCard("Red", 4), new NumberCard("Red", 5), new NumberCard("Red", 6), new NumberCard("Red", 7),
                // Trini
                new NumberCard("Blue", 1), new NumberCard("Blue", 2), new NumberCard("Blue", 3),
                new NumberCard("Blue", 4), new NumberCard("Blue", 5), new NumberCard("Blue", 6), new NumberCard("Blue", 7),
                // Carta extra para robar:
                new NumberCard("Green", 9)
        )).when(dealer).fullDeck();
    }

    @Test
    public void test01NewMatchReturnsUUID() throws Exception {
        mockMvc.perform(post("/newmatch")
                        .param("players", "Agus")
                        .param("players", "Trini")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    UUID.fromString(content.replace("\"", ""));
                });
    }

    @Test
    public void test02PlayerHandReturnsSevenCards() throws Exception {
        String uuid = createMatch();

        mockMvc.perform(get("/playerhand/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    public void test03ActiveCardReturnsValidJsonCard() throws Exception {
        String uuid = createMatch();

        mockMvc.perform(get("/activecard/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.type").value("NumberCard"));
    }

    @Test
    public void test04PlayCardUpdatesActiveCard() throws Exception {
        String uuid = createMatch();

        JsonCard cardToPlay = new JsonCard("Red", 6, "NumberCard", false);

        mockMvc.perform(post("/play/" + uuid + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardToPlay)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/activecard/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.type").value("NumberCard"));
    }

    @Test
    public void test05DrawAddsCardToHand() throws Exception {
        String uuid = createMatch();

        String beforeDraw = mockMvc.perform(get("/playerhand/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonCard[] cardsBefore = objectMapper.readValue(beforeDraw, JsonCard[].class);
        int handSizeBefore = cardsBefore.length;

        mockMvc.perform(post("/draw/" + uuid + "/Agus"))
                .andExpect(status().isOk());

        String afterDraw = mockMvc.perform(get("/playerhand/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonCard[] cardsAfter = objectMapper.readValue(afterDraw, JsonCard[].class);
        int handSizeAfter = cardsAfter.length;

        assert(handSizeAfter == handSizeBefore + 1);
    }

    @Test
    public void test06PlayCardNotInHandReturnsBadRequest() throws Exception {
        String uuid = createMatch();

        JsonCard fakeCard = new JsonCard("Green", 99, "NumberCard", false);

        mockMvc.perform(post("/play/" + uuid + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeCard)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card is not in the hand of player Agus"));
    }

    @Test
    public void test07PlayOutOfTurnReturnsBadRequest() throws Exception {
        String uuid = createMatch();

        JsonCard cardFromAgus = new JsonCard("Red", 1, "NumberCard", false);

        mockMvc.perform(post("/play/" + uuid + "/Trini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardFromAgus)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("It is not the turn of player Trini"));
    }

    @Test
    public void test08PlayCardThatDoesNotMatchReturnsBadRequest() throws Exception {
        String uuid = createMatch();

        JsonCard card = new JsonCard("Blue", 9, "NumberCard", false);

        mockMvc.perform(post("/play/" + uuid + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card does not match the current card's color, number, or kind"));
    }

    @Test
    public void test09InvalidMatchIdReturnsBadRequest() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/playerhand/" + fakeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Match with ID " + fakeId + " not found."));
    }

    @Test
    public void test10InvalidPlayerReturnsBadRequest() throws Exception {
        String uuid = createMatch();

        JsonCard validCard = new JsonCard("Red", 1, "NumberCard", false);

        mockMvc.perform(post("/play/" + uuid + "/Pepito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCard)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unknown player: Pepito"));
    }

    private String createMatch() throws Exception {
        return mockMvc.perform(post("/newmatch")
                        .param("players", "Agus")
                        .param("players", "Trini")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replace("\"", "");
    }
}