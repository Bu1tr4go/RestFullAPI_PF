Feature: Booking API Testing
  Background: Tests for GET, POST, and invalid input handling on booking endpoints.

  Scenario: Fetch all booking IDs
    Given Initiate request to get booking IDs
    Then Validate the response status code 200
    And Ensure the response size is not 0

  Scenario: Fetch a booking by ID
    Given Fetch booking by ID 13
    Then Validate the response status code 200
    And Validate booking details are not empty

  Scenario: Validate behavior for invalid booking ID
    Given Fetch booking by string ID "*/-"
    Then Validate the response status code 404

  Scenario: Create a new booking with valid data
    Given Initiate request to create booking
      | Daniel | Torrico | 100 | true | 2024-09-5 | 2024-10-5 | Breakfast |
    Then Validate the response status code 200

  Scenario: Validate Datatype control on Post request
    Given Initiate request to create booking
      | 123 | *- | 123 | 1 | a | b | Breakfast |
    Then Validate the response status code 400

  @Run
  Scenario: Validate behavior when sending empty dates
    Given Initiate request to create booking with empty dates
    Then Validate the response status code 400
