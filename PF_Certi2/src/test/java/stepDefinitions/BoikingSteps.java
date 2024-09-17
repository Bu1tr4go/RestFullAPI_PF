package stepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Booking;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import utils.Request;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class BoikingSteps {

    Response apiResponse;

    @Given("Initiate request to get booking IDs")
    public void initiateGetBookingIdsRequest() {
        apiResponse = Request.get("/booking");
    }

    @Then("Validate the response status code {int}")
    public void validateResponseStatusCode(int statusCode) {
        apiResponse.then().statusCode(statusCode);
    }

    @And("Ensure the response size is not {int}")
    public void ensureResponseSizeIsNotZero(int size) {
        apiResponse.then().assertThat().body("size()", not(0));
    }

    @Given("Fetch booking by ID {int}")
    public void fetchBookingById(int id) {
        this.apiResponse = Request.getById("/booking/{id}", String.valueOf(id));
    }

    @And("Validate booking details are not empty")
    public void validateBookingDetailsAreNotEmpty() {
        apiResponse.then().log().body();
        apiResponse.then().assertThat()
                .body("firstname", Matchers.not(Matchers.isEmptyOrNullString()))
                .body("lastname", Matchers.not(Matchers.isEmptyOrNullString()))
                .body("totalprice", Matchers.notNullValue())
                .body("depositpaid", Matchers.notNullValue())
                .body("bookingdates.checkin", Matchers.not(Matchers.isEmptyOrNullString()))
                .body("bookingdates.checkout", Matchers.not(Matchers.isEmptyOrNullString()));
    }

    @Given("Fetch booking by string ID {string}")
    public void fetchBookingByStringId(String id) {
        apiResponse = Request.getById("/booking/{id}", id);
    }

    @Given("Initiate request to create booking")
    public void initiateCreateBookingRequest(io.cucumber.datatable.DataTable bookingData) throws JsonProcessingException {
        List<String> attributes = bookingData.transpose().asList(String.class);
        Booking booking = new Booking();
        booking.setFirstname(attributes.get(0));
        booking.setLastname(attributes.get(1));
        booking.setTotalprice(Integer.parseInt(attributes.get(2)));
        booking.setDepositpaid(Boolean.parseBoolean(attributes.get(3)));

        Booking.BookingDates dates = new Booking.BookingDates();
        dates.setCheckin(attributes.get(4));
        dates.setCheckout(attributes.get(5));
        booking.setBookingdates(dates);
        booking.setAdditionalneeds(attributes.get(6));

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        apiResponse = Request.post("/booking", payload);
    }

    @Given("Initiate request to create booking with empty dates")
    public void initiateEmptyBookingRequest() throws JsonProcessingException {
        Booking booking = new Booking();
        booking.setFirstname("Daniel");
        booking.setLastname("Torrico");
        booking.setTotalprice(1234);
        booking.setDepositpaid(false);

        booking.setAdditionalneeds("");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        apiResponse = Request.post("/booking", payload);
    }
}
