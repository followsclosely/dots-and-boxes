package io.github.followsclosely.dots.impl;

import io.github.followsclosely.dots.Line;

import java.util.ArrayList;
import java.util.List;

public class DefaultLine implements Line {
    private int player;
    private List<DefaultBox> parents = new ArrayList<>(2);

    public DefaultLine(){

    }

    public DefaultLine(Line line){
        this.player = line.getPlayer();
    }

    @Override
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void addParent(DefaultBox box){
        parents.add(box);
    }

    public List<DefaultBox> getParents() { return parents; }
}