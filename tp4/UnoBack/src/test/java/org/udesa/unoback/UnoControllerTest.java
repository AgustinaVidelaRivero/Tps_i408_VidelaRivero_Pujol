package org.udesa.unoback;

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
public final class UnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Dealer dealer;

    @BeforeEach
    public void setup() {
        doReturn(List.of(
                // activa
                new NumberCard("Red", 0),
                //Agus
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
        mockMvc.perform(get("/playerhand/" + createMatch())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(7));
    }

    @Test
    public void test03ActiveCardReturnsValidJsonCard() throws Exception {
        mockMvc.perform(get("/activecard/" + createMatch())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.type").value("NumberCard"));
    }

    @Test
    public void test04PlayCardUpdatesActiveCard() throws Exception {
        String uuid = createMatch();
        mockMvc.perform(post("/play/" + uuid + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JsonCard("Red", 6, "NumberCard", false))))
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

        mockMvc.perform(post("/draw/" + uuid + "/Agus"))
                .andExpect(status().isOk());

        String afterDraw = mockMvc.perform(get("/playerhand/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonCard[] cardsAfter = objectMapper.readValue(afterDraw, JsonCard[].class);

        assert(cardsAfter.length == cardsBefore.length + 1);
    }

    @Test
    public void test06PlayCardNotInHandReturnsBadRequest() throws Exception {
        JsonCard fakeCard = new JsonCard("Green", 99, "NumberCard", false);
        mockMvc.perform(post("/play/" + createMatch() + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeCard)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test07PlayOutOfTurnReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/play/" + createMatch() + "/Trini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JsonCard("Red", 1, "NumberCard", false))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test08PlayCardThatDoesNotMatchReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/play/" + createMatch() + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JsonCard("Blue", 9, "NumberCard", false))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test09InvalidMatchIdReturnsBadRequest() throws Exception {
        UUID fakeId = UUID.randomUUID();
        mockMvc.perform(get("/playerhand/" + fakeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Match with ID " + fakeId + " not found."));
    }

    @Test
    public void test10InvalidPlayerReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/play/" + createMatch() + "/Pepito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JsonCard("Red", 1, "NumberCard", false))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unknown player: Pepito"));
    }

    @Test
    public void test11MalformedJsonReturnsBadRequest() throws Exception {
        String malformedJson = "{ \"color\": \"Red\", \"number\": 5, "; // JSON inválido (coma extra, string sin cerrar, etc.)
        mockMvc.perform(post("/play/" + createMatch() + "/Agus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid JSON input: malformed or missing fields"));
    }

    @Test
    public void test12DrawCardOutOfTurnReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/draw/" + createMatch() + "/Trini"))
                .andExpect(status().isBadRequest());
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