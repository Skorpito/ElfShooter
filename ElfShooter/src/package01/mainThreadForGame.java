package package01;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The basic setup for the game is based upon the minesweeper game by the person below
 *
 * Author: Jan Bodnar
 * Website: http://zetcode.com
 * 
 * Jframe allows for the drawing of the screen
 */
public class mainThreadForGame extends JFrame {

    private JLabel statusbar;
    private GameSetupAndPlayer g;
    
    //when you create the game class it just initializes the UI
    public mainThreadForGame() {
        initUI();
    }

    
    /**
     * This code sets up the frame that contains the game 
     */
    private void initUI() {

    	/*
    	 * Add a status bar and the game to the window
    	 */
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);
        g=(new GameSetupAndPlayer(statusbar));
        add(g);
		
        /*
         * Now make the window non-resizable and make the window fit the game
         */
        setResizable(false);
        pack();

        //Misc important setup
        setTitle("Java Test");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
    	
    	/*
    	 * Create the game and make it visible
    	 */
    	mainThreadForGame ex = new mainThreadForGame();
        ex.setVisible(true);
        

    }


}
