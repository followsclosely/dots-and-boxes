package io.github.followsclosely;

import io.github.followsclosely.dots.*;
import io.github.followsclosely.dots.ai.DummyAI;

import java.util.Random;

public class YourCustomAI implements ArtificialIntelligence {

    private final int color;
    private final Random random = new Random();

    public YourCustomAI(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Coordinate yourTurn(Board board) {
        //Your logic replaces this line below.
        return new DummyAI(color).yourTurn(board);
    }
}