package core;
import java.io.Serializable;

public class Cell implements Serializable{
    private boolean isOccupied;
    private boolean isHit;
    private Ship ship;

    public Cell(){
        isOccupied = false;
        isHit = false;
        ship = null;
    }

    //getters and setters
    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied){
        isOccupied = occupied;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        this.isOccupied = (ship != null);
    }

}
