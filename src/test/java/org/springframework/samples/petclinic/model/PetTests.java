package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * @author Anaëlle Drai Laguéns
 */
public class PetTests {

	private Owner owner;

	private Pet pet;

	private Visit visit;

	private Set<Pet> pets;

	private PetType type;

	// Test Pets Data
	private static final int PET_ID = 1;

	private static final String PET_NAME = "Rocky";

	private static final LocalDate PET_BIRTH_DATE = LocalDate.of(2001, 01, 10);

	private static final String PET_TYPE = "Cat";

	private static final int TYPE_ID = 1;

	// Test Owner Data
	private static final int OWNER_ID = 1;

	private static final String ADDRESS = "3649 avenue Coloniale";

	private static final String CITY = "Paris";

	private static final String TELEPHONE = "+1(514)-111-1111";

	private static final String FIRST_NAME = "First";

	private static final String LAST_NAME = "Last";

	// Visit Data
	private static final int VISIT_ID = 1;

	private static final LocalDate VISIT_DATE = LocalDate.of(2015, 06, 27);

	private static final String VISIT_DESCRIPTION = "Routine Check";

	@BeforeEach
	void setup() {
		// Create a pet instance
		pet = new Pet();
		pet.setName(PET_NAME);
		pet.setId(PET_ID);
		pet.setBirthDate(PET_BIRTH_DATE);
		pet.setVisitsInternal(new HashSet<>());

		// Create a PetType instance and associate it with Pet
		type = new PetType();
		type.setName(PET_TYPE);
		type.setId(TYPE_ID);
		pet.setType(type);

		// Create an owner and add the pet we created
		owner = new Owner();
		owner.setId(OWNER_ID);
		owner.setFirstName(FIRST_NAME);
		owner.setLastName(LAST_NAME);
		owner.setAddress(ADDRESS);
		owner.setCity(CITY);
		owner.setTelephone(TELEPHONE);

		pets = new HashSet<>();
		pets.add(pet);
		owner.setPetsInternal(pets);

		// Create a visit instance
		visit = new Visit();
		visit.setId(VISIT_ID);
		visit.setDate(VISIT_DATE);
		visit.setDescription(VISIT_DESCRIPTION);
		visit.setPetId(PET_ID);

		// Associate the pet with the visit instance we created
		pet.setOwner(owner);
		pet.addVisit(visit);

	}

	@Test
	@DisplayName("Test the birth date getter")
	void testGetBirthDate() {
		// Determine if the getter returns the proper date
		assertEquals(pet.getBirthDate(), PET_BIRTH_DATE);
	}

	@Test
	@DisplayName("Test the pet type getter")
	void testGetType() {
		// Determine if the getter returns the proper type
		assertEquals(pet.getType(), type);
	}

	@Test
	@DisplayName("Test the owner getter")
	void testGetOwner() {
		// Determine if the getter returns the proper owner and the fields have not
		// changed
		assertEquals(pet.getOwner(), owner);
		assertEquals(pet.getOwner().getId(), OWNER_ID);
		assertEquals(pet.getOwner().getFirstName(), FIRST_NAME);
		assertEquals(pet.getOwner().getLastName(), LAST_NAME);
		assertEquals(pet.getOwner().getAddress(), ADDRESS);
		assertEquals(pet.getOwner().getCity(), CITY);
		assertEquals(pet.getOwner().getTelephone(), TELEPHONE);
	}

	@Test
	@DisplayName("Test the internal visits getter")
	void testGetVisitsInternal() {
		// Making sure that the internal getter returns an object of the right size
		assertEquals(pet.getVisitsInternal().size(), 1);
		assertTrue(pet.getVisitsInternal().contains(visit));
	}

	@Test
	@DisplayName("Test the visits getter when there are no visits")
	void testGetVisitsInternalNoVisit() {
		// Making sure that the visits getter returns an empty set when there are no
		// visits
		Pet pet2 = new Pet();
		pet2.setName("Bucky");
		pet2.setBirthDate(LocalDate.of(2022, 04, 12));
		assertEquals(pet2.getVisits().size(), 0);
	}

	@Test
	@DisplayName("Test the visits getter when there is a single visit")
	void testGetVisitsSingleVisit() {
		// Making sure that the visits getter returns a set of the correct size with the
		// correct visit when there is a single visit
		assertEquals(pet.getVisits().size(), 1);
		assertEquals(pet.getVisits().get(0).getId(), VISIT_ID);
		assertEquals(pet.getVisits().get(0).getPetId(), PET_ID);
		assertEquals(pet.getVisits().get(0).getDate(), VISIT_DATE);
		assertEquals(pet.getVisits().get(0).getDescription(), VISIT_DESCRIPTION);
	}

	@Test
	@DisplayName("Test the visits getter when there are several visits")
	void testGetVisitMultipleVisits() {
		// Create a new visit
		visit = new Visit();
		visit.setId(2);
		visit.setDate(LocalDate.of(2014, 01, 9));
		visit.setDescription("Vaccine");
		pet.addVisit(visit);

		// Check both visits are properly in the list and they are correctly ordered by
		// date
		assertEquals(pet.getVisits().size(), 2);
		assertEquals(pet.getVisits().get(0).getId(), VISIT_ID);
		assertEquals(pet.getVisits().get(0).getPetId(), PET_ID);
		assertEquals(pet.getVisits().get(0).getDate(), VISIT_DATE);
		assertEquals(pet.getVisits().get(0).getDescription(), VISIT_DESCRIPTION);
		assertEquals(pet.getVisits().get(1).getId(), 2);
		assertEquals(pet.getVisits().get(1).getPetId(), PET_ID);
		assertEquals(pet.getVisits().get(1).getDate(), LocalDate.of(2014, 01, 9));
		assertEquals(pet.getVisits().get(1).getDescription(), "Vaccine");
	}

}
