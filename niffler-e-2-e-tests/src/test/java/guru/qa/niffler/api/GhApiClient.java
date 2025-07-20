package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.config.Config;
import static java.util.Objects.requireNonNull;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GhApiClient {

  private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";
  private static final Config CFG = Config.getInstance();

  private static final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.ghUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final GhApi ghApi = retrofit.create(GhApi.class);

  @SneakyThrows
  public String issueStatus(String issueNumber) {
    JsonNode response = ghApi.issue(
        "Bearer " + System.getenv(GH_TOKEN_ENV),
        issueNumber
    ).execute().body();

    return requireNonNull(response).get("state").asText();
  }
}
