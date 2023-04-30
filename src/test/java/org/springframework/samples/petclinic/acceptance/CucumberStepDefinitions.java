package org.springframework.samples.petclinic.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = PetClinicApplication.class)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class CucumberStepDefinitions {

	@Autowired
	private MockMvc mockMvc;

	private Object[] errorResponse = {};

	private int ownerID = -1;

	private String redirectedURL = "";

	// Create Owner step definitions
	@When("the user attempts to add an owner with {string}, {string}, {string}, {string} and {string}")
	public void the_user_attempts_to_add_an_owner_with_and(String firstName, String lastName, String address,
			String city, String telephone) throws Exception {

		MvcResult result = this.mockMvc
				.perform(post("/owners/new").param("firstName", firstName).param("lastName", lastName)
						.param("address", address).param("city", city).param("telephone", telephone))
				.andReturn();
		errorResponse = result.getModelAndView().getModelMap().values().toArray();
		redirectedURL = result.getResponse().getRedirectedUrl();
		if (redirectedURL == null) {
			ownerID = -1;
		}
		else {
			ownerID = Integer.valueOf(redirectedURL.substring(8));
		}

	}

	@Then("a new owner profile shall be created")
	public void a_new_owner_profile_shall_be_created() {
		assertEquals(0, errorResponse.length);
		assertTrue(redirectedURL.matches("/owners/[0-9]+"));
	}

	@Then("the profile has {string}, {string}, {string}, {string} and {string}")
	public void the_profile_has_and(String firstName, String lastName, String address, String city, String telephone)
			throws Exception {
		MvcResult result = this.mockMvc.perform(get("/owners/" + ownerID)).andReturn();
		String ownerData = result.getModelAndView().getModelMap().values().toArray()[0].toString();
		assertTrue(ownerData.contains("firstName = '" + firstName + "'"));
		assertTrue(ownerData.contains("lastName = '" + lastName + "'"));
		assertTrue(ownerData.contains("address = '" + address + "'"));
		assertTrue(ownerData.contains("city = '" + city + "'"));
		assertTrue(ownerData.contains("telephone = '" + telephone + "'"));
	}

	@Then("no new owner profile shall be created")
	public void no_new_owner_profile_shall_be_created() {
		assertEquals(2, errorResponse.length);
		assertEquals(null, redirectedURL);
	}

	@Then("the form shows this {string} message")
	public void the_form_shows_this_message(String message) {
		assertTrue(errorResponse.length > 0);
		boolean appropriateError = (errorResponse[errorResponse.length - 1].toString().contains(message));
		assertTrue(appropriateError);
	}

	// Create Pet step definitions

	@Given("there is an owner in the system")
	public void there_is_an_owner_in_the_system() throws Exception {
		this.mockMvc.perform(get("/owners/1")).andExpect(status().isOk());
	}

	@Given("the owner has no pet with {string}")
	public void the_owner_has_no_pet_with(String name) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/owners/1")).andReturn();
		String resultHTML = result.getResponse().getContentAsString();
		assertFalse(resultHTML.contains("<dd>" + name + "</dd>"));
	}

	@When("the user attempts to create a new pet with {string}, {string} and {string}")
	public void the_user_attempts_to_create_a_new_pet_with_and(String name, String birthDate, String type)
			throws Exception {
		MvcResult result = this.mockMvc.perform(
				post("/owners/1/pets/new").param("name", name).param("birthDate", birthDate).param("type", type))
				.andReturn();
		errorResponse = result.getModelAndView().getModelMap().values().toArray();
		redirectedURL = result.getResponse().getRedirectedUrl();
	}

	@Then("a new pet was created")
	public void a_new_pet_was_created() {
		assertEquals(0, errorResponse.length);
		assertTrue(redirectedURL.matches("/owners/[0-9]+"));
	}

	@Then("the pet has {string}, {string} and {string}")
	public void the_pet_has_and(String name, String birthDate, String type) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/owners/1")).andReturn();
		String resultHTML = result.getResponse().getContentAsString();
		resultHTML = resultHTML.replaceAll("\\s", "");
		assertTrue(resultHTML
				.contains(String.format("<dt>Name</dt><dd>%s</dd><dt>BirthDate</dt><dd>%s</dd><dt>Type</dt><dd>%s</dd>",
						name, birthDate, type)));
	}

	@Then("no new pet was created")
	public void no_new_pet_was_created() {
		assertTrue(errorResponse.length > 0);
		assertEquals(null, redirectedURL);
	}

	@Given("the owner has a pet with {string}")
	public void the_owner_has_a_pet_with(String name) throws Exception {
		this.mockMvc.perform(
				post("/owners/1/pets/new").param("name", name).param("birthDate", "2020-01-01").param("type", "cat"))
				.andExpect(status().isOk());

	}

	// Create Visit step definitions

	@When("the user attempts to create a visit with {string} and {string}")
	public void the_user_attempts_to_create_a_visit_with_and(String date, String description) throws Exception {
		MvcResult result = this.mockMvc
				.perform(post("/owners/1/pets/1/visits/new").param("date", date).param("description", description))
				.andReturn();
		errorResponse = result.getModelAndView().getModelMap().values().toArray();
		if (errorResponse.length > 0)
			errorResponse = Arrays.copyOf(errorResponse, errorResponse.length - 1);
		redirectedURL = result.getResponse().getRedirectedUrl();
	}

	@Then("a new visit was created")
	public void a_new_visit_was_created() {
		assertEquals(0, errorResponse.length);
		assertTrue(redirectedURL.matches("/owners/[0-9]+"));
	}

	@Then("the visit has {string} and {string}")
	public void the_visit_has_and(String date, String description) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/owners/1")).andReturn();
		String resultHTML = result.getResponse().getContentAsString();
		resultHTML = resultHTML.replaceAll("\\s", "");
		assertTrue(resultHTML.contains(String.format("<td>%s</td><td>%s</td>", date, description)));

	}

	@Then("no new visit was created")
	public void no_new_visit_was_created() {
		assertTrue(errorResponse.length > 0);
		assertEquals(null, redirectedURL);
	}

	@Given("the owner has a pet")
	public void the_owner_has_a_pet() throws Exception {
		this.mockMvc.perform(
				post("/owners/1/pets/new").param("name", "pet").param("birthDate", "2020-01-01").param("type", "cat"));
	}

	@When("the user attempts to create a visit with a date earlier than the pet birthdate")
	public void the_user_attempts_to_create_a_visit_with_a_date_earlier_than_the_pet_birthdate() throws Exception {
		MvcResult result = this.mockMvc
				.perform(
						post("/owners/1/pets/1/visits/new").param("date", "2019-01-01").param("description", "Vaccine"))
				.andReturn();
		errorResponse = result.getModelAndView().getModelMap().values().toArray();
		redirectedURL = result.getResponse().getRedirectedUrl();
	}

	@Then("the form shows the birthdate error message")
	public void the_form_shows_the_birthdate_error_message() {
		assertTrue(errorResponse.length > 0);
		boolean appropriateError = (errorResponse[errorResponse.length - 1].toString().contains("Invalid date"));
		assertTrue(appropriateError);
	}

}
