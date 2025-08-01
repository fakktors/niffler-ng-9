package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private static final Faker faker = new Faker();

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(annotation -> {
          CategoryJson category = new CategoryJson(
              null,
              faker.animal().name(),
              annotation.username(),
              annotation.archived()
          );

          CategoryJson created = spendApiClient.createCategory(category);

          if (annotation.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                created.id(),
                created.name(),
                created.username(),
                true
            );
            created = spendApiClient.updateCategory(archivedCategory);
          }

          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              created
          );
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (!category.archived()) {
      category = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );

      spendApiClient.updateCategory(category);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdCategory();
  }

  public static CategoryJson createdCategory() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), CategoryJson.class);
  }
}
