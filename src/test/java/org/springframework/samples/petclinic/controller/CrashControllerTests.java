package org.springframework.samples.petclinic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

//@Disabled
@WebMvcTest(CrashController.class)
class CrashControllerTests {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Tests the triggerException() method in CrashController class and if it throws exception correctly
	 * Endpoint: @GetMapping("/oups")
	 * Expectation: Returns 500 Internal Error and RuntimeException.
	 * @throws Exception
	 */
	@Test
	void testTriggerException() throws Exception {
		mockMvc.perform(get("/oups")).andExpect(status().is5xxServerError()).andExpect(content()
				.string("Expected: controller used to showcase what " + "happens when an exception is thrown"));
	}

}
