package api;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.junit.jupiter.CitrusExtension;
import com.consol.citrus.junit.jupiter.CitrusSupport;
import feature.CustomMarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pojo.xml.com.dataaccess.webservicesserver.NumberToDollars;
import pojo.xml.com.dataaccess.webservicesserver.NumberToDollarsResponse;

import java.math.BigDecimal;

import static com.consol.citrus.actions.LoadPropertiesAction.Builder.load;
import static com.consol.citrus.ws.actions.SoapActionBuilder.soap;

@ExtendWith(CitrusExtension.class)
@CitrusSupport
@DisplayName("Тесты для тестирования сервиса SOAP")
public class TestSoap {

  public String namespaceUri = "http://www.dataaccess.com/webservicesserver/";
  public String dollarCount = "15";
  public String dollarString = "15";

  @BeforeEach
  public void init(@CitrusResource TestCaseRunner runner) {
    runner.$(load("file:src/test/resources/load.properties"));
  }

  @CitrusTest
  @Test
  @DisplayName("Тест на проверку SOAP")
  public void test() {

    CustomMarshaller<Class<NumberToDollars>> ptxRq = new CustomMarshaller<>();
    CustomMarshaller<Class<NumberToDollarsResponse>> ptxRs = new CustomMarshaller<>();
    soap()
        .client("soapClient")
        .send()
        .message()
        .body(ptxRq.convert(NumberToDollars.class, getNumberToDollarsRequest(),
            namespaceUri, "NumberToDollars"));

    soap()
        .client("soapClient")
        .receive()
        .message()
        .body(ptxRs.convert(NumberToDollarsResponse.class, getNumberToDollarsResponse(),
            namespaceUri, "NumberToDollarsResponse"));
  }

  public NumberToDollars getNumberToDollarsRequest() {
    NumberToDollars numberToDollars = new NumberToDollars();
    numberToDollars.setDNum(new BigDecimal(dollarCount));
    return numberToDollars;
  }

  public NumberToDollarsResponse getNumberToDollarsResponse() {
    NumberToDollarsResponse numberToDollarsResponse = new NumberToDollarsResponse();
    numberToDollarsResponse.setNumberToDollarsResult(dollarString);
    return numberToDollarsResponse;
  }
}
