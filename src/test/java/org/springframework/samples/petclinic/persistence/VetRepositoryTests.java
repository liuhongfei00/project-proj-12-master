package org.springframework.samples.petclinic.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.samples.petclinic.model.Vet;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

@DataJpaTest
public class VetRepositoryTests {

	@Autowired
	VetRepository vetRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void testFindAll() {
		// Creating a first vet instance
		Vet vet = new Vet();
		vet.setFirstName("Vet");
		vet.setLastName("Test");

		// Creating a second vet instance
		Vet vet2 = new Vet();
		vet2.setFirstName("Vet2");
		vet2.setLastName("Test2");

		// Persist both instances
		entityManager.persist(vet);
		entityManager.persist(vet2);

		Collection<Vet> vets = vetRepository.findAll();

		// Check they both are in the returned collection
		assertTrue(vets.contains(vet));
		assertTrue(vets.contains(vet2));
	}

}
