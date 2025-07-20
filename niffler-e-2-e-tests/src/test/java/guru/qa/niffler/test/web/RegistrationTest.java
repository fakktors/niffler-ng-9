package guru.qa.niffler.test.web;


import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();
  private static final Faker faker = new Faker();

  @Test
  void shouldRegisterNewUser() {
    String newUsername = faker.name().username();
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .registrationFormDisplayed()
        .fillRegisterPage(newUsername, password, password)
        .successSubmit()
        .successLogin(newUsername, password)
        .checkThatPageLoaded();
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername() {
    String existingUsername = "test";
    String password = "12341234";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);

    loginPage.createNewAccount()
        .registrationFormDisplayed()
        .fillRegisterPage(existingUsername, password, password)
        .clickOnRegisterButton();

    loginPage.checkError("Username `" + existingUsername + "` already exists");
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String newUsername = faker.name().username();
    String password = "12345";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);

    loginPage.createNewAccount()
        .registrationFormDisplayed()
        .fillRegisterPage(newUsername, password, "bad password submit")
        .clickOnRegisterButton();

    loginPage.checkError("Passwords should be equal");
  }

  @Test
  void shouldShowErrorPasswordAllowedLengthSmall() {
    String newUsername = faker.name().username();
    String password = "11";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);

    loginPage.createNewAccount()
        .registrationFormDisplayed()
        .fillRegisterPage(newUsername, password, password)
        .clickOnRegisterButton();

    loginPage.checkError("Allowed password length should be from 3 to 12 characters");
  }

  @Test
  void shouldShowErrorPasswordAllowedLengthLong() {
    String newUsername = faker.name().username();
    String password = "1231231231212";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);

    loginPage.createNewAccount()
        .registrationFormDisplayed()
        .fillRegisterPage(newUsername, password, password)
        .clickOnRegisterButton();

    loginPage.checkError("Allowed password length should be from 3 to 12 characters");
  }
}
