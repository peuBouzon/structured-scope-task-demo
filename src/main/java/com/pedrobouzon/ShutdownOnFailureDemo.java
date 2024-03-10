package com.pedrobouzon;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ShutdownOnFailureDemo {
    private static final Random random = new Random();
    public static void main(String[] args) {
        scheduleParty();
    }

    private static void scheduleParty() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var band = scope.fork(ShutdownOnFailureDemo::hireBand);
            var buffet = scope.fork(ShutdownOnFailureDemo::hireBuffet);

            scope.join();

            System.out.println("band = " + band.get());
            System.out.println("buffet = " + buffet.get());

        } catch (InterruptedException | IllegalStateException e) {
            System.out.println("Could not schedule the party. Try again later.");
        }
    }
    private static PartyComponent hireBand() {
        sleepForBetweenOneAndTwoSeconds();
        if (random.nextBoolean()) {
            throw new NoBandAvailableException();
        }
        return new Band("David Guetta");
        
    }

    private static PartyComponent hireBuffet() {
        sleepForBetweenOneAndTwoSeconds();
        return new Buffet("Sushi inc");
    }

    private static void sleepForBetweenOneAndTwoSeconds() {
        sleepFor(random.nextInt(1_000, 2_000));
    }

    private static void sleepFor(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public interface PartyComponent {}
    public static class NoBandAvailableException extends RuntimeException {}
    public record Buffet(String name) implements PartyComponent {}
    public record Band(String name) implements PartyComponent {}
}