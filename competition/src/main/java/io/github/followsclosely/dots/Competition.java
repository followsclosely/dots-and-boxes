package io.github.followsclosely.dots;

import io.github.followsclosely.StinkAI;
import io.github.followsclosely.dots.ai.DummyAI;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Competition {

    private List<ArtificialIntelligence> ais = new ArrayList<>();

    public static void main(String[] args) {
        new Competition()
                .add(new DummyAI(1))
                .add(new StinkAI(2))
                .run();
    }

    public Competition add(ArtificialIntelligence ai) {
        ais.add(ai);
        return this;
    }

    public void run() {

        int size = ais.size();
        int numberOfSimulations = 1000000;
        final ExecutorService executorService = Executors.newFixedThreadPool(6);
        Simulation[][] matches = new Simulation[size][size];

        try {

            List<Future<Simulation>> futures = new ArrayList<>();

            for (int x = 0; x < size; x++) {
                ArtificialIntelligence player1 = ais.get(x);
                for (int y = 0; y < size; y++) {
                    ArtificialIntelligence player2 = ais.get(y);

                    System.out.println(player1 + " vs. " + player2);
                    final Simulation match = matches[x][y] = new Simulation()
                            .addArtificialIntelligence(player1)
                            .addArtificialIntelligence(player2)
                            .number(numberOfSimulations);

                    if (x != y) {
                        futures.add(executorService.submit(() -> {
                            match.run();
                            return match;
                        }));
                    }
                }
            }

            for (Future<Simulation> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    System.out.println("Something went wrong. This shouldn't happen.");
                }
            }

            printWithVelocity(matches);
        } finally {
            executorService.shutdown();
        }

    }

    private void printWithVelocity(Simulation[][] matches) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        VelocityContext context = new VelocityContext();
        context.put("matches", matches);

        Template t = velocityEngine.getTemplate("./competition/src/main/java/index.vm");

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        System.out.println(writer);
    }
}