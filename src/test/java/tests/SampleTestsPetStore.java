package tests;

import com.google.common.collect.ImmutableList;
import endpoints.PetStorePetEndpoint;
import io.restassured.response.Response;
import models.Category;
import models.Pet;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;

@RunWith(SerenityRunner.class)
public class SampleTestsPetStore {

    @Steps
    private PetStorePetEndpoint petStorePetEndpoint;

    @Before
    public void cleanup() {
        petStorePetEndpoint
                .getPetByStatus("available")
                .getBody()
                .jsonPath()
                .getList("findAll {item -> item.name == 'Barsik'}", Pet.class)
                .stream()
                .forEach(pet -> petStorePetEndpoint.deleteById(pet.getId()));

    }

    @Test
    public void verifyStatusCode() {
        petStorePetEndpoint.getPetByStatus("available")
                .then()
                .statusCode(200);
    }

    @Test
    public void verifyBody() {
        petStorePetEndpoint.getPetByStatus("available")
                .then()
                .assertThat()
                .body(is(not(isEmptyOrNullString())));
    }

    @Test
    public void verifyNotExistingPetReturn404() {
        petStorePetEndpoint
                .getPetById("asdfasdf")
                .then()
                .statusCode(404);
    }

    @Test
    public void verifyPetCanBeCreated() {
        Category category = new Category();
        category.setName("Cats");
        category.setId(123123);

        Pet cat = new Pet();
        cat.setName("Barsik");
        cat.setCategory(category);
        cat.setPhotoUrls(ImmutableList.of("someUrl"));
        cat.setStatus("available");

        petStorePetEndpoint
                .createPet(cat)
                .then()
                .statusCode(200);
    }

    @Test
    public void verifyBarsikHasIdAfterCreation() {
        Pet barsik = Pet.createBarsik();

        Response petResponse = petStorePetEndpoint
                .createPet(barsik);

        Pet petFromService = petResponse.body().as(Pet.class);
        Assert.assertNotNull(petFromService.getId());
    }
}
