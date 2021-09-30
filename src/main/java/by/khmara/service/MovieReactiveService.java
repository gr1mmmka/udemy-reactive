package by.khmara.service;

import by.khmara.domain.Movie;
import by.khmara.domain.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class MovieReactiveService {

    private MovieInfoService movieInfo;
    private ReviewService review;

    public MovieReactiveService(MovieInfoService movieInfo, ReviewService review) {
        this.movieInfo = movieInfo;
        this.review = review;
    }

    public Flux<Movie> getAllMovies() {
        var moviesInfoFlux = movieInfo.retrieveMoviesFlux();

        return moviesInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviews = review.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviews
                    .map(reviewList -> new Movie(movieInfo, reviewList));
        });
    }

    public Mono<Movie> getMovieById(long movieId) {
        var movieInfoMono = movieInfo.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = review.retrieveReviewsFlux(movieId)
                .collectList();

        return movieInfoMono.zipWith(reviewsFlux, (movieInfo, reviews) -> new Movie(movieInfo, reviews));
    }
}
