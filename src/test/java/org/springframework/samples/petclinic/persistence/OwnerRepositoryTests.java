package org.springframework.samples.petclinic.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Owner;

@DataJpaTest
public class OwnerRepositoryTests {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private TestEntityManager entityManager;

	private Owner owner;

	@BeforeEach
	void setup() {
		// Create an owner instance
		owner = new Owner();
		owner.setFirstName("Owner");
		owner.setLastName("Test");
		owner.setTelephone("5141111111");
		owner.setCity("Montréal");
		owner.setAddress("2000 University Street");
	}

	@Test
	void testFindByLastNameNoOwners() {
		// Try getting an owner by a last name that does not exist
		Collection<Owner> owners = ownerRepository.findByLastName("Wrong");
		assertEquals(owners.size(), 0);
	}

	@Test
	void testFindByLastNameOneOwner() {
		// Persist owner and make sure it is found properly by last name
		entityManager.persist(owner);
		Collection<Owner> owners = ownerRepository.findByLastName("Test");
		assertEquals(owners.size(), 1);
		assertTrue(owners.contains(owner));
	}

	@Test
	void testFindByLastNameTwoOwners() {
		// Create a second owner instance
		Owner owner2 = new Owner();
		owner2.setFirstName("Second");
		owner2.setLastName("Test");
		owner2.setTelephone("5142222222");
		owner2.setCity("Toronto");
		owner2.setAddress("2001 University Street");

		// Persit the two owner instances
		entityManager.persist(owner);
		entityManager.persist(owner2);

		Collection<Owner> owners = ownerRepository.findByLastName("Test");

		// Assert there were two owners found with that last name
		assertEquals(owners.size(), 2);
	}

	@Test
	void testFindByIdExists() {
		// Persist owner and try retrieving the instance using its id.
		entityManager.persist(owner);
		Owner foundOwner = ownerRepository.findById((int) entityManager.getId(owner));
		// Assert the proper owner with the correct fields was returned
		assertEquals(foundOwner.getId(), entityManager.getId(owner));
		assertEquals(foundOwner.getFirstName(), owner.getFirstName());
		assertEquals(foundOwner.getLastName(), owner.getLastName());
		assertEquals(foundOwner.getCity(), owner.getCity());
		assertEquals(foundOwner.getAddress(), owner.getAddress());
		assertEquals(foundOwner.getTelephone(), owner.getTelephone());
	}

	@Test
	void testFindByIdDoesNotExist() {
		// Try retrieving an owner with an id that does not exist
		Owner foundOwner = ownerRepository.findById(0);
		assertEquals(foundOwner, null);
	}

	@Test
	void testSaveInsert() {
		// Persist the owner using save
		ownerRepository.save(owner);
		// Retrieve the persisted owner by id and check all the fields were properly
		// saved
		Owner foundOwner = ownerRepository.findById((int) entityManager.getId(owner));
		assertEquals(foundOwner.getFirstName(), owner.getFirstName());
		assertEquals(foundOwner.getLastName(), owner.getLastName());
		assertEquals(foundOwner.getCity(), owner.getCity());
		assertEquals(foundOwner.getAddress(), owner.getAddress());
		assertEquals(foundOwner.getTelephone(), owner.getTelephone());
	}

	@Test
	void testSaveUpdate() {
		// Persist the owner and update the city
		entityManager.persist(owner);
		owner.setCity("Paris");

		// Save owner with the new modifications
		ownerRepository.save(owner);

		// Retrieve it from the database and check the change was properly updated
		Owner foundOwner = ownerRepository.findById((int) entityManager.getId(owner));
		assertEquals(foundOwner.getCity(), "Paris");
	}

}
