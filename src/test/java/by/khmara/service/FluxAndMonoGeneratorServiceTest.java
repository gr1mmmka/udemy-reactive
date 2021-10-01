package by.khmara.service;

import by.khmara.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void testNamesFlux() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
                //.expectNext("Anton", "Maksim", "Veronika")
                //.expectNextCount(3)
                .expectNext("Anton")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testNameMono() {
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(nameMono)
                .expectNext("Anton")
                .verifyComplete();
    }

    @Test
    void testNamesFluxToUpperCase() {
        var namesFluxToUpperCase = fluxAndMonoGeneratorService.namesFluxToUpperCase();

        StepVerifier.create(namesFluxToUpperCase)
                .expectNext("ANTON", "MAKSIM", "VERONIKA");
    }

    @Test
    void testNamesFluxFilterByLength() {
        var namesFluxFilterByLength = fluxAndMonoGeneratorService.namesFluxFilterByLength();

        StepVerifier.create(namesFluxFilterByLength)
                .expectNextCount(1);
    }

    @Test
    void testNameFluxFlatmapToChars() {
        var namesFluxFlatmapToChars = fluxAndMonoGeneratorService.namesFluxFlatmapToChars();

        StepVerifier.create(namesFluxFlatmapToChars)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void testNameFluxFlatmapToCharsAsync() {
        var namesFluxFlatmapToCharsAsync = fluxAndMonoGeneratorService.namesFluxFlatmapToCharsAsync();

        StepVerifier.create(namesFluxFlatmapToCharsAsync)
                .expectNextCount(19)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatmapToCharsAsync() {
        var namesFluxConcatmapToCharsAsync = fluxAndMonoGeneratorService.namesFluxConcatmapToCharsAsync();

        StepVerifier.create(namesFluxConcatmapToCharsAsync)
                .expectNextCount(19)
                .verifyComplete();
    }

    @Test
    void nameMonoFlatMap() {
        var nameMonoFlatMap = fluxAndMonoGeneratorService.nameMonoFlatMap();

        StepVerifier.create(nameMonoFlatMap)
                .expectNext(List.of("A", "n", "t", "o", "n"))
                .verifyComplete();
    }

    @Test
    void nameMonoFlatMapMany() {
        var nameMonoFlatMapMany = fluxAndMonoGeneratorService.nameMonoFlatMapMany();

        StepVerifier.create(nameMonoFlatMapMany)
                .expectNext("A", "n", "t", "o", "n")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        var namesFluxTransform = fluxAndMonoGeneratorService.namesFluxTransform();

        StepVerifier.create(namesFluxTransform)
                .expectNext("A","N", "T", "O", "N")
                .verifyComplete();
    }

    @Test
    void fluxConcat() {
        var fluxConcat = fluxAndMonoGeneratorService.fluxConcat();

        StepVerifier.create(fluxConcat)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void fluxZip() {
        var fluxZip = fluxAndMonoGeneratorService.fluxZip();

        StepVerifier.create(fluxZip)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void fluxWithException() {
        var fluxWithException = fluxAndMonoGeneratorService.fluxWithException();

        StepVerifier.create(fluxWithException)
                .expectNext("A","B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxOnErrorReturn() {
        var fluxOnErrorReturn = fluxAndMonoGeneratorService.fluxOnErrorReturn();

        StepVerifier.create(fluxOnErrorReturn)
                .expectNext("A","B", "C", "D")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorResume() {
        var exception = new IllegalStateException();
        var fluxOnErrorResume = fluxAndMonoGeneratorService.fluxOnErrorResume(exception);

        StepVerifier.create(fluxOnErrorResume)
                .expectNext("A","B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void  fluxOnErrorContinue() {
        var fluxOnErrorContinue = fluxAndMonoGeneratorService.fluxOnErrorContinue();

        StepVerifier.create(fluxOnErrorContinue)
                .expectNext("A", "C")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorMap() {
        var fluxOnErrorMap = fluxAndMonoGeneratorService.fluxOnErrorMap();

        StepVerifier.create(fluxOnErrorMap)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void fluxDoOnError() {
        var fluxDoOnError = fluxAndMonoGeneratorService.fluxDoOnError();

        StepVerifier.create(fluxDoOnError)
                .expectNext("A","B","C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}