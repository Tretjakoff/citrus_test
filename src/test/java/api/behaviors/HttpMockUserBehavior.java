package api.behaviors;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import dto.http.UserDto;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class HttpMockUserBehavior implements TestBehavior {

  @Override
  public void apply(TestActionRunner testActionRunner) {
    testActionRunner.run(http()
        .server("httpMock")
        .receive()
        .get("/user/get/all")
    );

    testActionRunner.run(http()
        .server("httpMock")
        .send()
        .response()
        .message()
        .type(MessageType.JSON)
        .body(new ObjectMappingPayloadBuilder(getUsersDto(), "objectMapper"))
    );
  }

  public UserDto getUsersDto() {
    UserDto userDto = new UserDto();
    userDto.setName("Test user");
    userDto.setCourse("QA");
    userDto.setEmail("test@test.test");
    userDto.setAge(23);
    return userDto;
  }
}
