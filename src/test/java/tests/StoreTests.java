package tests;

import endpoints.StoreEndPoint;
import io.restassured.response.Response;
import models.Order;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;

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
        System.out.println(orderId);
    }


    @Test
    @Title("verification of get request")
    public void getOrder() {
        //Given
        for (int i = 1; i < 11; i++) {
            Order order = new Order(i, i * 2, i, new Date(), "available", true);
            storeEndPoint.placeOrder(order);
        }

        for (int i = 1; i < 11; i++) {
            //When
            Order orderFromService = storeEndPoint.getOrderById(String.valueOf(i)).as(Order.class);
            //Then
            Assert.assertTrue(orderFromService.getStatus() != null && !orderFromService.getStatus().isEmpty());
            Assert.assertTrue(orderFromService.getShipDate() != null);

        }
    }

    @Test
    @Title("Order not found")
    public void getOrderException() {
        storeEndPoint
                .getOrderById(String.valueOf(50))
                .then()
                .statusCode(404);
    }

    @Test
    @Title("Invalid ID supplied")
    public void getOrderExceptionInvalidID() {
        storeEndPoint
                .getOrderById("pppfdg")
                .then()
                .statusCode(400);
    }

    @Test
    @Title("For valid response try integer IDs with value >= 1 and <= 10. Other values will generated exceptions")
    public void getValidOrder() {
        Response orderResponse = storeEndPoint.placeOrder(order);
        long orderId = orderResponse.body().as(Order.class).getId();
        storeEndPoint
                .getOrderById(String.valueOf(orderId))
                .then()
                .statusCode(200);
    }

    @Test
    public void verifyOrderStatus() {
        boolean result = storeEndPoint.getStoreInventory().as(Map.class)
                .containsKey("available");
        Assert.assertTrue(result);
    }


    @Test
    @Title("verification of delete request")
    public void deleteOrder() {
        Response orderResponse = storeEndPoint.placeOrder(order);
        long orderId = orderResponse.body().as(Order.class).getId();

        storeEndPoint
                .deleteOrderById(orderId)
                .then()
                .statusCode(200);

        storeEndPoint
                .deleteOrderById(orderId)
                .then()
                .statusCode(404);
    }

}
