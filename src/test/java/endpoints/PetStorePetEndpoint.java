package endpoints;

import config.Config;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Pet;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.http.HttpHeaders;


public class PetStorePetEndpoint {

    @Step("get Pet by Id")
    public Response getPetById(String id){
        return given()
                .pathParam("petId", id)
                .when()
                .get(Config.PET_BY_ID)
                .then()
                .extract().response();
    }

    @Step("get Pet by Status")
    public Response getPetByStatus(String status){
        return given()
                .queryParam("status", status)
                .when()
                .get(Config.FIND_BY_STATUS)
                .then().extract().response();
    }


    @Step("create Pet")
    public Response createPet(Pet pet){
        return given()
                .body(pet)
                .when()
                .post(Config.CREATE_PET)
                .then().extract().response();
    }

    @Step("update Pet")
    public Response updatePet(Pet pet){
        return given()
                .body(pet)
                .when()
                .put(Config.CREATE_PET)
                .then().extract().response();
    }

    @Step("delete Pet by Id {}")
    public Response deleteById(long id){
        return given()
                .when()
                .delete(Config.PET_BY_ID, id)
                .then().extract().response();
    }

    private RequestSpecification given() {
        return SerenityRest.given()
                .baseUri(Config.PETSTORE_BASE_URL)
                .header(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
