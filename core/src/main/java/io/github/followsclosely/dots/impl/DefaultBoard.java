package io.github.followsclosely.dots.impl;

import io.github.followsclosely.dots.Board;
import io.github.followsclosely.dots.Box;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultBoard implements Board {

    protected int width, height;
    protected final DefaultBox[][] state;

    public DefaultBoard(Board board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.state = new DefaultBox[this.width][this.height];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                Box box = board.getBox(x, y);
                //Boxes share two lines, index 0 and index 4.
                this.state[x][y] = new DefaultBox(
                        (y>0) ? this.state[x][y-1].getLine(2) : new DefaultLine(box.getLine(0)),
                        new DefaultLine(box.getLine(1)),
                        new DefaultLine(box.getLine(2)),
                        (x>0) ? this.state[x-1][y].getLine(1) : new DefaultLine(box.getLine(3))
                );
            }
        }
    }

    public DefaultBoard(int width, int height) {
        this.state = new DefaultBox[this.width = width][this.height = height];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                //Boxes share two lines, index 0 and index 4.
                this.state[x][y] = new DefaultBox(
                        (y>0) ? this.state[x][y-1].getLine(2) : new DefaultLine(),
                        new DefaultLine(),
                        new DefaultLine(),
                        (x>0) ? this.state[x-1][y].getLine(1) : new DefaultLine()
                );
            }
        }

    }

    @Override
    public DefaultBox getBox(int x, int y) {
        return state[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public String toString() {
        Board board = this;
        StringBuilder b = new StringBuilder("----------------\nboard = ");
        for (int x = 0; x < board.getWidth(); x++) {
            b.append(x).append(" ");
        }
        b.append("\n");

        for (int y = 0; y < board.getHeight(); y++) {
            b.append("     ").append(y).append(": ");
            for (int x = 0; x < board.getWidth(); x++) {
                b.append(board.getBox(x, y).getPlayer()).append(" ");
            }
            b.append("\n");
        }
        return b.toString();
    }

    public Map<Integer, AtomicInteger> getCounts(){
        Map<Integer, AtomicInteger> counts = new HashMap<>();
        for (int x = 0, width = getWidth(); x < width; x++) {
            for (int y = 0, height = getHeight(); y < height; y++) {
                Integer player = state[x][y].getPlayer();
                counts.computeIfAbsent(player, integer -> new AtomicInteger(0)).getAndIncrement();
            }
        }

        return counts;
    }
}
