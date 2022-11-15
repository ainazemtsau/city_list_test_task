package com.example.citylist.handler;

import com.example.citylist.AbstractIntegrationTest;
import com.example.citylist.model.City;
import com.example.citylist.repositories.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CityHandlerTest extends AbstractIntegrationTest {
    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("When request has send query params page=1&size=10 should return 10 cities")
    public void testPaginationWithFirstParams() {
        City firstCity = new City();
        firstCity.setId(1L);
        firstCity.setName("Tokyo");
        firstCity.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg");

        City wrongCity = new City();
        wrongCity.setId(11L);
        wrongCity.setName("Beijing");
        wrongCity.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Tiananmen_Gate.jpg/500px-Tiananmen_Gate.jpg");

        testNumberOfCitiesInResponse("/v1/city?page=1&size=10", 10)
                .doesNotContain(wrongCity)
                .contains(firstCity);
    }

    @Test
    @DisplayName("When request has send query params page=2&size=50 should return last 50 cities")
    public void testPaginationWithNotFirsParams() {
        String url = "/v1/city?page=2&size=50";
        int expectSize = 50;
        City lastCity = new City();
        lastCity.setId(100L);
        lastCity.setName("Singapore");
        lastCity.setPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Flag_of_Singapore.svg/500px-Flag_of_Singapore.svg.png");
        testNumberOfCitiesInResponse(url, expectSize)
                .contains(lastCity);
    }
    @Test
    @DisplayName("When request has send query params page=3&size=50 should return 0 cities")
    public void testPaginationWithNonExpectHighParams() {
        String url = "/v1/city?page=3&size=50";
        int expectSize = 0;
        testNumberOfCitiesInResponse(url, expectSize);
    }

    @Test
    @DisplayName("When request has send query params page=0&size=50 should return first 50 cities")
    public void testPaginationWith0Page() {
        String url = "/v1/city?page=0&size=50";
        int expectSize = 50;
        testNumberOfCitiesInResponse(url, expectSize);
    }

    @Test
    @DisplayName("When send request to /city/number should return number of all city in database")
    public void testTotalNumber() {
        String url = "/v1/city/number";
        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(100);
    }


    @Test
    @DisplayName("When send request to update city should update in database by id")
    @WithMockUser(username="admin",roles={"ALLOW_EDIT"})
    public void testUpdateCity() {

        var url = "/v1/city";
        var testPhotoUrl = "TestPhotoUrl";
        var testCityName = "TestCityName";
        var cityTestId = 3L;
        var testCityUpdate = new City();
        testCityUpdate.setId(cityTestId);
        testCityUpdate.setPhoto(testPhotoUrl);
        testCityUpdate.setName(testCityName);

        webTestClient.put()
                .uri(url)
                .bodyValue(testCityUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        var city = cityRepository.findById(cityTestId).block();

        assertThat(city).isNotNull();
        assertThat(city.getPhoto()).isEqualTo(testPhotoUrl);
        assertThat(city.getName()).isEqualTo(testCityName);
    }

    @Test
    @DisplayName("When send request to update city with empty id must return bad request error")
    @WithMockUser(username="admin",roles={"ALLOW_EDIT"})
    public void testUpdateCityWithEmptyCityName() {

        var url = "/v1/city";
        var testPhotoUrl = "TestPhotoUrl";
        var testCityName = "";
        var cityTestId = 3L;
        var testCityUpdate = new City();
        testCityUpdate.setId(cityTestId);
        testCityUpdate.setPhoto(testPhotoUrl);
        testCityUpdate.setName(testCityName);

        webTestClient.put()
                .uri(url)
                .bodyValue(testCityUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testSearchCity() {

        var url = "/v1/city?name=sh";

        webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(City.class)
                .hasSize(7);
    }


    private WebTestClient.ListBodySpec<City> testNumberOfCitiesInResponse(String url, int expectSize) {
        return webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(City.class)
                .hasSize(expectSize);
    }
}
