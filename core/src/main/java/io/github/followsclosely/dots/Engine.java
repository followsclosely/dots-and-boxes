package io.github.followsclosely.dots;

import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DefaultBox;
import io.github.followsclosely.dots.impl.DefaultLine;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class runs a game using ArtificialIntelligence to play the pieces.
 */
public class Engine {

    // A List of players.
    private List<ArtificialIntelligence> players = new ArrayList<>();

    private DefaultBoard board;// = new DefaultBoard(3, 3);

    /**
     * Constructs and new Engine with a default board.
     *
     * @param ais The players in the game. Can be 2-N.
     */
    public Engine(ArtificialIntelligence... ais) {
        Collections.addAll(players, ais);
    }

    public void setBoard(Board board) {
        this.board = new DefaultBoard(board);
    }

    /**
     * Runs a simulation of one game.
     */
    public int startGame(int firstIndex) {

        if( board == null ){
            board = new DefaultBoard(3, 3);
        }

        ArtificialIntelligence ai0 = players.get(0);
        ArtificialIntelligence ai1 = players.get(1);

        for(ArtificialIntelligence player = players.get(firstIndex); true; )
        {
            Coordinate turn = player.yourTurn(board);
            //System.out.println(player.getClass().getSimpleName() + " : turn = " + turn);

            if( turn != null){
                boolean boxClosed = false;

                DefaultBox box = board.getBox(turn.getX(), turn.getY());
                DefaultLine line = box.getLine(turn.getSide());

                if( line.getPlayer() == 0) {
                    line.setPlayer(player.getColor());

                    for (DefaultBox parent : line.getParents()) {
                        if (parent.getPlayer() == 0 && parent.getNumberOfUnclaimedLines() == 0) {
                            parent.setPlayer(player.getColor());
                            boxClosed = true;
                        }
                    }
                }

                if( !boxClosed){
                    player = (player==ai0) ? ai1 : ai0;
                }
            }
            else
            {
                break;
            }
        }

        Map<Integer, AtomicInteger> counts = board.getCounts();

        Entry<Integer, AtomicInteger> maxEntry = Collections.max(counts.entrySet(), Comparator.comparingInt((Entry<Integer, AtomicInteger> e) -> e.getValue().get()));

        return maxEntry.getKey();
    }
}