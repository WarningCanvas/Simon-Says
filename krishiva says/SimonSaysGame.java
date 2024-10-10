import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class SimonSaysGame extends JFrame implements ActionListener {
    private final JButton redButton = new JButton();
    private final JButton greenButton = new JButton();
    private final JButton blueButton = new JButton();
    private final JButton yellowButton = new JButton();
    
    private final ArrayList<Integer> gameSequence = new ArrayList<>();
    private final ArrayList<Integer> playerSequence = new ArrayList<>();
    
    private int currentLevel = 0;
    private boolean playerTurn = false;
    
    public SimonSaysGame() {
        setTitle("Simon Says Game");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));
        
        // Set button colors
        redButton.setBackground(Color.RED);
        greenButton.setBackground(Color.GREEN);
        blueButton.setBackground(Color.BLUE);
        yellowButton.setBackground(Color.YELLOW);
        
        // Add action listeners
        redButton.addActionListener(this);
        greenButton.addActionListener(this);
        blueButton.addActionListener(this);
        yellowButton.addActionListener(this);
        
        // Add buttons to the frame
        add(redButton);
        add(greenButton);
        add(blueButton);
        add(yellowButton);
        
        // Start the game
        nextRound();
    }
    
    private void nextRound() {
        playerSequence.clear();
        currentLevel++;
        playerTurn = false;
        
        // Generate a new random color and add it to the sequence
        Random random = new Random();
        int nextColor = random.nextInt(4);
        gameSequence.add(nextColor);
        
        // Only show the new color to the player
        showNewColor();
    }
    
    private void showNewColor() {
        // Show the latest color added to the sequence
        int lastColorIndex = gameSequence.size() - 1;
        int newColor = gameSequence.get(lastColorIndex);
        
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flashButton(newColor);
                ((Timer) e.getSource()).stop();
                playerTurn = true;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void flashButton(int color) {
        JButton buttonToFlash;
        switch (color) {
            case 0 -> buttonToFlash = redButton;
            case 1 -> buttonToFlash = greenButton;
            case 2 -> buttonToFlash = blueButton;
            case 3 -> buttonToFlash = yellowButton;
            default -> throw new IllegalArgumentException("Invalid color");
        }
        
        Timer flashTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonToFlash.setBackground(getButtonColor(color));
                ((Timer) e.getSource()).stop();
            }
        });
        buttonToFlash.setBackground(Color.WHITE); // Flash color
        flashTimer.setRepeats(false);
        flashTimer.start();
    }
    
    private Color getButtonColor(int color) {
        return switch (color) {
            case 0 -> Color.RED;
            case 1 -> Color.GREEN;
            case 2 -> Color.BLUE;
            case 3 -> Color.YELLOW;
            default -> throw new IllegalArgumentException("Invalid color");
        };
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn) {
            return; // Ignore input if it's not the player's turn
        }
        
        JButton clickedButton = (JButton) e.getSource();
        int clickedColor = -1;
        
        if (clickedButton == redButton) clickedColor = 0;
        else if (clickedButton == greenButton) clickedColor = 1;
        else if (clickedButton == blueButton) clickedColor = 2;
        else if (clickedButton == yellowButton) clickedColor = 3;
        
        playerSequence.add(clickedColor);
        
        if (!isPlayerSequenceCorrect()) {
            gameOver();
        } else if (playerSequence.size() == gameSequence.size()) {
            nextRound(); // Player completed the sequence correctly, move to the next round
        }
    }
    
    private boolean isPlayerSequenceCorrect() {
        // Compare player input with the correct sequence
        for (int i = 0; i < playerSequence.size(); i++) {
            if (!playerSequence.get(i).equals(gameSequence.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over! You reached level " + currentLevel);
        gameSequence.clear();
        currentLevel = 0;
        nextRound();
    }
    
    public static void main(String[] args) {
        SimonSaysGame game = new SimonSaysGame();
        game.setVisible(true);
    }
}
