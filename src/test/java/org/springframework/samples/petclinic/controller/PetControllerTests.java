package org.springframework.samples.petclinic.controller;


import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PetController.class)
public class PetControllerTests {

    @Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetRepository pets;

	@MockBean
	private OwnerRepository owners;

    private Owner amy;

	private Pet ichigo;

	private Pet nala;

	private PetType cat;

    private PetType dog;


    @BeforeEach
	void setup() {
		//mock owner 1
        amy = new Owner();
        amy.setFirstName("Amy");
        amy.setLastName("Wang");
        amy.setId(1);
		amy.setAddress("123 Meadow Lane");
		amy.setCity("Toronto");
		amy.setTelephone("123456789");

		// mock up pet
		nala = new Pet();
		// petType mock
		dog = new PetType();
		dog.setId(1);
		dog.setName("dog");
		nala.setId(1);
		nala.setType(dog);
		nala.setName("Nala");
        LocalDate birthday1 = LocalDate.parse("2016-11-30");
        nala.setBirthDate(birthday1);
		nala.setOwner(amy);
		amy.addPet(nala);

		ichigo = new Pet();
		cat= new PetType();
		cat.setName("cat");
		cat.setId(2);
		ichigo.setId(2);
		ichigo.setType(cat);
		ichigo.setName("Ichigo");
        LocalDate birthday2 = LocalDate.parse("2018-08-11");
        ichigo.setBirthDate(birthday2);
        ichigo.setOwner(amy);
        amy.addPet(ichigo);

		given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(dog));
		given(this.owners.findById(1)).willReturn(amy);
		given(this.pets.findById(1)).willReturn(nala);

	}


	/**
	 * Tests the PetController method initCreationForm()
	 * API Endpoint: @GetMapping("/owners/{ownerId}/pets/new") endpoint
	 * Expectation: return success code 200
	 * The expected model attribute is "pet"
	 * The expected view name is "pets/createOrUpdatePetForm"
	 * @throws Exception
	 */
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", 1)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	}

	/**
	 * Tests the PetController method processCreationForm() when there are no errors
	 * API Endpoint: @PostMapping("/owners/{ownerId}/pets/new")
	 * Expectation: return redirect code 302
	 * The expected view name is "redirect:/owners/{ownerId}"
	 * @throws Exception
	 */
	@Test
	void testProcessCreationForm() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 1).flashAttr("pet", nala))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));

		verify(pets).save(nala);
	}

	/**
	 * Tests the PetController method processCreationForm() result when there are errors
	 * API Endpoint: @PostMapping("/owners/{ownerId}/pets/new")
	 * Expectation: return success code 200
	 * Expected model with attribute "pet" will have errors
	 * Expected view name is "pets/createOrUpdatePetForm"
	 * @throws Exception
	 */
	@Test
	void testProcessCreationFormErrors() throws Exception {
		mockMvc.perform(
				post("/owners/{ownerId}/pets/new", 1).param("name", "King").param("birthDate", "2021-03-01"))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	/**
	 * Tests the PetController method initUpdateForm()
	 * API Endpoint: @GetMapping("/owners/{ownerId}/pets/{petId}/edit")
	 * Expectation: return success code 200
	 * Expected model attribute has "pet" and related properties
	 * Expected view name is "pets/createOrUpdatePetForm"
	 * @throws Exception
	 */
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", 1, 1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attribute("pet", hasProperty("name", is("Nala"))))
				.andExpect(model().attribute("pet", hasProperty("type", is(dog))))
				.andExpect(model().attribute("pet", hasProperty("birthDate", is(LocalDate.parse("2016-11-30")))))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	/**
	 * Tests the PetController method processUpdateForm() when there are no errors
	 * API Endpoint: @PostMapping("/owners/{ownerId}/pets/{petId}/edit")
	 * Expectation: return redirect code 302
	 * Expected view name is "redirect:/owners/{ownerId}"
	 * @throws Exception
	 */
	@Test
	void testProcessUpdateForm() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).flashAttr("pet", ichigo))
				.andExpect(view().name("redirect:/owners/{ownerId}")).andExpect(status().is3xxRedirection());

		verify(pets).save(ichigo);
	}

	/**
	 * Tests the PetController method processUpdateForm when there are errors
	 * API Endpoint: @PostMapping("/owners/{ownerId}/pets/{petId}/edit")
	 * Expectation: return success code 200
	 * Expected model with attribute "pet" has errors, but "owner" has no errors
	 * Expected view name is "pets/createOrUpdatePetForm"
	 * @throws Exception
	 */
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).param("name", "Ruby")
				.param("birthDate", "2022-10-09")).andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet")).andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdatePetForm"));
	}





}
