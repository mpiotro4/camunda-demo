Feature: Test Camunda Process with Cucumber

  Scenario: Calculate the sum of two numbers
    Given I have a calculator
    When I add 2 and 3
    Then the result should be 5

  Scenario: Testing Camunda Process Deployment
    Given a Camunda process engine is set up
    When the process is deployed
    Then the process should be deployed
    When process instance is started
    Then the process should be available
