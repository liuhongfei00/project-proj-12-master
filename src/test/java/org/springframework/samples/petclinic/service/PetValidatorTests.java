package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class PetValidatorTests {

	private PetValidator petValidator;

	private Pet pet;

	private Errors errors;

	@BeforeEach
	void setUp() {
		// Create new PetValidator object and a new Pet object with a Pet Type and a
		// birthdate
		petValidator = new PetValidator();
		pet = new Pet();
		pet.setName("Uzi");

		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);

		LocalDate birthDate = LocalDate.parse("2019-07-08");
		pet.setBirthDate(birthDate);

		errors = new BeanPropertyBindingResult(pet, "pet"); // Instantiate new Errors
															// instance
	}

	@Test
	@DisplayName("Tests if the valid creation of a Pet object will raise errors")
	void testValidatePet() {
		// Check that the creation of our Pet object was done correctly and resulted in no
		// errors
		petValidator.validate(pet, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	@DisplayName("Tests if an invalid name of a Pet Object will be rejected")
	void testRejectInvalidName() {
		pet.setName(null); // Set Pet name to Null to raise an error

		// Check that Pet object now results in an error and ensure that this error is
		// caused due the pet name being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals("name", Objects.requireNonNull(errors.getFieldError()).getField().toString());
	}

	@Test
	@DisplayName("Tests if an invalid type of a Pet Object will be rejected")
	void testRejectInvalidType() {
		pet.setId(null); // Set Pet ID to Null
		pet.setType(null); // Set Pet type to Null

		// Check that Pet object now results in an error and ensure that this error is
		// caused due pet type being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals("type", Objects.requireNonNull(errors.getFieldError()).getField().toString());
	}

	@Test
	@DisplayName("Tests if an invalid birthdate of a Pet Object will be rejected")
	void testRejectInvalidBirthDate() {
		pet.setBirthDate(null); // Set Pet Birthdate to Null

		// Check that Pet object now results in an error and ensure that this error is
		// caused due pet birthdate being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getErrorCount());
		assertEquals("birthDate", Objects.requireNonNull(errors.getFieldError()).getField().toString());
	}

	@Test
	@DisplayName("Tests if an invalid name and type of a Pet Object will be rejected")
	void testRejectInvalidNameAndType() {
		pet.setName(""); // Set Pet name to Null to raise an error
		pet.setId(null); // Set Pet ID to Null
		pet.setType(null); // Set Pet type to Null

		// Check that Pet object now results in an error and ensure that this error is caused due pet type being null
		// and name being of lengh 0
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(2, errors.getErrorCount());
		assertEquals("name", Objects.requireNonNull(errors.getFieldError("name")).getField().toString());
		assertEquals("type", Objects.requireNonNull(errors.getFieldError("type")).getField().toString());

	}

	@Test
	@DisplayName("Tests if an invalid name and birthdate of a Pet Object will be rejected")
	void testRejectInvalidNameAndBd() {
		pet.setName(""); // Set Pet name to Null to raise an error
		pet.setBirthDate(null); // Set Pet ID to Null

		// Check that Pet object now results in an error and ensure that this error is caused due pet type being null
		// and birthdate being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(2, errors.getErrorCount());
		assertEquals("name", Objects.requireNonNull(errors.getFieldError("name")).getField().toString());
		assertEquals("birthDate", Objects.requireNonNull(errors.getFieldError("birthDate")).getField().toString());

	}

	@Test
	@DisplayName("Tests if an invalid type and birthdate of a Pet Object will be rejected")
	void testRejectInvalidTypeAndBd() {
		pet.setId(null); // Set Pet ID to Null
		pet.setType(null); // Set Pet name to Null to raise an error
		pet.setBirthDate(null); // Set Pet ID to Null

		// Check that Pet object now results in an error and ensure that this error is caused due pet type being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(2, errors.getErrorCount());
		assertEquals("type", Objects.requireNonNull(errors.getFieldError("type")).getField().toString());
		assertEquals("birthDate", Objects.requireNonNull(errors.getFieldError("birthDate")).getField().toString());

	}

	@Test
	@DisplayName("Tests if an invalid name, type and birthdate of a Pet Object will be rejected")
	void testRejectAllInvalid() {
		pet.setName("");
		pet.setId(null); // Set Pet ID to Null
		pet.setType(null); // Set Pet name to Null to raise an error
		pet.setBirthDate(null); // Set Pet ID to Null

		// Check that Pet object now results in an error and ensure that this error is caused due pet type being null
		petValidator.validate(pet, errors);
		assertTrue(errors.hasErrors());
		assertEquals(3, errors.getErrorCount());
		assertEquals("name", Objects.requireNonNull(errors.getFieldError("name")).getField().toString());
		assertEquals("type", Objects.requireNonNull(errors.getFieldError("type")).getField().toString());
		assertEquals("birthDate", Objects.requireNonNull(errors.getFieldError("birthDate")).getField().toString());

	}



	@Test
	@DisplayName("Tests which classes the petValidator class supports")
	void testSupportValidation() {

		// Check that pet validator only supports the Pet class and nothing else
		assertTrue(petValidator.supports(pet.getClass()));
		assertFalse(petValidator.supports(Vet.class));
		assertFalse(petValidator.supports(Visit.class));
	}

}
