package by.khmara.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
