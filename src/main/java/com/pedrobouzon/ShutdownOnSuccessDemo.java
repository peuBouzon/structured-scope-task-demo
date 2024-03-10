package com.pedrobouzon;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ShutdownOnSuccessDemo {
    private static final Random random = new Random();
    public static void main(String[] args) {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
          scope.fork(ShutdownOnSuccessDemo::scheduleSoccerWithFriends);
          scope.fork(ShutdownOnSuccessDemo::scheduleMeetWithGirlfriend);

          scope.join();

          System.out.println("What am I gonna do? = " + scope.result());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static String scheduleSoccerWithFriends() {
        sleepForBetweenOneAndTwoSeconds();
        return "Play soccer with friends";
    }

    private static String scheduleMeetWithGirlfriend() {
        sleepForBetweenOneAndTwoSeconds();
        return "Meet with my girlfriend";
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
}