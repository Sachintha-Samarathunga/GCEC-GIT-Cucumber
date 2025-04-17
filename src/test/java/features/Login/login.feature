Feature: Login to the system
  As a user
  I should be able to login to the system

  Background:tesy@sem.com
    Given I have opened the system
    When I set the browser to full screen

  @Login
  Scenario: Verify login with valid credentials 2

    When I type "customer4@gmail.com" to the "Login_emailField"
    Then I type "123456@Cd" to the "Login_pswField"

    And I click on "Login_btn"

    And I wait few seconds


#    Examples:
#      | username | password |
#      | username | password |


