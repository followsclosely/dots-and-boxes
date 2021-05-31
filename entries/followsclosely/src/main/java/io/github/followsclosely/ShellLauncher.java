package io.github.followsclosely;

import io.github.followsclosely.dots.Simulation;
import io.github.followsclosely.dots.ai.DummyAI;
import io.github.followsclosely.dots.impl.DefaultBoard;

public class ShellLauncher {
    public static void main(String[] args) {
        new Simulation()
                .board(new DefaultBoard(10,10))
                .number(1000)
                .addArtificialIntelligence(new StinkAI(1))
                .addArtificialIntelligence(new DummyAI(2))
                .run()
                .printSummary();
    }
}
