package io.github.followsclosely;

import io.github.followsclosely.dots.Simulation;
import io.github.followsclosely.dots.ai.DummyAI;

public class ShellLauncher {
    public static void main(String[] args) {
        new Simulation()
                .number(100)
                .addArtificialIntelligence(new DummyAI(1))
                .addArtificialIntelligence(new YourCustomAI(2))
                .run()
                .printSummary();
    }
}
