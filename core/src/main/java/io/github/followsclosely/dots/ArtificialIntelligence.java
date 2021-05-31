package io.github.followsclosely.dots;

public interface ArtificialIntelligence {

    /**
     * Gets the shape/color that the AI is playing for.
     *
     * @return color of the AI player
     */
    int getColor();

    /**
     * This method is called by the Engine when it is "your" turn to play.
     * It should return the column to drop the piece down.
     *
     * @param board The current state of the game.
     * @return The column (x) to drop the piece.
     */
    Coordinate yourTurn(Board board);
}