Feature: Create a new pet profile
  As an employee of the veterinarian clinic, 
  I want to be able create a new pet profile with a name, birth date and type,
  So that I can keep records of the pet informaation and visits.

  Scenario Outline: Create a pet profile successfully
  	Given there is an owner in the system
  	And the owner has no pet with "<name>"
    When the user attempts to create a new pet with "<name>", "<birth>" and "<type>"
    Then a new pet was created
    And the pet has "<name>", "<birth>" and "<type>"
    
    Examples: 
      | name	| birth				| type	|
      | Odie	| 2020-01-01	| dog 	|
      | Luky	| 2019-08-12	| cat 	|
      | Toro	| 2022-10-21	| bird 	|
      | Soup	| 2018-03-25	| snake |


  Scenario Outline: Create a pet profile with incomplete form
  	Given there is an owner in the system
  	And the owner has no pet with "<name>"
    When the user attempts to create a new pet with "<name>", "<birth>" and "<type>"
    Then no new pet was created
    And the form shows this "<error>" message
    
    Examples: 
      | name	| birth				| type	| error			|
      | 			| 2020-01-01	| dog 	|	required	|
      | Bobby	| 						| cat 	|	required	|
      | 			| 						| bird 	|	required	|
      
    
  Scenario Outline: Create a pet profile with already used name
  	Given there is an owner in the system
  	And the owner has a pet with "<name>"
    When the user attempts to create a new pet with "<name>", "<birth>" and "<type>"
    Then no new pet was created
    And the form shows this "<error>" message
    
    Examples: 
      | name	| birth				| type	| error						|
      | Odie	| 2020-01-01	| dog 	|	already exists	|
      | Luky	| 2019-08-12	| cat 	|	already exists	|
      | Toro	| 2022-10-21	| bird 	|	already exists	|
   