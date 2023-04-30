package org.springframework.samples.petclinic.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VisitRepositoryTests {

	@Autowired
	private VisitRepository visitRepository;

	@Autowired
	private TestEntityManager entityManager;

	private Owner owner;

	private PetType petType;

	private Pet pet;

	private Visit visit;

	@BeforeEach
	void setup() {

		// Create Owner Instance to be associated with Pet which is associated with Visit
		owner = new Owner();
		owner.setFirstName("Owner");
		owner.setLastName("Test");
		owner.setTelephone("5141111111");
		owner.setCity("Montréal");
		owner.setAddress("2000 University Street");

		// Create Pet Type instance to be associated with Pet which is associated with
		// Visit
		petType = new PetType();
		petType.setName("turtle");

		// Create Pet instance to be associated with Visit instance
		pet = new Pet();
		pet.setName("Uzi");
		pet.setType(petType);
		pet.setOwner(owner);
		pet.setBirthDate(LocalDate.parse("2019-07-08"));

		// Persist Dependent Entities
		entityManager.persist(owner);
		entityManager.persist(petType);
		entityManager.persist(pet);

		// Instantiate Visit object
		visit = new Visit();
		visit.setPetId((Integer) entityManager.getId(pet));
		visit.setDescription("Broken Leg");
		visit.setDate(LocalDate.parse("2022-10-02"));

	}

	@Test
	void testFindByPetIdOneVisit() {
		entityManager.persist(visit); // persist visit

		// Check if findByPetId() returns a list that contains out Visit object
		List<Visit> petVisits = visitRepository.findByPetId((Integer) entityManager.getId(pet));
		assertTrue(petVisits.contains(visit));
		assertEquals(visit.getDescription(), petVisits.get(0).getDescription());
	}

	@Test
	void testFindByPetIdMoreVisits() {
		entityManager.persist(visit); // persist visit

		// CREATE NEW OWNER FOR NEW PET AND NEW VISIT
		Owner owner1 = new Owner();
		owner1.setFirstName("NewOwner");
		owner1.setLastName("NewTest");
		owner1.setTelephone("5142222222");
		owner1.setCity("Montréal");
		owner1.setAddress("2002 University Street");

		// Create new PetType instance to be associated with Pet which is associated with
		// new Visit
		PetType petType1 = new PetType();
		petType1.setName("Rabbit");

		// Create new Pet instance to be associated with new Visit instance
		Pet pet1 = new Pet();
		pet1.setName("Gucci");
		pet1.setType(petType);
		pet1.setOwner(owner);
		pet1.setBirthDate(LocalDate.parse("2018-06-02"));

		// Persist Dependent Entities
		entityManager.persist(owner1);
		entityManager.persist(petType1);
		entityManager.persist(pet1);

		// Instantiate New Visit object
		Visit visit1 = new Visit();
		visit1.setPetId((Integer) entityManager.getId(pet));
		visit1.setDescription("Injured Eye");
		visit1.setDate(LocalDate.parse("2022-11-02"));

		entityManager.persist(visit1); // persist new visit

		// Check if findByPetId() returns a list that contains out Visit objects
		List<Visit> petVisits1 = visitRepository.findByPetId((Integer) entityManager.getId(pet));
		assertTrue(petVisits1.contains(visit));
		assertTrue(petVisits1.contains(visit1));
		assertEquals(visit.getDescription(), petVisits1.get(0).getDescription());
		assertEquals(visit1.getDescription(), petVisits1.get(1).getDescription());
	}

	@Test
	void testFindByIncorrectPetId() {
		entityManager.persist(visit); // persist visit

		// Assert that findByPetId() returns an empty list when provided an incorrect Pet
		// ID as input
		List<Visit> petVisits = visitRepository.findByPetId(726);
		assertFalse(petVisits.contains(visit));
		assertTrue(petVisits.isEmpty());
	}

	@Test
	void testSaveInsert() {

		entityManager.persist(visit); // persist visit

		// Persist pet using save
		visitRepository.save(visit);

		// Retrieve the persisted pet by id and check if all the fields were correctly
		// saved
		List<Visit> petVisits = visitRepository.findByPetId((Integer) entityManager.getId(pet));
		assertTrue(petVisits.contains(visit));
		assertEquals(visit.getDescription(), petVisits.get(0).getDescription());
		assertEquals(visit.getDate(), petVisits.get(0).getDate());
		assertNotNull(petVisits.get(0).getId());

	}

	@Test
	void testSaveUpdate() {
		// Persist visit and update a field
		entityManager.persist(visit); // persist visit

		visit.setDescription("Checkup");
		visit.setDate(LocalDate.parse("2022-10-01"));

		// Persist pet using save
		visitRepository.save(visit);

		// Retrieve the persisted pet by id and check if all the fields were correctly
		// saved
		List<Visit> petVisits = visitRepository.findByPetId((Integer) entityManager.getId(pet));
		assertTrue(petVisits.contains(visit));
		assertEquals(visit.getDescription(), petVisits.get(0).getDescription());
		assertEquals(visit.getDate(), petVisits.get(0).getDate());
		assertNotNull(petVisits.get(0).getId());

	}

}
