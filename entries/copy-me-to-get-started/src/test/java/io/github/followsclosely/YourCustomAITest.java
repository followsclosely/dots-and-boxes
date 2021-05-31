package io.github.followsclosely;

import io.github.followsclosely.dots.ArtificialIntelligence;
import io.github.followsclosely.dots.ai.ArtificialIntelligenceTester;
import io.github.followsclosely.dots.ai.DummyAI;


public class YourCustomAITest extends ArtificialIntelligenceTester {

    @Override
    public ArtificialIntelligence instance(int shape, int opponent) {
        DummyAI ai = new DummyAI(shape);
        ai.initialize(opponent);
        return ai;
    }
}