package by.khmara.service;

import by.khmara.domain.Movie;
import by.khmara.domain.Revenue;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {

    private ReviewService reviewService = new ReviewService();
    private MovieInfoService movieInfoService = new MovieInfoService();
    private RevenueService revenueService = new RevenueService();

    private MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);
    private MovieReactiveService movieReactiveServiceWithRevenue = new MovieReactiveService(movieInfoService, reviewService, revenueService);

    @Test
    void testGetAllMovies() {
        Flux<Movie> movies = movieReactiveService.getAllMovies();

        StepVerifier.create(movies)
                .assertNext(movie ->
                        assertEquals("Batman Begins", movie.getMovie().getName()))
                .assertNext(movie ->
                        assertEquals("The Dark Knight", movie.getMovie().getName()))
                .assertNext(movie ->
                        assertEquals("Dark Knight Rises", movie.getMovie().getName()))
                .verifyComplete();
    }

    @Test
    void testGetMovieById() {
        Mono<Movie> movie = movieReactiveService.getMovieById(100L);

        StepVerifier.create(movie)
                .assertNext(m -> {
                    assertEquals("Batman Begins", m.getMovie().getName());
                    assertEquals(2005, m.getMovie().getYear());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdWithRevenue() {
            Mono<Movie> movie = movieReactiveServiceWithRevenue.getMovieByIdWithRevenue(100L);

            StepVerifier.create(movie)
                    .assertNext(m ->
                            assertNotNull(m.getRevenue()))
                    .verifyComplete();

    }

}