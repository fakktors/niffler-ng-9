package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.text;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registrationButton = $("#register-button");
  private final SelenideElement errorContainer = $(".form__error");

  public void fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
  }

  public MainPage successLogin(String username, String password) {
    fillLoginPage(username, password);
    return new MainPage();
  }

  public RegisterPage createNewAccount() {
    registrationButton.click();
    return new RegisterPage();
  }

  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }
}
