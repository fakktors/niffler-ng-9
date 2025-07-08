package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import com.codeborne.selenide.SelenideElement;

public class RegisterPage {
  private final SelenideElement registerForm = $("#register-form");
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement registerButton = $("#register-button");
  private final SelenideElement errorContainer = $(".form__error");
  private final SelenideElement proceedLoginButton = $(".form_sign-in");

  public RegisterPage registrationFormDisplayed() {
    registerForm.shouldBe(visible);
    return this;
  }

  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    usernameInput.setValue(login);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  public LoginPage successSubmit() {
    clickOnRegisterButton();
    proceedLoginButton.click();
    return new LoginPage();
  }

  public void clickOnRegisterButton() {
    registerButton.click();
  }

  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }
}
