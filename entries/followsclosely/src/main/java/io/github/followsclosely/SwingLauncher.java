package io.github.followsclosely;

import io.github.followsclosely.dots.SwingSupport;
import io.github.followsclosely.dots.impl.DefaultBoard;

public class SwingLauncher {
    public static void main(String[] args) {

        DefaultBoard board = new DefaultBoard(4, 4);

        new SwingSupport()
                .setBoard(board)
                .setArtificialIntelligence(new StinkAI(SwingSupport.COMPUTER_COLOR))
                .run();
    }
}