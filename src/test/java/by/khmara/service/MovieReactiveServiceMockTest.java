package by.khmara.service;

import by.khmara.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {
    @Mock
    private MovieInfoService movieInfoService;
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private MovieReactiveService movieReactiveService;


    @Test
    void getAllMovies() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        var fluxMovies = movieReactiveService.getAllMovies();

        StepVerifier.create(fluxMovies)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMoviesWithException() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException("Exception from review service"));

        var fluxMovies = movieReactiveService.getAllMovies();

        StepVerifier.create(fluxMovies)
                .expectError(MovieException.class)
                .verify();
    }

    @Test
    void getAllMoviesWithExceptionRetry() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException("Exception from review service"));

        var fluxMovies = movieReactiveService.getAllMoviesWithRetry();

        StepVerifier.create(fluxMovies)
                .expectError(MovieException.class)
                .verify();
    }
}