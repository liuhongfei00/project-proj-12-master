package org.springframework.samples.petclinic.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VisitController.class)
class VisitControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VisitRepository visits;

	@MockBean
	private PetRepository pets;

	private Owner amy;

	private Pet nala;

	private PetType dog;

	@BeforeEach
	void setup() {
		// mock up petType
		dog = new PetType();
		dog.setId(1);
		dog.setName("dog");
		// mock up pet
		nala = new Pet();
		nala.setId(1);
		nala.setType(dog);
		nala.setName("nala");
		nala.setBirthDate(LocalDate.parse("2016-11-30"));
		nala.setOwner(amy);

		// mock up persistence response
		given(this.pets.findById(1)).willReturn(nala);
	}

	/**
	 * Tests VisitController method initNewVisitForm()
	 * Endpoint: @GetMapping("/owners/{*}/pets/{petId}/visits/new")
	 * Expectation: return success code 200; return view "pets/createOrUpdateVisitForm"
	 * @throws Exception
	 */
	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", 1)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	/**
	 * Tests VisitController method processNewVisitForm() with no errors
	 * Endpoint: @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	 * Expectation: return redirect code 302; return view "redirect:/owners/{ownerId}".
	 * @throws Exception
	 */
	@Test
	void testProcessNewVisitForm() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", 1).param("name", "Amy").param("description",
				"Physical examination")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	/**
	 * Tests VisitController method processNewVisitForm() with errors
	 * Endpoint: @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new" when errors exist
	 * Expectation: return success code 200, view "pets/createOrUpdateVisitForm", model with attribute visit having errors.
	 * @throws Exception
	 */
	@Test
	void testProcessNewVisitFormErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", 1).param("name", "Amy"))
				.andExpect(model().attributeHasErrors("visit"))
				.andExpect(model().attributeHasFieldErrors("visit", "description")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

}
