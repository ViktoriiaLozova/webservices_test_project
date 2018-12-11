package endpoints;

import config.Config;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Order;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.http.HttpHeaders;

public class StoreEndPoint {

    @Step("inventory list")
    public Response getStoreInventory() {
        return given()
                .when()
                .get(Config.STORE_INVENTORY)
                .then()
                .extract().response();
    }

    @Step("place order")
    public Response placeOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(Config.CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("get order by id")
    public Response getOrderById(String id) {
        return given()
                .pathParam("orderId", id)
                .when()
                .get(Config.GET_ORDER_BY_ID)
                .then()
                .extract().response();
    }

    @Step("delete purchse by order Id")
    public Response deleteOrderById(long id) {
        return given()
                .when()
                .delete(Config.GET_ORDER_BY_ID, id)
                .then()
                .extract().response();
    }

    private RequestSpecification given() {
        return SerenityRest.given()
                .baseUri(Config.PETSTORE_BASE_URL)
                .header(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
