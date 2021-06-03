package io.github.followsclosely.dots.impl;

import io.github.followsclosely.dots.Board;
import io.github.followsclosely.dots.Box;
import io.github.followsclosely.dots.Coordinate;
import io.github.followsclosely.dots.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class DotsAndBoxesUtils {

    public static Optional<Coordinate> getAnySpotToPlay(final Board board){

        //Select a random place on the board
        //int x = ThreadLocalRandom.current().nextInt(board.getWidth());
        //int y = ThreadLocalRandom.current().nextInt(board.getHeight());
        int x = 0, y = 0;

        //Look for a any valid spot.
        for (int i = 0, width = board.getWidth(); i < width; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height; j++, y = (y + 1) % height) {
                final Box box = board.getBox(x, y);
                for (int side = 0; side < 4; side++) {
                    final Line line = box.getLine(side);
                    if (line.getPlayer() == 0) {
                        return Optional.of(new Coordinate(x, y, side));
                    }
                }
            }
        }

        return Optional.empty();
    }

    public static List<Coordinate> getAnySpotToPlay(final Board board, int returnLimit) {

        //Select a random place on the board
        //int x = ThreadLocalRandom.current().nextInt(board.getWidth());
        //int y = ThreadLocalRandom.current().nextInt(board.getHeight());
        int x = 0, y = 0;

        List<Coordinate> coordinates = new ArrayList<>();

        for (int i = 0, width = board.getWidth(); i < width && returnLimit > 0; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height && returnLimit > 0; j++, y = (y + 1) % height) {
                Box box = board.getBox(x, y);
                for (int side = 0; side < 4; side++) {
                    final Line line = box.getLine(side);
                    if (line.getPlayer() == 0 && returnLimit-- > 0) {
                        coordinates.add(new Coordinate(x,y,side));
                    }
                }
            }
        }

        return coordinates;
    }


    public static List<Coordinate> getSafeSpotsToPlay(final Board board, int returnLimit){

        //Select a random place on the board
        //int x = ThreadLocalRandom.current().nextInt(board.getWidth());
        //int y = ThreadLocalRandom.current().nextInt(board.getHeight());

        int x=0, y=0;

        List<Coordinate> coordinates = new ArrayList<>();

        for (int i = 0, width = board.getWidth(); i < width && returnLimit > 0; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height && returnLimit > 0; j++, y = (y + 1) % height) {

                Box box = board.getBox(x, y);
                if (box.getNumberOfUnclaimedLines() > 0){

                    for(int side=0; side<4; side++) {
                        final Line line = box.getLine(side);
                        if ( line.getPlayer() == 0) {
                            boolean closesBox = false;
                            for(Box parent : line.getParents() ){
                                if (parent.getNumberOfUnclaimedLines() == 2){
                                    closesBox = true;
                                }
                            }

                            if( !closesBox && returnLimit > 0){
                                returnLimit--;
                                coordinates.add(new Coordinate(x, y, side));
                            }
                        }
                    }
                }
            }
        }

        return coordinates;
    }
}