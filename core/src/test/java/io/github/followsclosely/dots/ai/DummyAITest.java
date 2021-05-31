package io.github.followsclosely.dots.ai;

import io.github.followsclosely.dots.ArtificialIntelligence;

public class DummyAITest extends ArtificialIntelligenceTester {
    @Override
    public ArtificialIntelligence instance(int shape, int opponent) {
        return new DummyAI(shape);
    }
}