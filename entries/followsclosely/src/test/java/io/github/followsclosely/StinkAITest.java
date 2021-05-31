package io.github.followsclosely;

import io.github.followsclosely.dots.ArtificialIntelligence;
import io.github.followsclosely.dots.ai.ArtificialIntelligenceTester;

public class StinkAITest extends ArtificialIntelligenceTester {
    public ArtificialIntelligence instance(int shape, int opponent) {
        return new StinkAI(shape);
    }
}