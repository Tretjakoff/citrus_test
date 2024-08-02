package api.behaviors;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import dto.http.CourseDto;

import java.util.List;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class HttpMockCourseBehavior implements TestBehavior {

  @Override
  public void apply(TestActionRunner testActionRunner) {
    testActionRunner.run(http()
        .server("httpMock")
        .receive()
        .get("/cource/get/all")
    );

    testActionRunner.run(http()
        .server("httpMock")
        .send()
        .response()
        .message()
        .type(MessageType.JSON)
        .body(new ObjectMappingPayloadBuilder(getCourses(), "objectMapper"))
    );
  }

  public List<CourseDto> getCourses() {
    CourseDto courseDto1 = new CourseDto();
    courseDto1.setName("QA java");
    courseDto1.setPrice(15000);
    CourseDto courseDto2 = new CourseDto();
    courseDto2.setName("Java");
    courseDto2.setPrice(12000);
    return List.of(courseDto1, courseDto2);
  }
}
