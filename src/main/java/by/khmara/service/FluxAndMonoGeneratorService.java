package by.khmara.service;

import by.khmara.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .log();
    }

    public Flux<String> namesFluxToUpperCase() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .map(String::toUpperCase);
    }

    public Flux<String> namesFluxFilterByLength() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .filter(name -> name.length() > 5);
    }

    public Flux<String> namesFluxFlatmapToChars() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .filter(name -> name.equals("Anton"))
                .flatMap(name -> splitName(name));
    }

    public Flux<String> namesFluxFlatmapToCharsAsync() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .flatMap(name -> splitNameAsync(name))
                .log();
    }

    public Flux<String> namesFluxConcatmapToCharsAsync() {
        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .concatMap(name -> splitNameAsync(name))
                .log();
    }

    public Flux<String> namesFluxTransform() {
        Function<Flux<String>, Flux<String>> transformLogic = name -> name.filter(n -> n.equals("Anton"))
                .map(String::toUpperCase);

        return Flux.fromIterable(List.of("Anton", "Maksim", "Veronika"))
                .transform(transformLogic)
                .flatMap(name -> splitName(name))
                .defaultIfEmpty("Default")
                .log();
    }

    public Flux<String> fluxConcat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux)
                .log();
    }

    public Flux<String> fluxZip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first+second).log();
    }

    public Flux<String> fluxWithException() {
        return Flux.just("A","B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception orrurred")))
                .concatWith(Flux.just("D", "E", "F"));
    }

    public Flux<String> fluxOnErrorReturn() {
        return Flux.just("A","B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception orrurred")))
                .onErrorReturn("D");
    }

    public Flux<String> fluxOnErrorResume(Exception e) {
        var recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A","B", "C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("{} occurred in fluxOnErrorResume()", ex);
                    return recoveryFlux;
                }).log();
    }

    public Flux<String> fluxOnErrorContinue() {

        return Flux.just("A", "B", "C")
                .map(letter -> {
                    if(letter.equals("B"))
                        throw new IllegalStateException();
                    else
                        return letter;
                })
                .onErrorContinue((ex, letter) -> {
                    log.error("Exception is ", ex);
                    log.info("Letter with exception: {}", letter);
                }).log();
    }

    public Flux<String> fluxOnErrorMap() {

        return Flux.just("A", "B", "C")
                .map(letter -> {
                    if(letter.equals("B"))
                        throw new IllegalStateException();
                    else
                        return letter;
                })
                .onErrorMap((ex) -> {
                    log.error("Exception is ", ex);
                    return new ReactorException(ex.getMessage(), ex);
                }).log();
    }

    public Flux<String> fluxDoOnError() {
        return Flux.just("A","B","C")
                .concatWith(Flux.error(new IllegalStateException()))
                .doOnError(ex -> {
                    log.error("Exception is ", ex);
                })
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Anton");
    }

    public Mono<List<String>> nameMonoFlatMap() {
        return Mono.just("Anton")
                .flatMap(this::splitNameMono)
                .log();
    }

    public Flux<String> nameMonoFlatMapMany() {
        return Mono.just("Anton")
                .flatMapMany(this::splitName)
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name: " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name: " + name);
                });
    }

    private Flux<String> splitName(String name) {
        return Flux.fromArray(name.split(""));
    }

    private Flux<String> splitNameAsync(String name) {
        var random = new Random().nextInt(1000);

        return Flux.fromArray(name.split(""))
                .delayElements(Duration.ofMillis(random));
    }

    private Mono<List<String>> splitNameMono(String name) {
        return Mono.just(List.of(name.split("")));
    }
}
