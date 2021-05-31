package io.github.followsclosely;

import io.github.followsclosely.dots.*;
import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DotsAndBoxesUtils;

import java.util.List;
import java.util.Random;

public class StinkAI implements ArtificialIntelligence {

    private int color;
    private final Random random = new Random();

    public StinkAI(int color) {
        this.color = color;
    }

    @Override public int getColor() {
        return color;
    }

    @Override
    public Coordinate yourTurn(Board b) {
        final DefaultBoard board = new DefaultBoard(b);

        //Select a random place on the board
        int x = random.nextInt(board.getWidth());
        int y = random.nextInt(board.getHeight());

        Coordinate nextBest = null;

        //Close a box if we can.
        for (int i = 0, width = board.getWidth(); i < width; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height; j++, y = (y + 1) % height) {
                Box box = board.getBox(x, y);
                if (box.getNumberOfUnclaimedLines() == 1){
                    for(int side=0; side<4; side++) {
                        if( box.getLine(side).getPlayer() == 0 ){
                            return new Coordinate(x, y, side);
                        }
                    }
                }
            }
        }

        List<Coordinate> safeSpots = DotsAndBoxesUtils.getSafeSpotsToPlay(board, 3);
        if( !safeSpots.isEmpty() ){
            return safeSpots.get(0);
        }

        return DotsAndBoxesUtils.getAnySpotToPlay(board).orElse(null);
    }
}
