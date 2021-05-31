package io.github.followsclosely.dots;

/**
 * A board holds the state of the game in a two dimensional array.
 * A zero '0' represents an empty spot, any other number represents
 * a players piece.
 */
public interface Board {

    /**
     * A getter for width
     *
     * @return The number of pieces across the board.
     */
    int getWidth();

    /**
     * A getter for height
     *
     * @return The number of pieces high that make up the board.
     */
    int getHeight();

    /**
     * Gets the Box at [x,y]
     *
     * @param x The X coordinate
     * @param y The y coordinate
     * @return The int value of the pieces at [x,y]
     */
    Box getBox(int x, int y);
}