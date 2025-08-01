package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {

  private static final GhApiClient ghApiClient = new GhApiClient();

  @SneakyThrows
  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    Optional<DisabledByIssue> annotation;

    annotation = AnnotationSupport.findAnnotation(
        context.getRequiredTestClass(),
        DisabledByIssue.class,
        SearchOption.INCLUDE_ENCLOSING_CLASSES
    );

    if (context.getTestMethod().isPresent() && annotation.isEmpty()) {
      annotation = AnnotationSupport.findAnnotation(
          context.getRequiredTestMethod(),
          DisabledByIssue.class
      );
    }

    return annotation.map(
        byIssue -> "open".equals(ghApiClient.issueStatus(byIssue.value()))
            ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())
            : ConditionEvaluationResult.enabled("Issue closed")
    ).orElseGet(
        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
    );
  }
}
