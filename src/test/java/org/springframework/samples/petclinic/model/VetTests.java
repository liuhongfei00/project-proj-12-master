/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mohammad Saeid Nafar
 */
class VetTests {

	private Vet vet;

	@BeforeEach
	void setUp() {
		// Create new Vet instance to use throughout testing and avoid redundancy
		vet = new Vet();
		vet.setFirstName("Zaphod");
		vet.setLastName("Beeblebrox");
		vet.setId(123);
		vet.setSpecialtiesInternal(null);
	}

	@Test
	@DisplayName("Test the Serialization of a Vet object")
	void testSerialization() {
		Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
		assert other != null;
		assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
		assertThat(other.getLastName()).isEqualTo(vet.getLastName());
		assertThat(other.getId()).isEqualTo(vet.getId());
	}

	@Test
	@DisplayName("Test the Specialties Internal getter of a Vet object")
	void testGetSpecialtiesInternal() {
		// Create new instances of type Specialty and set their name fields
		Specialty specialty1 = new Specialty();
		specialty1.setName("Dentistry");
		Specialty specialty2 = new Specialty();
		specialty2.setName("Surgeon");

		// Create a new Set of type Specialty and add the previously created specialty
		// instances to the set
		Set<Specialty> specialtySet = new HashSet<>();
		specialtySet.add(specialty1);
		specialtySet.add(specialty2);

		vet.setSpecialtiesInternal(specialtySet); // Associate the set to out vet object

		// Assert that specialties internal of vet is not empty
		assertFalse(vet.getSpecialtiesInternal().isEmpty());
		// Assert that specialties internal of vet contains specialty1
		assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
		// Assert that specialties internal of vet contains specialty2
		assertTrue(vet.getSpecialtiesInternal().contains(specialty2));
		// Assert that specialties internal of vet is the same as the specialty set we
		// created
		assertEquals(specialtySet, vet.getSpecialtiesInternal());
	}

	@Test
	@DisplayName("Test the specialties getter of a Vet object")
	void testGetSpecialties() {
		// Create new instances of type Specialty and set their name fields
		Specialty specialty1 = new Specialty();
		specialty1.setName("Dentistry");
		specialty1.setId(123);
		Specialty specialty2 = new Specialty();
		specialty2.setName("Surgeon");
		specialty2.setId(456);

		// Create a new Set of type Specialty and add the previously created specialty
		// instances to the set
		Set<Specialty> specialtySet = new HashSet<>();
		specialtySet.add(specialty1);
		specialtySet.add(specialty2);

		vet.setSpecialtiesInternal(specialtySet); // Associate the set to out vet object

		// Assert that specialties internal of vet is not empty
		assertFalse(vet.getSpecialties().isEmpty());
		// Assert that the size of the specialty set associated with our vet instance is 2
		assertEquals(2, vet.getSpecialties().size());
		// Assert that the first element of the specialty set retrieved from our pet
		// instance is specialty1 by checking the name it returns
		assertEquals(specialty1.getName(), vet.getSpecialties().get(0).getName());
		// Assert that the second element of the specialty set retrieved is specialty2 by
		// checking the name it returns
		assertEquals(specialty2.getName(), vet.getSpecialties().get(1).getName());
		// Assert that the first element of the specialty set retrieved is specialty1 by
		// checking the ID it returns
		assertEquals(specialty1.getId(), vet.getSpecialties().get(0).getId());
		// Assert that the second element of the specialty set retrieved is specialty2 by
		// checking the ID it returns
		assertEquals(specialty2.getId(), vet.getSpecialties().get(1).getId());

		List<Specialty> specialtyTestList = new ArrayList<>();
		specialtyTestList.add(specialty1);
		specialtyTestList.add(specialty2);

		assertEquals(specialtyTestList, vet.getSpecialties()); // Assert that it retrieves
																// the correct list
	}

	@Test
	@DisplayName("Test the addSpecialty() method of a Vet object")
	void testAddSpecialty() {
		// Create new instances of type Specialty and set their name fields
		Specialty specialty1 = new Specialty();
		specialty1.setName("Dentistry");
		specialty1.setId(123);
		Specialty specialty2 = new Specialty();
		specialty2.setName("Surgeon");
		specialty2.setId(456);

		// Use the addSpecialty() method to associate the specialty instances to out vet
		// object
		vet.addSpecialty(specialty1);
		vet.addSpecialty(specialty2);

		// Run tests to ensure that the specialty instances were added correctly
		assertFalse(vet.getSpecialties().isEmpty());
		assertEquals(2, vet.getSpecialties().size());
		assertEquals(specialty1.getName(), vet.getSpecialties().get(0).getName());
		assertEquals(specialty2.getName(), vet.getSpecialties().get(1).getName());
		assertEquals(specialty1.getId(), vet.getSpecialties().get(0).getId());
		assertEquals(specialty2.getId(), vet.getSpecialties().get(1).getId());
	}

	@Test
	@DisplayName("Test getNrOfSpecialties() method of a Vet object")
	void testGetNumberOfSpecialties() {

		// Create 5 Specialty objects and add them all to our vet object using
		// addSpecialty() function
		Specialty specialty1 = new Specialty();
		vet.addSpecialty(specialty1);
		Specialty specialty2 = new Specialty();
		vet.addSpecialty(specialty2);
		Specialty specialty3 = new Specialty();
		vet.addSpecialty(specialty3);
		Specialty specialty4 = new Specialty();
		vet.addSpecialty(specialty4);
		Specialty specialty5 = new Specialty();
		vet.addSpecialty(specialty5);

		// Check if getNrOfSpecialties() method functions correctly
		assertEquals(5, vet.getNrOfSpecialties());
		assertEquals(vet.getSpecialties().size(), vet.getNrOfSpecialties());
	}

}
