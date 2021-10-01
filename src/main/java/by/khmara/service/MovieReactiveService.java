package by.khmara.service;

import by.khmara.domain.Movie;
import by.khmara.domain.Revenue;
import by.khmara.domain.Review;
import by.khmara.exception.MovieException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfo;
    private ReviewService review;
    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfo, ReviewService review) {
        this.movieInfo = movieInfo;
        this.review = review;
    }

    public MovieReactiveService(MovieInfoService movieInfo, ReviewService review, RevenueService revenueService) {
        this.movieInfo = movieInfo;
        this.review = review;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies() {
        var moviesInfoFlux = movieInfo.retrieveMoviesFlux();

        return moviesInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviews = review.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviews
                    .map(reviewList -> new Movie(movieInfo, reviewList));
        })
                .onErrorMap(ex -> {
                    log.error("Exception from MovieReactiveService is ", ex);
                    return new MovieException(ex, ex.getMessage());
                });
    }

    public Flux<Movie> getAllMoviesWithRetry() {
        var moviesInfoFlux = movieInfo.retrieveMoviesFlux();

        return moviesInfoFlux.flatMap(movieInfo -> {
                    Mono<List<Review>> reviews = review.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviews
                            .map(reviewList -> new Movie(movieInfo, reviewList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception from MovieReactiveService is ", ex);
                    return new MovieException(ex, ex.getMessage());
                })
                .retry(3);
    }

    public Mono<Movie> getMovieById(long movieId) {
        var movieInfoMono = movieInfo.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = review.retrieveReviewsFlux(movieId)
                .collectList();

        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews));
    }

    public Mono<Movie> getMovieByIdWithRevenue(long movieId) {
        var movieInfoMono = movieInfo.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = review.retrieveReviewsFlux(movieId)
                .collectList();
        var revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews))
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                });
    }
}
