package io.github.followsclosely.dots.ai;

import io.github.followsclosely.dots.*;
import io.github.followsclosely.dots.impl.DefaultBoard;

import java.util.Random;

public class DummyAI implements ArtificialIntelligence {

    private final int color;
    private final Random random = new Random();

    public DummyAI(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Coordinate yourTurn(Board board) {

        //Select a random place on the board
        int x = random.nextInt(board.getWidth());
        int y = random.nextInt(board.getHeight());

        //Keep adding to the spot until a free spot is found.
        for (int i = 0, width = board.getWidth(); i < width; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height; j++, y = (y + 1) % height) {
                Box box = board.getBox(x, y);
                if (box.getNumberOfUnclaimedLines() > 0){
                    for(int side=0; side<4; side++) {
                        if( box.getLine(side).getPlayer() == 0 ){
                            return new Coordinate(x, y, side);
                        }
                    }
                }
            }
        }

        return null;
    }
}