package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.UserProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @Category(
      username = "test",
      archived = true
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("test", "12341234")
        .checkThatPageLoaded();

    Selenide.open(CFG.frontUrl() + "profile", UserProfilePage.class)
        .checkArchivedCategoryExists(category.name());
  }

  @Category(
      username = "test"
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .successLogin("test", "12341234")
        .checkThatPageLoaded();

    Selenide.open(CFG.frontUrl() + "profile", UserProfilePage.class)
        .checkCategoryExists(category.name());
  }
}
