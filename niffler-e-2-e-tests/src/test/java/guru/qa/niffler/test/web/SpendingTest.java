package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @Spending(
      username = "test",
      amount = 89990.00,
      description = "Advanced 9 поток!",
      category = "Обучение"
  )
  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(SpendJson spendJson) {
    final String newDescription = ":)";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("test", "12341234")
        .checkThatPageLoaded()
        .editSpending(spendJson.description())
        .setNewSpendingDescription(newDescription)
        .save()
        .checkThatTableContainsSpending(newDescription);
  }
}
