package io.github.followsclosely.dots.impl;

import io.github.followsclosely.dots.Box;

import java.util.Arrays;

public class DefaultBox implements Box {
    private int player = 0;
    private DefaultLine[] lines;

    public DefaultBox(DefaultLine... lines) {
        this.lines = lines;
        Arrays.stream(lines).forEach(line -> line.addParent(this));
    }

    @Override
    public DefaultLine getLine(int side){ return lines[side]; }

    @Override
    public int getNumberOfUnclaimedLines(){
        int unclaimed = lines.length;
        for( DefaultLine line : lines){
            if( line.getPlayer() != 0){
                unclaimed--;
            }
        }
        return unclaimed;
    }

    @Override
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}