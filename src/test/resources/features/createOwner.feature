Feature: Create a new owner
  As an employee of the veterinarian clinic, 
  I want to be able to create a new owner profile with name, address and phone number,
  So that I can keep records for this owner and their pets.

  Scenario Outline: Create a new owner successfully
    When the user attempts to add an owner with "<firstName>", "<lastName>", "<address>", "<city>" and "<phone>"
    Then a new owner profile shall be created
    And the profile has "<firstName>", "<lastName>", "<address>", "<city>" and "<phone>"
    
    Examples: 
      | firstName	| lastName	| address			| city			| phone				|
      | John	 		| Doe				| 123 street	|	Test Town	|	1234567890	|
      | Don	 			| Jones			| 456 avenue	|	Test Town	|	0987654321	|


  Scenario Outline: Create a new owner with incomplete form
    When the user attempts to add an owner with "<firstName>", "<lastName>", "<address>", "<city>" and "<phone>"
    Then no new owner profile shall be created
    And the form shows this "<error>" message
    
    Examples: 
      | firstName	| lastName	| address			| city			| phone				| error							|
      | 			 		| Doe				| 123 street	|	Test Town	|	1234567890	|	must not be empty	|
      | John	 		| 					| 123 street	|	Test Town	|	1234567890	|	must not be empty	|
      | John		 	| Doe				| 						|	Test Town	|	1234567890	|	must not be empty	|
      | John			| Doe				| 123 street	|						|	1234567890	|	must not be empty	|
      | John	 		| Doe				| 123 street	|	Test Town	|							|	must not be empty	|
      
    
  Scenario Outline: Create a new owner with invalid phone number
    When the user attempts to add an owner with "<firstName>", "<lastName>", "<address>", "<city>" and "<phone>"
    Then no new owner profile shall be created
    And the form shows this "<error>" message
    
    Examples: 
      | firstName	| lastName	| address			| city			| phone					| error												|
      | John			| Doe				| 123 street	|	Test Town	|	1234567890123	|	numeric value out of bounds	|
      | John			| Doe				| 123 street	|	Test Town	|	1234abc5678		|	numeric value out of bounds	|
      | John			| Doe				| 123 street	|	Test Town	|	123-456-7890	|	numeric value out of bounds	|
   