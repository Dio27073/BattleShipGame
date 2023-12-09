package gui;

import core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StartMenu extends JFrame {

    public StartMenu() {
        setTitle("Battleship Start Menu");
        setSize(800, 1000);  // Size of window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createComponents();
    }

    private void createComponents() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton pvpButton = new JButton("Player vs Player");
        JButton pvaiButton = new JButton("Player vs AI");

       //pvpButton.addActionListener(e -> startPvPGame());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        centerPanel.add(pvpButton, gbc);

        gbc.gridy++;
        centerPanel.add(pvaiButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }


 /*

    private void startPvPGame() {
        String player1Name = JOptionPane.showInputDialog("Enter Player 1's Name:");
        String player2Name = JOptionPane.showInputDialog("Enter Player 2's Name:");
        if(player1Name != null && player2Name != null && !player1Name.trim().isEmpty() && !player2Name.trim().isEmpty()) {
            Game game = new Game(player1Name, player2Name);
            launchGame(game);
        }
    }


    private void launchGame(Game game) {
        SwingUtilities.invokeLater(() -> {
            BattleShip battleShip = new BattleShip(game);
            battleShip.setVisible(true);
            this.setVisible(false); // Hide the Start Menu
        });
    }

 */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu().setVisible(true);
        });
    }
}
