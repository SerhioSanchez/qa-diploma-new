package ru.netology.data;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.card.CardsBank;

import static io.restassured.RestAssured.given;


public class Api {
    public static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static String buyForm (CardsBank cardsBank) {
        return given()
                .spec(requestSpecification)
                .body(cardsBank)
                .when()
                .post("/api/v1/pay")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().asString();
    }

    public static String creditForm (CardsBank cardsBank) {
        return given()
                .spec(requestSpecification)
                .body(cardsBank)
                .when()
                .post("/api/v1/credit")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().asString();
    }
}