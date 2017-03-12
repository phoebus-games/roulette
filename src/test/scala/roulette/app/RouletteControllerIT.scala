package roulette.app

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor, urlEqualTo}
import games.app.IntegrationTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.{equalTo, notNullValue}
import org.junit.{Before, Test}
import roulette.App

class RouletteControllerIT extends IntegrationTest(new App) {

  @Before override def before(): Unit = {
    super.before()

    RestAssured.requestSpecification = spec
      .setBasePath("/games/roulette")
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .build()

    given()
      .when()
      .delete()
      .`then`()
      .statusCode(204)
  }

  //noinspection AccessorLikeMethodIsUnit
  @Test def getRoulette(): Unit = {
    given()
      .when()
      .get()
      .`then`()
      .statusCode(200)
      .body("pocket", equalTo(0))
  }

  @Test def addNumberBet(): Unit = {
    given()
      .body("{\"amount\": 10, \"number\": 19}")
      .when()
      .post("/bets/number")
      .`then`()
      .statusCode(201)
      .body("balance", notNullValue())
    given()
      .when()
      .get()
      .`then`()
      .statusCode(200)
      .body("bets[0].type", equalTo("number"))
      .body("bets[0].amount", equalTo(10))
      .body("bets[0].number", equalTo(19))
  }

  @Test def addRedBet(): Unit = {
    given()
      .body("{\"amount\": 10}")
      .when()
      .post("/bets/red")
      .`then`()
      .statusCode(201)
      .body("balance", notNullValue())
    given()
      .when()
      .get()
      .`then`()
      .statusCode(200)
      .body("bets[0].type", equalTo("red"))
      .body("bets[0].amount", equalTo(10))
  }

  @Test def addBlackBet(): Unit = {
    given()
      .body("{\"amount\": 10}")
      .when()
      .post("/bets/black")
      .`then`()
      .statusCode(201)
      .body("balance", notNullValue())
    given()
      .when()
      .get()
      .`then`()
      .statusCode(200)
      .body("bets[0].type", equalTo("black"))
      .body("bets[0].amount", equalTo(10))
  }

  @Test def spin(): Unit = {
    given()
      .body("{\"amount\": 10}")
      .when()
      .post("/bets/black")
      .`then`()
      .statusCode(201)
    given()
      .when()
      .post("/spins")
      .`then`()
      .statusCode(201)
      .body("balance", notNullValue())
      .body("pocket", notNullValue())
  }

  @Test def badAddBet(): Unit = {
    given()
      .body("{\"amount\": -1}")
      .when()
      .post("/bets/black")
      .`then`()
      .statusCode(400)
      .body("message", equalTo("requirement failed: amount must be greater than zero"))
  }

  @Test def badSpin(): Unit = {
    given()
      .when()
      .post("/spins")
      .`then`()
      .statusCode(400)
      .body("message", equalTo("requirement failed: no bets on table"))
  }

  @Test def notEnoughFunds(): Unit = {

    stubFor(post(urlEqualTo("/transactions"))
      .willReturn(aResponse()
        .withStatus(403)
        .withHeader("Content-Type", "application/json")
        .withBody("{\"message\": \"not enough funds\"}")))

    given()
      .body("{\"amount\": 10}")
      .when()
      .post("/bets/black")
      .`then`()
      .statusCode(403)
      .body("message", equalTo("not enough funds"))
  }
}
