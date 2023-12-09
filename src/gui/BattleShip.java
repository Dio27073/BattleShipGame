 package gui;

import core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;

public class BattleShip extends JFrame {
    private Game game;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JPanel playerBoardPanel;
    private JPanel guessBoardPanel;
    private static final Color EMPTY_TILE_COLOR = Color.LIGHT_GRAY;
    private static final int BOARD_GAP_WIDTH = 5; //Gap width

    private JPanel createRulesPanel() {
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BorderLayout()); // You can choose a different layout as needed

        // Set the preferred width (and height) for the rules panel
        rulesPanel.setPreferredSize(new Dimension(200, this.getHeight())); // Adjust width as needed

        JTextArea rulesText = new JTextArea("Rules of the Game:\n\n -Grey Tiles are your ships\n\n -Blue Tiles are the sea" +
                "\n\n -If you hit an enemy ship or yours is hit, it will appear as Red\n\n -If you miss your shot, the Tile will" +
                "become white");
        rulesText.setEditable(false); // Make the text area non-editable
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);

        // Add a scroll pane in case the text is longer than the panel
        JScrollPane scrollPane = new JScrollPane(rulesText);
        rulesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add a clickable link for more rules
        JLabel linkLabel = new JLabel("<html><a href=''>More Rules</a></html>");
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.ultraboardgames.com/battleship/game-rules.php")); // Put the URL of your rules page here
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        rulesPanel.add(linkLabel, BorderLayout.SOUTH);

        return rulesPanel;
    }

    public BattleShip() {
        // initialize the game
        game = new Game("Player1", "Player2");
        game.getPlayer1().autoPlaceShips();
        game.getPlayer2().autoPlaceShips();

        // set up the game window
        setTitle("Battleship Game");
        setSize(800, 1000); // Adjust the window size as needed to accommodate the guess board
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up the game board panel
        boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS)); // Use BoxLayout to stack boards vertically
        initializeBoardPanel(game.getCurrentPlayer());
        add(boardPanel, BorderLayout.CENTER);

        JPanel rulesPanel = createRulesPanel();
        add(rulesPanel, BorderLayout.EAST);

        // set up the status label at the bottom
        statusLabel = new JLabel("Player1's turn");
        add(statusLabel, BorderLayout.SOUTH);

        // start game
        game.startGame();
    }

    private void initializeBoard(Board board, JPanel boardPanel, Player currentPlayer, boolean isPlayerBoard) {
        Color theSea = new Color(173, 216, 230);

        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++) {
                JButton button = new JButton();
                Cell cell = board.getCell(row, col);

                //set button color based on cell state
                if(isPlayerBoard){
                    if(cell.isOccupied()){
                        button.setBackground(Color.GRAY); // gray for players ships
                    }else{
                        button.setBackground(theSea);
                    }
                    button.setEnabled(false); //disable buttons on players board
                }else {
                    if(cell.isHit()){
                        if(cell.isOccupied()) {
                            button.setBackground(Color.RED);   // red = hit ship
                        }else {
                            button.setBackground(Color.WHITE); // white = miss
                        }
                    }else {
                        button.setBackground(theSea);
                    }
                    button.setEnabled(!cell.isHit()); // Enable only unhit cells on guess board
                }

                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> {
                    if(isPlayerBoard){
                        // no action if the player's own board is clicked
                    }else {
                        handleCellClick(finalRow, finalCol, button); // handle clicks on the guess board
                    }
                });
                boardPanel.add(button);
            }
        }
    }
    private void initializeBoardPanel(Player currentPlayer) {
        playerBoardPanel = new JPanel(new GridLayout(10, 10));
        guessBoardPanel = new JPanel(new GridLayout(10, 10));

        Board playerBoard = currentPlayer.getBoard();
        Board opponentBoard = game.getOpponentBoard(currentPlayer);

        initializeBoard(playerBoard, playerBoardPanel, currentPlayer, true);
        initializeBoard(opponentBoard, guessBoardPanel, currentPlayer, false);

        // create areas for the gap and labels
        Component gap = Box.createRigidArea(new Dimension(0, BOARD_GAP_WIDTH)); // adjust the height of the gap
        JLabel yourBoardLabel = new JLabel("Your Board");
        JLabel opponentBoardLabel = new JLabel("Opponent's Board");

        // add the boards and labels to the board panel
        boardPanel.removeAll();
        boardPanel.add(opponentBoardLabel); // add the opponent's board first
        boardPanel.add(guessBoardPanel);    // then the guess board
        boardPanel.add(gap);
        boardPanel.add(yourBoardLabel);     // then the player's board
        boardPanel.add(playerBoardPanel);

        // disable buttons on the player's board
        disableButtons(playerBoardPanel);

        // enable buttons on the guess board
        enableButtons(guessBoardPanel);

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // helper method to disable buttons on a panel
    private void disableButtons(JPanel panel) {
        for(Component component : panel.getComponents()){
            if(component instanceof JButton){
                JButton button = (JButton) component;
                button.setEnabled(false);
            }
        }
    }

    // helper method to enable buttons on a panel
    private void enableButtons(JPanel panel) {
        for(Component component : panel.getComponents()){
            if(component instanceof JButton){
                JButton button = (JButton) component;
                button.setEnabled(true);
            }
        }
    }

    private void handleCellClick(int row, int col, JButton button) {
        Player currentPlayer = game.getCurrentPlayer();
        Board opponentBoard = game.getOpponentBoard(currentPlayer);

        // check if the cell has already been guessed
        Cell cell = opponentBoard.getCell(row, col);
        if(cell.isHit()) {
            return;  // cell has already been guessed, do nothing
        }

        // player takes a shot
        boolean hit = opponentBoard.shootAt(row, col);
        updateButtonState(button, hit);

        String hitOrMiss = hit ? "HIT" : "MISS"; // update the status label
        statusLabel.setText(currentPlayer.getName() + " shoots at (" + (row + 1) + "," + (col + 1) + ") and it is a " + hitOrMiss);


        // switch to the next player's turn
        game.endTurn();
        currentPlayer = game.getCurrentPlayer();
        initializeBoardPanel(currentPlayer);
        statusLabel.setText(currentPlayer.getName() + "'s turn");

        // check for a winner
        if(game.isGameOver()) {
            displayEndGameDialog();
        }else{
            // Switch to the next player's board and update the status label
            currentPlayer = game.getCurrentPlayer();
            initializeBoardPanel(currentPlayer);
            statusLabel.setText(currentPlayer.getName() + "'s turn");
        }
    }

    private void displayEndGameDialog() {
        Player winner = game.getWinner();
        int response = JOptionPane.showOptionDialog(this,
                "Game Over! The winner is " + winner.getName() + ".\nWould you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Restart", "Exit"}, // options
                "default");

        if(response == JOptionPane.YES_OPTION) {
            game.restartGame();
            initializeBoardPanel(game.getCurrentPlayer());
            statusLabel.setText("New game started! " + game.getCurrentPlayer().getName() + "'s turn.");
        }else {
            System.exit(0); // or some other logic to close the game
        }
    }

    private void updateButtonState(JButton button, boolean hit) {
        if(hit){
            button.setBackground(Color.RED);    // red for hit ship
        }else {
            button.setBackground(Color.WHITE);  // blue for miss
        }
        button.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BattleShip().setVisible(true);
            }
        });
    }
}