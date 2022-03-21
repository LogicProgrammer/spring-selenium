Feature: google search


  Scenario: google search scenario
    Given user is on google home
    When user searches for "selenium"
    Then user gets the results