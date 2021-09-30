package by.khmara.service;

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
}