package core;
import java.io.Serializable;

public class Ship implements Serializable{
    private final ShipType type;
    private final boolean[] hitSegments;

    private Position position;    // the starting position of the ship on the board
    private boolean isHorizontal; // the orientation of the ship
    private boolean isSunk;

    public Ship(ShipType type){
        this.type = type;
        this.hitSegments = new boolean[type.getSize()];
        this.isSunk = false;
    }

    //set the position of the ship on the board
    public void setPosition(Position position, boolean isHorizontal) {
        this.position = position;
        this.isHorizontal = isHorizontal;
    }

    // getters and setters
    public ShipType getType() {
        return type;
    }

    public int getSize() {
        return type.getSize();
    }

    public Position getPosition() {
        return position;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public boolean isHit(int row, int column) {
        if(isHorizontal){
            int colDiff = column - position.getColumn();

            //check if the column is within the ship's length
            if(colDiff >= 0 && colDiff < getSize()){
                return hitSegments[colDiff];
            }
        }else{
            int rowDiff = row - position.getRow(); //check if the row is within the ship's length

            //check if the column is within the ship's length
            if(rowDiff >= 0 && rowDiff < getSize()){
                return hitSegments[rowDiff];
            }
        }
        return false;
    }

    public void hit(int segment) {
        if(segment >= 0 && segment < hitSegments.length) {
            hitSegments[segment] = true; // mark the segment as hit

            isSunk = checkSunk();  // check if the ship is sunk
        }
    }

    private boolean checkSunk(){
        for(boolean hit : hitSegments) {
            if(!hit) {
                return false; // at least one ship segment not hit
            }
        }
        return true; //all segments hit ship is sunk
    }


}
