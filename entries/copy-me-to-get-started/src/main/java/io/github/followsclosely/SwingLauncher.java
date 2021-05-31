package io.github.followsclosely;

import io.github.followsclosely.dots.SwingSupport;

public class SwingLauncher {
    public static void main(String[] args) {
        new SwingSupport()
                .setArtificialIntelligence(new YourCustomAI(SwingSupport.COMPUTER_COLOR))
                .run();
    }
}