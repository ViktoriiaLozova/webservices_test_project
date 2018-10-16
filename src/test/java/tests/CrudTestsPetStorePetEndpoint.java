package tests;

import endpoints.PetStorePetEndpoint;
import io.restassured.response.Response;
import models.Pet;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class CrudTestsPetStorePetEndpoint {

    @Steps
    public PetStorePetEndpoint petStoreEndpoint;

    @Before
    public void cleanup() {
        petStoreEndpoint
                .getPetByStatus("available")
                .body()
                .jsonPath().getList("findAll {item -> item.name == 'Barsik'}", Pet.class)
                .stream()
                .forEach(pet -> petStoreEndpoint.deleteById(pet.getId()));
    }

    @Test
    @Title("verification of post request")
    public void createPet() {
        //Given
        Pet barsik = Pet.createBarsik();

        //When
        Response petResponse = petStoreEndpoint.createPet(barsik);
        //Then
        long createdPetId = petResponse.body().as(Pet.class).getId();
        Pet createdPetFromService = petStoreEndpoint.getPetById(String.valueOf(createdPetId)).as(Pet.class);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(createdPetFromService.getName()).as("Name").isEqualTo(barsik.getName());
        assertions.assertThat(createdPetFromService.getStatus()).as("Name").isEqualTo(barsik.getStatus());
        assertions.assertAll();
    }

    @Test
    @Title("verification of put request")
    public void updatePet() {
        //Given
        Pet barsik = Pet.createBarsik();
        Response petResponse = petStoreEndpoint.createPet(barsik);
        long createdPetId = petResponse.body().as(Pet.class).getId();

        //When
        petStoreEndpoint.getPetById(String.valueOf(createdPetId)).as(Pet.class);
        //Then
        Pet createdPetFromService = petStoreEndpoint.getPetById(String.valueOf(createdPetId)).as(Pet.class);

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(createdPetFromService.getName()).as("Name").isEqualTo(barsik.getName());
        assertions.assertThat(createdPetFromService.getStatus()).as("Name").isEqualTo(barsik.getStatus());
        assertions.assertAll();
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
