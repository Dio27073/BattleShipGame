package core;
import java.util.Random;

import java.io.Serializable;

public class Game implements Serializable {
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private GameState gameState;

    public Game(String player1Name, String player2Name){
        this.player1 = new Player(player1Name); // player 1
        this.player2 = new Player(player2Name); // player 2

        this.currentPlayer = player1;     // player 1 starts
        this.gameState = GameState.SETUP; // game starts in setup mode
    }

    // call when both players have finished placing ships
    public Board getOpponentBoard(Player currentPlayer) {
        if (currentPlayer == player1) {
            return player2.getBoard();
        } else {
            return player1.getBoard();
        }
    }
    public void startGame() {

        if(gameState != GameState.SETUP) {
            throw new IllegalStateException("Cannot start the game until all ships have been placed.");
        }

        //randomly choose which player goes first
        Random random = new Random();
        currentPlayer = random.nextBoolean() ? player1 : player2;

        gameState = GameState.IN_PROGRESS;
    }

    //handle the logic for a player's turn
    public boolean takeTurn(int row, int col) {
        if(gameState != GameState.IN_PROGRESS) {
            throw new IllegalStateException("Cannot take a turn when the game is not in progress.");
        }

        boolean hit = currentPlayer.takeShot(row, col); // record the shot

        if(checkGameOver()) {
            gameState = GameState.GAME_OVER;
        }else {
            switchPlayer(); // switch to the next player's turn if the game is still in progress
        }

        return hit;
    }

    //check if the game is over
    private boolean checkGameOver() {
        return player1.hasLost() || player2.hasLost();
    }

    //switches the current player to the other player
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    //determine the winner of the game
    public Player getWinner(){
        if(!isGameOver()){
            throw new IllegalStateException("Cannot determine a winner until the game is over.");
        }
        return (player1.hasLost()) ? player2 : player1;
    }
    public void endTurn() {
        switchPlayer();   //logic to switch players
    }

    public void restartGame() {
        player1.resetPlayer();
        player2.resetPlayer();
        currentPlayer = player1;
        gameState = GameState.SETUP;
    }

}
