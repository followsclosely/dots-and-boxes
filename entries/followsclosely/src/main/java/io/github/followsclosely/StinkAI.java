package io.github.followsclosely;

import io.github.followsclosely.dots.*;
import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DotsAndBoxesUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class StinkAI implements ArtificialIntelligence {

    private static int STRATEGY_LIMIT = 5;

    private int color;
    private int opponent = 1;

    private final Random random = new Random();

    public StinkAI(int color) {
        this.color = color;
    }

    private Engine engine = new Engine();

    @Override public int getColor() {
        return color;
    }

    @Override
    public Coordinate yourTurn(Board b) {

        if( b instanceof DefaultBoard) {
            ((DefaultBoard) b).getLines().forEach(l -> l.setScore(null));
        }

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

            try {
                Map<Coordinate, AtomicInteger> values = new HashMap<>(coordinates.size());
                for (Coordinate turn : coordinates) {
                    //System.out.println(turn + " :simulateRecursiveGame(depth=n/a)...");

                    for(int i=0; i<10000; i++) {
                        Map<Integer, AtomicInteger> results = simulateGame(new DefaultBoard(board), turn, color, opponent);
                        //System.out.println(color + " " + turn + " : " + results);
                        Map.Entry<Integer, AtomicInteger> maxEntry = Collections.max(results.entrySet(), Comparator.comparingInt((Map.Entry<Integer, AtomicInteger> e) -> e.getValue().get()));
                        //System.out.println("maxEntry: " + maxEntry);


                        if (maxEntry.getKey() == color) {
                            //If the A.I won this, then add score to the entry in the map for this Coordinate
                            values.computeIfAbsent(turn, t -> new AtomicInteger()).addAndGet(maxEntry.getValue().intValue());
                        } else {
                            //If the A.I lost this, then subtract the score from the entry in the map for this Coordinate
                            values.computeIfAbsent(turn, t -> new AtomicInteger()).addAndGet(-maxEntry.getValue().intValue());
                        }
                    }

                    //This is a line to assist in debugging the ai.
                    ((DefaultBoard)b).getBox(turn.getX(), turn.getY()).getLine(turn.getSide()).setScore(values.get(turn).get());
                }

                System.out.println("values: " + values);
                //if (values(color)) {

                if ( values.size() > 1) {
                    Map.Entry<Coordinate, AtomicInteger> maxEntry = Collections.max(values.entrySet(), Comparator.comparingInt((Map.Entry<Coordinate, AtomicInteger> e) -> e.getValue().get()));

                    System.out.println("Returning " + maxEntry.getKey());

                    if (maxEntry.getKey() != null) {
                        return maxEntry.getKey();
                    }
                } else if (values.size() == 1){
                    return values.entrySet().stream().findFirst().get().getKey();
                }
            } catch (Throwable e){
                e.printStackTrace();
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
                            return Optional.of(new Coordinate(x, y, side));
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    public Map<Integer, AtomicInteger> simulateGame(DefaultBoard board, Coordinate turn, int color, int opponent){

        Map<Integer, AtomicInteger> results = ( engine.processTurn(board, turn, color) )
                ? simulateRecursiveGame(board, color, opponent, 1)
                : simulateRecursiveGame(board, opponent, color, 1);

        return results;


//        Map.Entry<Integer, AtomicInteger> maxEntry = Collections.max(counts.entrySet(), Comparator.comparingInt((Map.Entry<Integer, AtomicInteger> e) -> e.getValue().get()));
//        return maxEntry;
    }

    public Map<Integer, AtomicInteger> simulateRecursiveGame(DefaultBoard board, int color, int opponent, int depth) {

        //Optional<Coordinate> turn = ( color == this.opponent ) ? closeBoxIfYouCanStrategy(board) : Optional.empty();

        //if( !turn.isPresent()) {
        Optional<Coordinate> turn = DotsAndBoxesUtils.getAnySpotToPlay(board);
        //}

        //System.out.println(turn + " :simulateRecursiveGame(depth="+depth+")...");

        if( turn.isPresent() ){
            return ( engine.processTurn(board, turn.get(), color) )
                    ? simulateRecursiveGame(board, color, opponent, depth+1)
                    : simulateRecursiveGame(board, opponent, color, depth+1);
        } else {
            return board.getCounts();
        }
    }


}