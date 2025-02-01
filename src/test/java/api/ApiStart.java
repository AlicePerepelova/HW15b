package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiStart {
  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = "https://reqres.in";
    RestAssured.basePath = "/api";
  }

  @Test
  @DisplayName("Успешная проверка пользователя по id и email")
  void checkUser() {
    given()
      .log().uri()
      .when()
      .get("/users/2")
      .then()
      .log().body()
      .statusCode(200)
      .body("data.id", is(2))
      .body("data.email", is("janet.weaver@reqres.in"));
  }

  @Test
  @DisplayName("Проверка, что в списке 6 пользователей")
  void checkListUserSize() {

    given()
      .log().uri()
      .when()
      .queryParam("page", "2")
      .get("/users")
      .then()
      .log().body()
      .statusCode(200)
      .body("data.id", hasSize(6));
  }

  @Test
  @DisplayName("Успешное создание пользователя")
  void successfulcheckCreateUser() {
    String newUser = """
      {
      "name": "morpheus", "job": "leader"
      }
      """;
    given()
      .body(newUser)
      .log().uri()
      .contentType(ContentType.JSON)
      .when()
      .post("/users")
      .then()
      .log().status()
      .log().body()
      .statusCode(201)
      .body("name", is("morpheus"))
      .body("job", is("leader"))
      .body("id", is(notNullValue()))
      .body("createdAt", is(notNullValue()));
  }

  @Test
  @DisplayName("Неуспешное создание пользователя с некорректными данными")
  void unsuccessfulcheckCreateUser() {
    String newUser = """
      {
      "name": , "job": "leader"
      }
      """;
    given()
      .body(newUser)
      .log().uri()
      .contentType(ContentType.JSON)
      .when()
      .post("/users")
      .then()
      .log().status()
      .log().body()
      .statusCode(400);
  }

  @Test
  @DisplayName("Успешная регистрация пользователя")
  void successfulRegister() {
    String user = """
      {
          "email": "eve.holt@reqres.in",
          "password": "pistol"
      }
      """;
    given()
      .body(user)
      .log().uri()
      .contentType(ContentType.JSON)
      .when()
      .post("/register")
      .then()
      .statusCode(200)
      .body("id", is(notNullValue()))
      .body("token", is(notNullValue()))
      .log().status()
      .log().body();
  }

  @Test
  @DisplayName("Удаление пользователя")
  void checkDeleteUser() {
    given()
      .when()
      .delete("/users/{id}", 2)
      .then()
      .assertThat()
      .log().all()
      .statusCode(204);
  }
}

