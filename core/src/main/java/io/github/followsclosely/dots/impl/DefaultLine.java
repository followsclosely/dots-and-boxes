package io.github.followsclosely.dots.impl;

import io.github.followsclosely.dots.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultLine implements Line {
    private int player;
    private Integer score;
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

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultLine that = (DefaultLine) o;
        return player == that.player && Objects.equals(parents, that.parents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, parents);
    }
}