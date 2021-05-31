package io.github.followsclosely.dots;


public interface Box {
    int getPlayer();
    Line getLine(int side);
    int getNumberOfUnclaimedLines();
}
