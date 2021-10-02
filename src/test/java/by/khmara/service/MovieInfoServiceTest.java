package by.khmara.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void retrieveMovieInfo_restClient() {
        var allMovies = movieInfoService.retrieveMovieInfo_restClient();

        StepVerifier.create(allMovies)
                .expectNextCount(7)
                .verifyComplete();
    }
}