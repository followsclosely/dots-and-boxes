package io.github.followsclosely;

import io.github.followsclosely.dots.*;
import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DefaultBox;
import io.github.followsclosely.dots.impl.DefaultLine;
import io.github.followsclosely.dots.impl.DotsAndBoxesUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StinkAI implements ArtificialIntelligence {

    private static int STRATEGY_LIMIT = Integer.MAX_VALUE;

    private int color;
    private int opponent = 1;

    private final Random random = new Random();

    public StinkAI(int color) {
        this.color = color;
    }

    @Override public int getColor() {
        return color;
    }

    @Override
    public Coordinate yourTurn(Board b) {

        final DefaultBoard board = new DefaultBoard(b);

        List<Coordinate> safeSpots = DotsAndBoxesUtils.getSafeSpotsToPlay(board, STRATEGY_LIMIT);

        if( safeSpots.size() >= STRATEGY_LIMIT){
            Optional<Coordinate> coordinate = closeBoxIfYouCanStrategy(board);
            if( coordinate.isPresent() ){
                return coordinate.get();
            }
        }
        else
        {
            //Get all the valid spots to play.
            List<Coordinate> coordinates = DotsAndBoxesUtils.getAnySpotToPlay(board, Integer.MAX_VALUE);
            for(Coordinate turn : coordinates) {

                //System.out.println(turn + " :simulateRecursiveGame(depth=n/a)...");

                Map.Entry<Integer, AtomicInteger> results = simulateGame(new DefaultBoard(board), turn, color, opponent);
                System.out.println( results.getKey() + " : "+results.getValue()+" Blocks Won");
            }
        }

        return (!safeSpots.isEmpty())
                ? safeSpots.get(0)
                : DotsAndBoxesUtils.getAnySpotToPlay(board).orElse(null);
    }

    public Optional<Coordinate> closeBoxIfYouCanStrategy(Board board) {

        //Select a random place on the board
        int x = random.nextInt(board.getWidth());
        int y = random.nextInt(board.getHeight());

        //Close a box if we can.
        for (int i = 0, width = board.getWidth(); i < width; i++, x = (x + 1) % width) {
            for (int j = 0, height = board.getHeight(); j < height; j++, y = (y + 1) % height) {
                Box box = board.getBox(x, y);
                if (box.getNumberOfUnclaimedLines() == 1) {
                    for (int side = 0; side < 4; side++) {
                        if (box.getLine(side).getPlayer() == 0) {
                            return Optional.ofNullable(new Coordinate(x, y, side));
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    public Map.Entry<Integer, AtomicInteger> simulateGame(DefaultBoard board, Coordinate turn, int color, int opponent){

        Map.Entry<Integer, AtomicInteger> results = ( processTurn(board, turn, color) )
                ? simulateRecursiveGame(board, opponent, color, 1)
                : simulateRecursiveGame(board, color, opponent, 1);

        return results;
    }

    public Map.Entry<Integer, AtomicInteger> simulateRecursiveGame(DefaultBoard board, int color, int opponent, int depth){
        Optional<Coordinate> turn = DotsAndBoxesUtils.getAnySpotToPlay(board);

        //System.out.println(turn + " :simulateRecursiveGame(depth="+depth+")...");

        if( turn.isPresent() ){
            return ( processTurn(board, turn.get(), color) )
                    ? simulateRecursiveGame(board, opponent, color, depth+1)
                    : simulateRecursiveGame(board, color, opponent, depth+1);
        } else {
            Map<Integer, AtomicInteger> counts = board.getCounts();
            Map.Entry<Integer, AtomicInteger> maxEntry = Collections.max(counts.entrySet(), Comparator.comparingInt((Map.Entry<Integer, AtomicInteger> e) -> e.getValue().get()));
            return maxEntry;
        }
    }

    private boolean processTurn(DefaultBoard board, Coordinate turn, int color){

        boolean boxClosed = false;

        DefaultLine line = board.getBox(turn.getX(), turn.getY()).getLine(turn.getSide());

        if( line.getPlayer() == 0) {
            line.setPlayer(color);
            //System.out.println(turn + " :processTurn()... claiming line for " + color);

            for (DefaultBox parent : line.getParents()) {
                if (parent.getNumberOfUnclaimedLines() == 0) {
                    parent.setPlayer(color);
                    boxClosed = true;
                }
            }
        } else {
            System.out.println("Can not claim a spot already taken!");
        }



        return boxClosed;
    }
}