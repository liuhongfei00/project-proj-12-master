package org.springframework.samples.petclinic.controller;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.assertj.core.util.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import java.util.*;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;



/**
 * Test class for the {@link OwnerController}
 */
@WebMvcTest(OwnerController.class)
@AutoConfigureMockMvc
public class OwnerControllerTests {
    @Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;

    @MockBean
	private VisitRepository visits;

    private Owner amy;

    private Owner sam;

	private Owner anna;

	private Pet ichigo;

	private Pet nala;

    @BeforeEach
	void setup() {

        //mock owner 1
        amy = new Owner();
        amy.setFirstName("Amy");
		amy.setLastName("Wang");
		amy.setId(1);

        //mock owner 2
		anna = new Owner();
		anna.setFirstName("Anna");
		anna.setLastName("Hao");
		anna.setId(2);

        //mock owner 3
        sam = new Owner();
		sam.setFirstName("Sam");
		sam.setLastName("Hao");
		sam.setId(3);

        //set address and city
		String address1 = "123 Meadow Lane";
		amy.setAddress(address1);
        String city1 = "Toronto";
		amy.setCity(city1);

		String address2 = "345 rue Miel";
        String city2 = "Montreal";
        anna.setAddress(address2);
        anna.setCity(city2);

        String address3 = "10 River St";
        sam.setAddress(address3);
        sam.setCity(city2);

        //set phone numbers
        String phone1="123456789";
        String phone2="987654321";
        String phone3= "100100100";
        amy.setTelephone(phone1);
        anna.setTelephone(phone2);
        sam.setTelephone(phone3);

        //create pet and add to owner
        Pet ichigo = new Pet();
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("Cat");
        ichigo.setType(cat);
        ichigo.setId(1);
        LocalDate birthday1 = LocalDate.parse("2018-08-11");
        ichigo.setBirthDate(birthday1);
        amy.addPet(ichigo);
        ichigo.setOwner(amy);


        //again
        Pet nala = new Pet();
        PetType dog = new PetType();
        dog.setId(2);
        dog.setName("Dog");
        nala.setType(dog);
        nala.setId(2);
        LocalDate birthday2 = LocalDate.parse("2016-11-30");
        nala.setBirthDate(birthday2);
        anna.addPet(nala);
        nala.setOwner(anna);

        //again
        Pet king = new Pet();
        king.setType(dog);
        king.setId(3);
        LocalDate birthday3 = LocalDate.parse("2021-09-01");
        king.setBirthDate(birthday3);
        sam.addPet(king);
        king.setOwner(sam);


        //return by id
		given(this.owners.findById(1)).willReturn(amy);
        given(this.owners.findById(2)).willReturn(anna);
        given(this.owners.findById(3)).willReturn(sam);
        amy.setPetsInternal(Collections.singleton(ichigo));

        //return by last name
        //empty last name field returns all names
        given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(amy, anna, sam));
        //nonexistent last name returns empty list
        given(this.owners.findByLastName("Smith")).willReturn(Lists.newArrayList());
        //1 matching last name
        given(this.owners.findByLastName("Wang")).willReturn(Lists.newArrayList(amy));
        //more than 1 matching last name
        given(this.owners.findByLastName("Hao")).willReturn(Lists.newArrayList(anna, sam));

        Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		given(this.visits.findByPetId(1)).willReturn(Collections.singletonList(visit));


	}

    /**
	 * Tests the OwnerController method initCreationForm()
	 * Endpoint: @GetMapping("/owners/new")
     * Expectation: returns HTTP success status code 200 and view "owners/createOrUpdateOwnerForm"
	 * @throws Exception
	 */

    @Test
    void testinitCreationForm() throws Exception{

        mockMvc.perform(get("/owners/new")).andExpect(status().isOk())
        .andExpect(model().attributeExists("owner"))
        .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /**
	 * Tests the OwnerController method processCreationForm() when there are no errors
	 * Endpoint: @PostMapping("/owners/new") when successful and has complete parameters
	 * Expectation: Returns HTTP redirect status code 302 and view ""redirect:/owners/" + 1"
	 * @throws Exception
	 */
	@Test
	void testProcessCreationForm() throws Exception {

		mockMvc.perform(post("/owners/new").flashAttr("owner", amy)).andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/owners/" + 1));
		verify(owners).save(amy);
	}

	/**
	 * Tests OwnerController method processCreationForm() when there are errors
	 * Endpoint: @PostMapping("/owners/new")
	 * Expectation: Returns HTTP success status code 200 and view "owners/createOrUpdateOwnerForm"
	 * @throws Exception
	 */
	@Test
	void testProcessCreationFormHasErrors() throws Exception {

		mockMvc.perform(post("/owners/new").param("firstName", "Tay").param("lastName", "Collins"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	/**
	 * Tests OwnerController method initFindForm()
	 * Endpoint: @GetMapping("/owners/find")
     * Should return code 200 and view "owners/findOwners"
	 * @throws Exception
	 */

    @Test
    void testinitFindForm() throws Exception{

        mockMvc.perform(get("/owners/find")).andExpect(status().isOk())
        .andExpect(model().attributeExists("owner"))
        .andExpect(view().name("owners/findOwners"));
    }

    /*
    Now we will write 4 tests for processFindForm()
    First test is to show all owners when search parameters are empty
    */

    /**
	 * Tests OwnerController method processFindForm() when given 0 parameters i.e. broadest search
	 * Endpoint: @GetMapping("/owners")
	 * Expectation: returns HTTP success status code 200, view "owners/ownersList"
	 * @throws Exception
	 */
    @Test
    void testprocessFindForm1() throws Exception {
        mockMvc.perform(get("/owners/")).andExpect(status().isOk())
        .andExpect(model().attributeExists("owner"))
        .andExpect(view().name("owners/ownersList"));
    }

    //2nd test is to show find owner by last name (0 matches found)
    /**
	 * Tests OwnerController method processFindForm() with last name parameter and 1 match
	 * Endpoint: @GetMapping("/owners")
	 * Expectation: return HTTP redirect status code 302, return "redirect:/owners/1" i.e. the single match found
	 * @throws Exception
	 */
	@Test
	void testProcessFindForm2() throws Exception {
		// Test single possible search
		mockMvc.perform(get("/owners?lastName=Wang")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/" + 1));
	}

    /**
	 * Tests OwnerController method processFindForm() with last name parameter and multiple matches
	 * Endpoint: @GetMapping("/owners") with multiple owners found.
     * Expectation: return HTTP "owners/ownersList".
	 * @throws Exception
	 */
	@Test
	void testProcessFindForm3() throws Exception {
		// Test multiple possible search
		mockMvc.perform(get("/owners?lastName=Hao")).andExpect(status().isOk())
				.andExpect(model().attributeExists("selections")).andExpect(view().name("owners/ownersList"));
	}

    /**
	 * Tests OwnerController method processFindForm() with last name parameter and 0 matches
	 * Endpoint: @GetMapping("/owners") with 0 owners found.
     * Expectation: returns HTTP success 200, view "owners/findOwners
	 * @throws Exception
	 */
	@Test
	void testProcessFindForm4() throws Exception {
		// Test multiple possible search
		mockMvc.perform(get("/owners?lastName=Smith")).andExpect(status().isOk())
				.andExpect(view().name("owners/findOwners"));
	}

	/**
	 * Tests OwnerController method initUpdateOwnerForm()
	 * Endpoint: @GetMapping("/owners/{ownerId}/edit")
     * Expectation: return model with attribute "owner" and related properties and view "owners/createOrUpdateOwnerForm"
	 * @throws Exception
	 */
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		// Test the correct owner is returned
		mockMvc.perform(get("/owners/{ownerId}/edit", 1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("Amy"))))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Wang"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("123 Meadow Lane"))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Toronto"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("123456789"))))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}


    /**
	 * Tests OwnerController method processUpdateOwnerForm() when there are no errors
	 * Endpoint: @PostMapping("/owners/{ownerId}/edit")
     * Expectation: return HTTP redirect code 302 and view name "redirect:/owners/{ownerId}".
	 * @throws Exception
	 */
    @Test
    void testProcessUpdateOwnerForm() throws Exception {

        mockMvc.perform(post("/owners/{ownerId}/edit", 1).flashAttr("owner", sam))
        .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));

        verify(owners).save(sam);
    }

     /**
	 * Tests OwnerController method processUpdateOwnerForm() when there are errors
	  * Endpoint: @PostMapping("/owners/{ownerId}/edit") when given errors.
     * Expectation: returns HTTP 200 code and view "owners/createOrUpdateOwnerForm"
	 * @throws Exception
	 */

    @Test
    void testProcessUpdateOwnerFormErrors() throws Exception {

		mockMvc.perform(post("/owners/{ownerId}/edit", 1).param("firstName", "Ami").param("lastName", "Wong"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "city"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /**
	 * Tests OwnerController method showOwner()
	 * Endpoint: @GetMapping("/owners/{ownerId}")
	 * Expectation: returns HTTP 200 success, returns view "owners/ownerDetails"
	 * @throws Exception
	 */
	@Test
	void testShowOwner() throws Exception {

		mockMvc.perform(get("/owners/{ownerId}", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("owner", hasProperty("firstName", is("Amy"))))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Wang"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("123 Meadow Lane"))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Toronto"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("123456789"))))
				.andExpect(model().attribute("owner", hasProperty("pets", not(empty()))))
				.andExpect(model().attribute("owner", hasProperty("pets", new BaseMatcher<List<Pet>>() {

					public boolean matches(Object item) {
						List<Pet> pets = (List<Pet>) item;
						Pet pet = pets.get(0);
						if (pet.getVisits().isEmpty()) {
							return false;
						}
						return true;
					}
					//if false, then that means the pet has no visits and we will display a message
					public void describeTo(Description description) {
						description.appendText("Ichigo has no visits");
					}
				}))).andExpect(view().name("owners/ownerDetails"));
	}





}
