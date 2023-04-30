package org.springframework.samples.petclinic.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PetRepositoryTests {

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private TestEntityManager entityManager;

	private Pet pet;

	private PetType petType;

	private Owner owner;

	@BeforeEach
	void setup() {
		// Create Owner instance to be associated with Pet instance
		owner = new Owner();
		owner.setFirstName("Owner");
		owner.setLastName("Test");
		owner.setTelephone("5141111111");
		owner.setCity("Montréal");
		owner.setAddress("2000 University Street");

		// Create Pet Type instance to be associated Pet instance
		petType = new PetType();
		petType.setName("turtle");

		// Create new Pet instance
		pet = new Pet();
		pet.setName("Uzi");
		pet.setType(petType);
		pet.setOwner(owner);
		pet.setBirthDate(LocalDate.parse("2019-07-08"));

	}

	@Test
	void testFindPetTypes() {
		// Get size of list that is returned when findPetTypes() method is invoked
		int size1 = petRepository.findPetTypes().size();

		// Persist owner, petType and pet
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);
		// Get size of list that is returned when findPetTypes() method is invoked
		int size2 = petRepository.findPetTypes().size();

		assertTrue(size1 < size2); // Assert that size1 < size 2 which means
									// findPetTypes() functions correctly
		assertTrue(petRepository.findPetTypes().contains(pet.getType())); // Assert that
																			// it contains
																			// our pet's
																			// type
	}

	@Test
	void testFindByIncorrectId() {
		// Persist owner, petType and pet
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);

		// Assert that a null object is returned when using the wrong ID
		assertNull(petRepository.findById(8291));
	}

	@Test
	void testFindByCorrectId() {
		// Persist owner, petType and pet
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);

		// Assert the correct pet object is returned when using the correct ID
		assertEquals(pet.getName(), petRepository.findById((Integer) entityManager.getId(pet)).getName());
	}

	@Test
	void testSaveInsert() {
		// Persist owner, petType and pet
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);

		// Persist pet using save
		petRepository.save(pet);

		// Retrieve the persisted pet by id and check if all the fields were correctly
		// saved
		Pet foundPet = petRepository.findById((int) entityManager.getId(pet));
		assertEquals(foundPet.getId(), pet.getId());
		assertEquals(pet.getName(), foundPet.getName());
		assertEquals(pet.getVisits(), foundPet.getVisits());
		assertEquals(pet.getOwner(), foundPet.getOwner());
		assertEquals(pet.getVisitsInternal(), foundPet.getVisitsInternal());
		assertEquals(pet.getType(), foundPet.getType());

	}

	@Test
	void testSaveUpdate() {
		// Persist the pet and update a field
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);

		pet.setName("Cookie");
		pet.setBirthDate(LocalDate.parse("2016-07-08"));

		// Save pet with the new modifications
		petRepository.save(pet);

		// Retrieve it from the database and check the change was correctly updated
		Pet foundPet = petRepository.findById((int) entityManager.getId(pet));
		assertEquals(foundPet.getId(), pet.getId());
		assertEquals(pet.getName(), foundPet.getName());
		assertEquals(pet.getVisits(), foundPet.getVisits());
		assertEquals(pet.getOwner(), foundPet.getOwner());
		assertEquals(pet.getVisitsInternal(), foundPet.getVisitsInternal());
		assertEquals(pet.getType(), foundPet.getType());

	}

}
