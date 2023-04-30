Feature: Creating a visit for a pet
  As an employee of the veterinarian clinic, 
  I want to be able to add a visit for a pet with a visit date and description,
  So that I can keep a record of every visits that were made for this pet.

  Scenario Outline: Create a visit successfully
  	Given there is an owner in the system
  	And the owner has a pet with "<name>"
  	When the user attempts to create a visit with "<date>" and "<description>"
  	Then a new visit was created
  	And the visit has "<date>" and "<description>"
    
    Examples: 
      | name	| date				| description	|
      | Odie	| 2020-01-01	| Vaccine		 	|
      | Luky	| 2019-08-12	| Checkup		 	|
      | Toro	| 2022-10-21	| Vaccine 		|
      | Soup	| 2018-03-25	| Routine			|


  Scenario Outline: Create a visit with incomplete form
  	Given there is an owner in the system
  	And the owner has a pet with "<name>"
  	When the user attempts to create a visit with "<date>" and "<description>"
  	Then no new visit was created
  	And the form shows this "<error>" message
    
    Examples: 
      | name	| date				| description	|	error							|
      | Odie	| 						| Vaccine		 	|	must not be empty	|
      | Luky	| 2019-08-12	| 					 	|	must not be empty	|
      | Toro	| 						| 				 		|	must not be empty	|
      
    
  Scenario: Create a visit with date before pet birth date
  	Given there is an owner in the system
  	And the owner has a pet
  	When the user attempts to create a visit with a date earlier than the pet birthdate
  	Then no new visit was created
  	And the form shows the birthdate error message 
  