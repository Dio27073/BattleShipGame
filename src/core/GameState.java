package core;
import java.io.Serializable;

public enum GameState implements Serializable{
    SETUP,       // players are placing their ships
    IN_PROGRESS, // game is currently being played
    GAME_OVER    // game has ended
}
