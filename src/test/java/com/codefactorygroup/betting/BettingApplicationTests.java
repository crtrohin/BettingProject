package com.codefactorygroup.betting;

import com.codefactorygroup.betting.controller.ParticipantController;
import com.codefactorygroup.betting.domain.Participant;
import com.codefactorygroup.betting.dto.ParticipantDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class BettingApplicationTests {


	// Autowired annotation on properties - eliminates the need for getters and setters
	@Autowired
	private ParticipantController participantController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void givenParticipantId_whenGetParticipant() {
		ParticipantDTO actualParticipant = participantController.getParticipant(4);
		ParticipantDTO expectedParticipant = new ParticipantDTO(4, "Manchester United");

		assertEquals(actualParticipant, expectedParticipant, "Participant should be Manchester United.");
	}

	@Test
	void givenParticipantId_whenUpdateParticipant() {
		ParticipantDTO actualParticipant = participantController.updateParticipant(new Participant(1, "Dinamo"), 1);
		ParticipantDTO expectedParticipant = new ParticipantDTO(1, "Dinamo");

		assertEquals(actualParticipant, expectedParticipant, "Participant information should be updated.");
	}

	@Test
	void contextLoads() {
		assertNotNull(participantController);
	}

	@Test
	void returnParticipant_whenAddParticipant() throws Exception {
		this.mockMvc.perform(post("/participant")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(new Participant(9, "FC Sheriff")))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.dtoID").value(9));
	}

	@Test
	void givenParticipantId_whenGetParticipant_thenError() throws Exception {
		this.mockMvc.perform(get("/participant/10")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
//				.andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
//				.andExpect(result -> assertEquals("No participant with ID = 10 was found.", result.getResolvedException().getMessage()));
	}


	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
