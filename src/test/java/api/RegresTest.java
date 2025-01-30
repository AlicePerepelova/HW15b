package api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RegresTest {
  String URL = "https://reqres.in/";

  @Test
  @DisplayName("Успешная регистрация пользователя")
  public void successRegTest() {
    Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
    Register user = new Register("eve.holt@reqres.in", "pistol");
    Integer id = 4;
    String token = "QpwL5tke4Pnpja7X4";
    SuccessReg successReg = given()
      .body(user)
      .when()
      .post("/api/register")
      .then().log().all()
      .extract().as(SuccessReg.class);
    Assertions.assertEquals(id, successReg.getId());
    Assertions.assertEquals(token, successReg.getToken());
  }
}