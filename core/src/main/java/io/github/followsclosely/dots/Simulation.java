package io.github.followsclosely.dots;

import io.github.followsclosely.dots.ai.DummyAI;
import io.github.followsclosely.dots.impl.DefaultBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private int numberOfGames = 1000000;

    private Map<Integer, AtomicInteger> counts = new HashMap<>() {
        @Override
        public AtomicInteger get(Object key) {
            AtomicInteger value = super.get(key);
            if (value == null) {
                super.put((Integer) key, value = new AtomicInteger(0));
            }
            return value;
        }
    };

    private List<ArtificialIntelligence> ais = new ArrayList<>();

    public Simulation addArtificialIntelligence(ArtificialIntelligence ai) {
        ais.add(ai);
        return this;
    }

    public Simulation number(int simulations) {
        this.numberOfGames = simulations;
        return this;
    }

    private DefaultBoard board;
    public Simulation board(DefaultBoard board){
        this.board = board;
        return this;
    }

    public Simulation run() {

        if (ais.size() == 0) {
            System.out.println("ERROR: ai not provided, call addArtificialIntelligence()");
            return this;
        } else if (ais.size() == 1) {
            ais.add(0, new DummyAI(1));
        }

        for (int i = 1; i <= numberOfGames; i++) {
            Engine engine = new Engine(ais.toArray(new ArtificialIntelligence[0]));
            if( board != null ){
                engine.setBoard(board);
            }
            int winner = engine.startGame(i % ais.size());
            counts.get(winner).getAndIncrement();
            System.out.print("\r" + i + "/" + numberOfGames);

//            if( winner == 1) {
//                for (Coordinate c : engine.getBoard().getTurns()){
//                    System.out.println(String.format("board.dropPiece(%d,%d);", c.getX(), engine.getBoard().getPiece(c.getX(), c.getY())));
//                }
//                System.exit(0);
//            }
        }
        System.out.println();

        return this;
    }

    public Simulation printSummary() {

        for (Map.Entry<Integer, AtomicInteger> entry : counts.entrySet()) {
            StringBuilder b = new StringBuilder();
            b.append("Player/Color\t").append(entry.getKey()).append(": ");
            b.append((float) (Math.round(entry.getValue().floatValue() / numberOfGames * 10000)) / 100).append("%\t");
            b.append(entry.getValue());
            System.out.println(b);
        }

        return this;
    }

    public Map<Integer, AtomicInteger> getCounts() {
        return counts;
    }

    public String getName() {
        return ais == null || ais.size() == 0 ? null : ais.get(0).getClass().getName();
    }

    public float getNumberOfGames() {
        return numberOfGames;
    }

    public Integer getWins() {
        try {
            return getWins(ais.get(0).getColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getWins(int color) {
        return (counts == null) ? null : counts.get(color).intValue();
    }

}
