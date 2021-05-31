package io.github.followsclosely.dots;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DotsAndBoxesMouseAdapter extends MouseAdapter {

    private final TurnSupport turnSupport;

    private final int maxX, maxY;

    private int cellHeight;
    private int cellWidth;

    private Coordinate lastClick = null;
    private Coordinate lastClickTranslated = null;
    private final Polygon[] polygons;

    public DotsAndBoxesMouseAdapter(TurnSupport turnSupport, Board board) {
        this.turnSupport = turnSupport;
        this.maxX = board.getWidth() - 1;
        this.maxY = board.getHeight() - 1;

        this.polygons = new Polygon[4];
        for(int i=0; i<4; i++){
            polygons[i] = new Polygon();
        }
    }

    public void init(int w, int h){

        this.cellWidth = w;
        this.cellHeight = h;

        polygons[0].reset();
        polygons[0].addPoint(0,0);
        polygons[0].addPoint(cellWidth,0);
        polygons[0].addPoint(cellWidth/2,cellHeight/2);

        polygons[1].reset();
        polygons[1].addPoint(cellWidth,0);
        polygons[1].addPoint(cellWidth,cellHeight);
        polygons[1].addPoint(cellWidth/2,cellHeight/2);

        polygons[2].reset();
        polygons[2].addPoint(cellWidth,cellHeight);
        polygons[2].addPoint(0,cellHeight);
        polygons[2].addPoint(cellWidth/2,cellHeight/2);

        polygons[3].reset();
        polygons[3].addPoint(0,cellHeight);
        polygons[3].addPoint(0,0);
        polygons[3].addPoint(cellWidth/2,cellHeight/2);
    }

    @Override
    public synchronized void mouseClicked(MouseEvent event) {

        //Locate the box that contains the event
        int x = event.getX() / cellWidth;
        int y = event.getY() / cellHeight;

        int dx = event.getX() - (x*cellWidth);
        int dy = event.getY() - (y*cellHeight);

        //Locate the location within the box (relative to the upper right) of the event
        //System.out.println(String.format("cellWidth = %d, cellWidth=%d", this.cellWidth, this.cellWidth));
        //System.out.println(String.format("Clicking on: [%d,%d] : [%d,%d] -> [%d,%d]", event.getX(), event.getY(), x, y, dx, dy));

        int side = -1;

        if( x != Math.min(x, maxX) ){
            x = maxX;
            side = 1;
        } else if( y != Math.min(y, maxY) ){
            y = maxY;
            side = 2;
        }
        else {
            for (int i = 0; i < 4; i++) {
                if (polygons[i].contains(new Point(dx, dy))) {
                    side = i;
                    this.lastClickTranslated = new Coordinate(dx, dy, side);
                    break;
                }
            }
        }

        if ( side != -1) {
            turnSupport.onTurn(lastClick = new Coordinate(x,y,side));
        }
    }

    public Coordinate getLastClick() {
        return lastClick;
    }

    public Coordinate getLastClickTranslated() {
        return lastClickTranslated;
    }

    public Polygon[] getPolygons() {
        return polygons;
    }
}