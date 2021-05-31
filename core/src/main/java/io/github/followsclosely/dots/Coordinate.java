package io.github.followsclosely.dots;

import java.util.Objects;

public class Coordinate {
    int x, y, side;

    public Coordinate(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSide(){ return side; }

    @Override
    public String toString() {
        return String.format("[%d,%d], %d", x, y, side);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, side);
    }
}