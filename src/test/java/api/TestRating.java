package api;

import api.behaviors.HttpMockRatingBehavior;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.junit.jupiter.CitrusExtension;
import com.consol.citrus.junit.jupiter.CitrusSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.actions.ApplyTestBehaviorAction.Builder.apply;
import static com.consol.citrus.actions.LoadPropertiesAction.Builder.load;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты с применением заглушек")
public class TestRating {

  @BeforeEach
  public void init(@CitrusResource TestCaseRunner runner) {
    runner.$(load("file:src/test/resources/load.properties"));
  }

  @CitrusTest
  @Test
  @DisplayName("Тест на проверку оценки")
  public void test(@CitrusResource TestCaseRunner runner) {

    int id = 100;

    runner.run(http()
        .client("httpClientMock")
        .send()
        .get("/user/get/" + id)
        .fork(true)
    );

    runner.run(
        apply(new HttpMockRatingBehavior(id))
    );


    runner.run(http()
        .client("httpClientMock")
        .receive()
        .response(HttpStatus.OK)
        .message()
        .name("msg")
        .validate(jsonPath()
            .expression("$.name", "Test user")
            .expression("$.score", "@isNumber()@")
            .expression("$.score", 78)
        )
    );
  }
}
