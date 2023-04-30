package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.style.ToStringCreator;

/*
 * @author Anaëlle Drai Laguéns
 */
public class OwnerTests {

	private Owner owner;

	private Pet pet;

	private Set<Pet> pets;

	// Test Owner Data
	private static final int OWNER_ID = 1;

	private static final String ADDRESS = "3649 avenue Coloniale";

	private static final String CITY = "Paris";

	private static final String TELEPHONE = "+1(514)-111-1111";

	private static final String FIRST_NAME = "First";

	private static final String LAST_NAME = "Last";

	// Test Pet Data
	private static final int PET_ID = 1;

	private static final String PET_NAME = "Rocky";

	private static final LocalDate PET_BIRTH_DATE = LocalDate.of(2001, 01, 10);

	@BeforeEach
	void setup() {
		// Create a pet instance and associate it to owner
		pet = new Pet();
		pet.setName(PET_NAME);
		pet.setId(PET_ID);
		pet.setOwner(owner);
		pet.setBirthDate(PET_BIRTH_DATE);

		// Create a HashSet and add the pet we just created
		pets = new HashSet<>();
		pets.add(pet);

		// Create an owner instance and associate it to the previously created pets
		// hashset
		owner = new Owner();
		owner.setId(OWNER_ID);
		owner.setFirstName(FIRST_NAME);
		owner.setLastName(LAST_NAME);
		owner.setAddress(ADDRESS);
		owner.setCity(CITY);
		owner.setTelephone(TELEPHONE);
		owner.setPetsInternal(pets);

	}

	@Test
	@DisplayName("Test address getter")
	void testGetAddress() {
		// Test address getter method
		assertEquals(owner.getAddress(), ADDRESS);
	}

	@Test
	@DisplayName("Test city getter")
	void testGetCity() {
		// Test city getter method
		assertEquals(owner.getCity(), CITY);
	}

	@Test
	@DisplayName("Test telephone getter")
	void testGetTelephone() {
		// Test telephone getter method
		assertEquals(owner.getTelephone(), TELEPHONE);
	}

	@Test
	@DisplayName("Test get pets internal getter")
	void testGetPetsInternal() {
		// Test the internal pets getter
		assertEquals(owner.getPetsInternal(), pets);
	}

	@Test
	@DisplayName("Test get pets when the owner has no pets")
	void testGetPetsNoPets() {
		// Set pets to an empty hashset
		owner.setPetsInternal(new HashSet<>());
		assertTrue(owner.getPets().isEmpty());
	}

	@Test
	@DisplayName("Test get pets with a single pet for the owner")
	void testGetPetsSinglePet() {
		// Test the get pet setter with the single pet that was added & compare their
		// fields
		assertEquals(owner.getPets().size(), 1);
		assertEquals(owner.getPets().get(0).getId(), PET_ID);
		assertEquals(owner.getPets().get(0).getName(), PET_NAME);
		assertEquals(owner.getPets().get(0).getBirthDate(), PET_BIRTH_DATE);
	}

	@Test
	@DisplayName("Test get pets with several pets for the owner")
	void testGetPetsSeveralPets() {
		// Test the get pet setter with the single pet that was added & compare their
		// fields
		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setName("Bucky");
		pet2.setBirthDate(LocalDate.of(2022, 04, 12));
		owner.addPet(pet2);
		// Test the pet was added properly and sorting based on name is correct
		assertEquals(owner.getPets().size(), 2);
		assertEquals(owner.getPets().get(0).getId(), PET_ID);
		assertEquals(owner.getPets().get(0).getName(), PET_NAME);
		assertEquals(owner.getPets().get(0).getBirthDate(), PET_BIRTH_DATE);
		assertEquals(owner.getPets().get(1).getId(), 2);
		assertEquals(owner.getPets().get(1).getName(), "Bucky");
		assertEquals(owner.getPets().get(1).getBirthDate(), LocalDate.of(2022, 04, 12));
	}

	@Test
	@DisplayName("Test get pet given a name that exists")
	void testGetPetGivenName() {
		// Test the pet was added properly and sorting based on name is correct
		assertEquals(owner.getPet(PET_NAME).getId(), PET_ID);
		assertEquals(owner.getPet(PET_NAME).getName(), PET_NAME);
		assertEquals(owner.getPet(PET_NAME).getBirthDate(), PET_BIRTH_DATE);
	}

	@Test
	@DisplayName("Test get pet given a name that does not exist")
	void testGetPetDoesNotExist() {
		// Test the pet was added properly and sorting based on name is correct
		assertEquals(owner.getPet("Wrong"), null);
	}

	@Test
	@DisplayName("Test get pet given a name that exists with argument ignoreNew")
	void testGetPetGivenNameAndIgnoreNew() {
		// Test the pet is not returned if it's new
		Pet pet2 = new Pet();
		pet2.setName("Bucky");
		pet2.setBirthDate(LocalDate.of(2022, 04, 12));
		owner.addPet(pet2);

		// Because the ID is not set, the pet we just added will be ignored
		assertEquals(owner.getPet("Bucky", true), null);
	}

	@Test
	@DisplayName("Test the to string method")
	void testToString() {
		// Make sure the toString method is properly overridden
		assertEquals(owner.toString(),
				new ToStringCreator(owner).append("id", owner.getId()).append("new", owner.isNew())
						.append("lastName", owner.getLastName()).append("firstName", owner.getFirstName())
						.append("address", owner.getAddress()).append("city", owner.getCity())
						.append("telephone", owner.getTelephone()).toString());
	}

	@Test
	@DisplayName("Test the addition of pet to a different owner")
	void testAddPetToADifferentOwner() {
		// Create a different owner
		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setFirstName("Second");
		owner2.setLastName("Owner");
		owner2.setAddress("University Street");
		owner2.setCity("Montréal");
		owner2.setTelephone("+1(514)-222-2222");

		// Add the same pet to the second owner
		owner2.addPet(pet);

		// Check if the pet's owner is properly set to the new owner
		assertEquals(pet.getOwner().getId(), 2);
		assertEquals(pet.getOwner().getFirstName(), "Second");
		assertEquals(pet.getOwner().getLastName(), "Owner");
		assertEquals(pet.getOwner().getAddress(), "University Street");
		assertEquals(pet.getOwner().getCity(), "Montréal");
		assertEquals(pet.getOwner().getTelephone(), "+1(514)-222-2222");

		// Check the new owner's pet list is properly updated
		assertEquals(owner2.getPets().size(), 1);
		assertEquals(owner2.getPets().get(0).getId(), PET_ID);
		assertEquals(owner2.getPets().get(0).getName(), PET_NAME);
		assertEquals(owner2.getPets().get(0).getBirthDate(), PET_BIRTH_DATE);

	}

}
