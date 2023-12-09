package core;
import core.*;
import javax.swing.*;
import java.awt.*;
public class CompositeBoardPanel extends JPanel {
    private JPanel currentPlayerBoardPanel;
    private JPanel opponentPlayerBoardPanel;

    public CompositeBoardPanel(Board currentPlayerBoard, Board opponentPlayerBoard) {
        setLayout(new GridLayout(1, 2));

        currentPlayerBoardPanel = new JPanel(new GridLayout(10, 10));
        opponentPlayerBoardPanel = new JPanel(new GridLayout(10, 10));

        initializeBoardPanel(currentPlayerBoard, currentPlayerBoardPanel);
        initializeBoardPanel(opponentPlayerBoard, opponentPlayerBoardPanel);

        add(currentPlayerBoardPanel);
        add(opponentPlayerBoardPanel);
    }

    private void initializeBoardPanel(Board board, JPanel boardPanel) {
        // Initialize and populate the board panel
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton();
                Cell cell = board.getCell(row, col);

                // Set button color based on cell state
                if (cell.isHit()) {
                    if (cell.isOccupied()) {
                        button.setBackground(Color.RED); // Red for hit ship
                    } else {
                        button.setBackground(Color.WHITE); // White for miss
                    }
                } else {
                    button.setBackground(Color.GRAY); // Gray for ship location
                }

                button.setEnabled(!cell.isHit()); // Disable button if cell is already hit

                boardPanel.add(button);
            }
        }
    }
}