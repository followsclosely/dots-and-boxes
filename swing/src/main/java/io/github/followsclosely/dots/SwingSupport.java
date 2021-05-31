package io.github.followsclosely.dots;

import io.github.followsclosely.dots.ai.DummyAI;
import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DefaultBox;
import io.github.followsclosely.dots.impl.DefaultLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class assembles the game into a JFrame, registers listeners and displays.
 *
 * @author Matthew L. Wilson
 */
public class SwingSupport {

    public static final int PLAYER_COLOR = 1;
    public static final int COMPUTER_COLOR = 2;

    private DefaultBoard board;
    private ArtificialIntelligence bot;

    private final List<Coordinate> turns = new ArrayList<>();

    public static void main(String[] args) {
        new SwingSupport()
                .setBoard(new DefaultBoard(2,10))
                .setArtificialIntelligence(new DummyAI(COMPUTER_COLOR))
                .run();
    }

    public SwingSupport setArtificialIntelligence(ArtificialIntelligence bot) {
        this.bot = bot;
        return this;
    }

    public SwingSupport setBoard(DefaultBoard board) {
        this.board = board;
        return this;
    }

    /**
     * Launches the JFrame that contains the BoardPanel to display the game.
     */
    public void run() {

        if (board == null) {
            board = new DefaultBoard(5, 5);
        }

        //Create the panel that displays the tic tac toe board.
        final DotsAndBoxesPanel boardPanel = new DotsAndBoxesPanel(board);
        boardPanel.addDotsAndBoxesMouseAdapter(new DotsAndBoxesMouseAdapter(turn -> {
            DefaultBox box = board.getBox(turn.getX(), turn.getY());
            DefaultLine line = box.getLine(turn.getSide());
            if( line.getPlayer() == 0) {
                turns.add(turn);
                line.setPlayer(PLAYER_COLOR);
                SwingUtilities.invokeLater(() -> boardPanel.repaint());
                boolean boxClaimed = false;
                for (DefaultBox parent : line.getParents()){
                    if( parent.getNumberOfUnclaimedLines() == 0 ){
                        parent.setPlayer(PLAYER_COLOR);
                        boxClaimed = true;
                    }
                }

                SwingUtilities.invokeLater(() -> boardPanel.repaint());

                if( !boxClaimed ) {
                    new Thread(() ->computersTurn(board, boardPanel)).start();
                }
            }
        }, board));

        JPanel statusPanel = new JPanel(new BorderLayout());
        JTextField status = new JTextField("");
        status.setEditable(false);
        statusPanel.add(status, BorderLayout.CENTER);
        JButton undo = new JButton("<");
        undo.addActionListener((ActionEvent e) -> SwingUtilities.invokeLater(() -> {
            Coordinate turn = turns.remove(turns.size()-1);
            Box box = board.getBox(turn.getX(), turn.getY());
            DefaultLine line =(DefaultLine)box.getLine(turn.getSide());
            for(DefaultBox parent : line.getParents() ){
                if ( parent.getPlayer() != 0){
                    parent.setPlayer(0);
                }
            }
            line.setPlayer(0);
            SwingUtilities.invokeLater( () -> boardPanel.repaint());
        }));
        statusPanel.add(undo, BorderLayout.EAST);

        JFrame frame = new JFrame("Connect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.pack();

        frame.setVisible(true);
    }

    private void computersTurn(DefaultBoard board, JComponent component){
        Coordinate botsPlay = bot.yourTurn(board);
        if( botsPlay != null){
            try { Thread.sleep(200); } catch(Exception ignore){}
            DefaultBox box = board.getBox(botsPlay.getX(), botsPlay.getY());
            DefaultLine line = box.getLine(botsPlay.getSide());

            if( line.getPlayer() == 0) {
                turns.add(botsPlay);
                line.setPlayer(COMPUTER_COLOR);
                SwingUtilities.invokeLater(() -> component.repaint());
                try {
                    Thread.sleep(200);
                } catch (Exception ignore) {
                }

                boolean boxClosed = false;
                for (DefaultBox parent : line.getParents()) {
                    if (parent.getPlayer() == 0 && parent.getNumberOfUnclaimedLines() == 0) {
                        parent.setPlayer(COMPUTER_COLOR);
                        boxClosed = true;
                    }
                }

                if (boxClosed) {
                    SwingUtilities.invokeLater(() -> component.repaint());
                    computersTurn(board, component);
                }
            }
        }
    }
}