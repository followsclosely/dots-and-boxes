package io.github.followsclosely.dots;

import io.github.followsclosely.dots.impl.DefaultBoard;
import io.github.followsclosely.dots.impl.DefaultBox;
import io.github.followsclosely.dots.impl.DefaultLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DotsAndBoxesPanel extends JPanel {

    private final int DEFAULT_CELL_SIZE = 100;
    private final Dimension DEFAULT_MINIMUM_SIZE;
    public static final int DOT_DIAMETER = 11;

    private static final int barHeight = 5;

    protected final DefaultBoard board;
    private DotsAndBoxesMouseAdapter mouseAdapter;
    protected int cellHeight;
    protected int cellWidth;

    private final ComponentAdapter resizer = new ComponentAdapter() {
        public synchronized void componentResized(ComponentEvent e) {
            DotsAndBoxesPanel.this.cellWidth = (getWidth()-DOT_DIAMETER) / board.getWidth();
            DotsAndBoxesPanel.this.cellHeight = (getHeight()-DOT_DIAMETER) / board.getHeight();

            if( mouseAdapter != null) {
                mouseAdapter.init(cellWidth, cellHeight);
            }
        }
    };

    private Color[] COLORS = {new Color(225, 225, 225), new Color(165, 25, 25), new Color(25, 25, 165)};
    //private Color[] COLORS_BRIGHTER = {COLORS[0], new Color(225, 95, 95), new Color(95, 95, 225)};

    private boolean initialized = false;

    public DotsAndBoxesPanel(DefaultBoard board) {
        this.board = board;
        this.DEFAULT_MINIMUM_SIZE = new Dimension(board.getWidth() * DEFAULT_CELL_SIZE, board.getHeight() * DEFAULT_CELL_SIZE);
        this.addComponentListener(resizer);
    }

    public void addDotsAndBoxesMouseAdapter(DotsAndBoxesMouseAdapter mouseAdapter){
        this.addMouseListener(this.mouseAdapter = mouseAdapter);
    }

    @Override
    public Dimension getPreferredSize() {
        return DEFAULT_MINIMUM_SIZE;
    }

    @Override
    public void paint(Graphics g) {

        if (!initialized) {
            resizer.componentResized(null);
            initialized = true;
        }

        int width = board.getWidth();
        int height = board.getHeight();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GRAY);
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {

                if (x < width && y < height) {
                    DefaultBox box = board.getBox(x, y);
                    if (box != null) {

                        //Draw the box if it is owned by a player
                        if( box.getPlayer() != 0){
                            g.setColor(COLORS[box.getPlayer()]);
                            g.fillRect(x * cellWidth + DOT_DIAMETER /2, y * cellHeight + DOT_DIAMETER /2, cellWidth, cellHeight);
                        }

                        g.setColor(Color.BLACK);

                        //Draw the top
                        DefaultLine top = box.getLine(0);
                        //g.setColor(COLORS[top.getPlayer()]);
                        if( top.getPlayer() > 0)
                        g.fillRect(x * cellWidth + DOT_DIAMETER, y * cellHeight + (DOT_DIAMETER -barHeight)/2 , cellWidth - DOT_DIAMETER + 2, barHeight);

                        //Draw the left
                        DefaultLine left = box.getLine(3);
                       // g.setColor(COLORS[left.getPlayer()]);
                        if( left.getPlayer() > 0)
                        g.fillRect(x * cellWidth + (DOT_DIAMETER -barHeight)/2, y * cellHeight + DOT_DIAMETER, barHeight, cellHeight - DOT_DIAMETER + 2);

                        //Draw the right
                        if (x == width - 1) {
                            DefaultLine right = box.getLine(1);
                            //g.setColor(COLORS[right.getPlayer()]);
                            if( right.getPlayer() > 0)
                            g.fillRect((x + 1) * cellWidth + (DOT_DIAMETER -barHeight)/2, y * cellHeight + DOT_DIAMETER, barHeight, cellHeight - DOT_DIAMETER + 2);
                        }

                        //Draw the bottom
                        if (y == height - 1) {
                            DefaultLine bottom = box.getLine(2);
                            //g.setColor(COLORS[bottom.getPlayer()]);
                            if( bottom.getPlayer() > 0)
                            g.fillRect(x * cellWidth + DOT_DIAMETER, (y + 1) * cellHeight + (DOT_DIAMETER -barHeight)/2, cellWidth - DOT_DIAMETER + 2, barHeight);
                        }
                    }
                }

                //Draw the dots
                g.setColor(Color.BLACK);
                g.fillOval(x * cellWidth, y * cellHeight, DOT_DIAMETER, DOT_DIAMETER);

            }
        }

//        if( mouseAdapter != null ){
//            for(Polygon p : mouseAdapter.getPolygons()){
//                g.setColor(Color.RED);
//                g.drawPolygon(p);
//            }
//        }
//
//        g.setColor(Color.BLUE);
//        Coordinate lastClick = mouseAdapter.getLastClick();
//        if( lastClick!= null ){
//            g.fillOval(lastClick.getX(), lastClick.getY(), 3, 3);
//        }
//        g.setColor(Color.GREEN);
//        Coordinate lastClickTranslated = mouseAdapter.getLastClickTranslated();
//        if( lastClickTranslated!= null ){
//            g.fillOval(lastClickTranslated.getX(), lastClickTranslated.getY(), 3, 3);
//        }
    }
}