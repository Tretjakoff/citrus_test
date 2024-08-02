package api;

import api.behaviors.HttpMockCourseBehavior;
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
public class TestCourse {

  @BeforeEach
  public void init(@CitrusResource TestCaseRunner runner) {
    runner.$(load("file:src/test/resources/load.properties"));
  }

  @CitrusTest
  @Test
  @DisplayName("Тест на проверку курсов")
  public void test(@CitrusResource TestCaseRunner runner) {

    runner.run(http()
        .client("httpClientMock")
        .send()
        .get("cource/get/all")
        .fork(true)
    );

    runner.run(apply(new HttpMockCourseBehavior()));

    runner.run(http()
        .client("httpClientMock")
        .receive()
        .response(HttpStatus.OK)
        .message()
        .name("msg")
        .validate(jsonPath()
            .expression("$[0].name", "QA java")
            .expression("$.size()", "2")
            .expression("$[0].price", "@isNumber()@"))
    );
  }
}
