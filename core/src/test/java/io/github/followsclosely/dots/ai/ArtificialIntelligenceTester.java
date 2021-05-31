package io.github.followsclosely.dots.ai;

import io.github.followsclosely.dots.ArtificialIntelligence;
import io.github.followsclosely.dots.Coordinate;
import io.github.followsclosely.dots.impl.DefaultBoard;
import org.junit.Assert;
import org.junit.Test;

public abstract class ArtificialIntelligenceTester {

    public abstract ArtificialIntelligence instance(int shape, int opponent);

    @Test
    public void testPlayOnAnEmptyBoard() {
        DefaultBoard board = new DefaultBoard(2, 2);

        Coordinate turn = instance(2, 1).yourTurn(board);
        Assert.assertNotNull(turn);
        Assert.assertEquals("The spot on the board was expected to be empty (0).", 0, board.getBox(turn.getX(), turn.getY()).getLine(turn.getSide()).getPlayer());
    }
}
