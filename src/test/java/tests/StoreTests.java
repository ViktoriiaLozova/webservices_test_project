package tests;

import endpoints.PetStorePetEndpoint;
import endpoints.StoreEndPoint;
import io.restassured.response.Response;
import models.Order;
import models.Pet;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(SerenityRunner.class)
public class StoreTests {
    @Steps
    public StoreEndPoint storeEndPoint;
    private Order order = new Order(11, 1, new Date(), "available", true);

    @Test
    @Title("verification of post request")
    public void createOrder() {

        //When
        Response orderResponse = storeEndPoint.placeOrder(order);

        //Then
        long orderId = orderResponse.body().as(Order.class).getId();
        Order orderFromService = storeEndPoint.getOrderById(String.valueOf(orderId)).as(Order.class);

        Assert.assertEquals(order.getQuantity(), orderFromService.getQuantity());
        Assert.assertEquals(order.getStatus(), orderFromService.getStatus());
    }


    @Test
    @Title("verification of get request")
    public void readPet() {
        //Given
        Pet barsik = Pet.createBarsik();
        Response petResponse = petStoreEndpoint.createPet(barsik);
        barsik.setStatus("sold");

        //When
        Pet createdPetFromService = petStoreEndpoint.updatePet(barsik).body().as(Pet.class);
        //Then

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(createdPetFromService.getName()).as("Name").isEqualTo(barsik.getName());
        assertions.assertThat(createdPetFromService.getStatus()).as("Name").isEqualTo(barsik.getStatus());
        assertions.assertAll();
    }

    @Test
    @Title("verification of delete request")
    public void deletePet() {
        //Given
        Pet barsik = Pet.createBarsik();
        Response petResponse = petStoreEndpoint.createPet(barsik);
        Pet createdPetFromService = petStoreEndpoint.updatePet(barsik).body().as(Pet.class);

        //When
        petStoreEndpoint.deleteById(createdPetFromService.getId());
        //Then

        Response petById = petStoreEndpoint.getPetById(String.valueOf(createdPetFromService.getId()));
        Assertions.assertThat(petById.getStatusCode()).isEqualTo(404);
    }

}
