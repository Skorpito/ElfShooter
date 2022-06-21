package package01;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;

public class GameSetupAndPlayer extends JPanel {
	private final int GAME_WIDTH = 1024;
	private final int GAME_HEIGHT = 720;

	private ArrayList<Dwarf> dwarfList = new ArrayList<Dwarf>();
	JLabel statusbar;
	private Elf elf= new Elf();
	private Background b = new Background();
	private Line line = new Line();

	private int maxStreak=0;

	/**
	 * Sets up the basic stuff required for the game
	 * @param statusbar the status bar at the bottom
	 */
	public GameSetupAndPlayer(JLabel statusbar) {
		//setup how big the screen is 
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		
		this.statusbar = statusbar;
		
		//start up the game
		newGame();
		
		//this refreshes the screen 60 times per second in order to make the dwarf movement look smoth
		screenRefreshHandler refresh = new screenRefreshHandler();
		refresh.start();
	}

	// getters and Setters
	public int gameWidth() {
		return GAME_WIDTH;
	}
	public int gameHeight() {
		return GAME_HEIGHT;
	}
	public ArrayList<Dwarf> getDwarfList() {
		return dwarfList;
	}
	public void setDwarfList(ArrayList<Dwarf> newDwarf) {
		dwarfList = newDwarf;
	}
	
	public int getMaxStreak() {
		return maxStreak;
	}

	public void setMaxStreak(int maxStreak) {
		this.maxStreak = maxStreak;
	}
	
	//This removes the line
	public void clearLine() {
		line= new Line(); //the constructor with no parameters makes the line invisible
	}
	
	public JLabel getStatusbar() {
		return statusbar;
	}
	
	


	/**
	 * Setup the basic things that the games relies on
	 * 
	 */
	private void newGame() {
		//add he mouselistener that handles userinput
		addMouseListener(new CardListener());
		
		//set the drawable's game to be this 
		Drawables.setGame(this);
		
		//add the two dwarfs
		//adding the background and elf is not nessary, already add it
		dwarfList.add(new Dwarf(50,0,440));
		dwarfList.add(new Dwarf(GAME_HEIGHT-143-60,0,GAME_WIDTH));
		
		//Draw the graphics now that everything has been added 
		repaint();
		
		
	}


	/**
	 * This is called whenever the repaint() method is called 
	 * Note that there is only method so we must handle all the painting logic and order in here 
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		// Change to be 2d as we are working in 2d
		Graphics2D g2d = (Graphics2D) g.create();
		
		//draw the background (is first as the background is alwasys drawn over
		b.paint(g2d);
		elf.paint(g2d); //not really needed the elf is never drawn
	
		
		// draw all the dwarfs
		for (Drawables dwarf : dwarfList) {
			dwarf.paint(g2d);
		}
		
		//paint the line being shot
		line.paint(g2d);
		
		//prevent memory leak
		g2d.dispose();
	}

	/**
	 * user interaction, runs in separate thread
	 *
	 */
	private class CardListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			//if it can shot shoot it
			if (elf.getCanShoot()) {
				
				//print out diagnostic information
				System.out.println(x +", " + y);
				
				//create a line that connects to the point being shot
				line =new Line(x,y);
				
				//kill the dwarfs
				elf.shoot(x, y);
				
				//make it enter a refactory period
				elf.setCanShoot(false);
				
				//This runs in a seperate thread, makes sure that you cannot spam fire
				RefreshHandler r= new RefreshHandler();
				r.start();
			}

			//redraw everytime it  it is fired
			repaint();
			System.out.println("PRESSED");
		}
		
		
		//Makes it so that the elf can fire after some amount of time 
		protected class RefreshHandler extends Thread{
			private final int refreshTimeMs=500;
			public void run () {
				//This line of code is nessary to avoid a synchronization error
				 synchronized(this){
					//try catch is nessary for the wait method
					try {wait(refreshTimeMs);}
					catch (InterruptedException e) {e.printStackTrace();}
					
					if(!elf.getCanShoot()) {
						elf.setCanShoot(true);
						}
					//remove the line to indicate to the user that they can shoot again
					clearLine();
				 }
			}
		}
	}
	
	
	/**
	 * The class that handles how long it takes before the screen refreshes,  in a new thread
	 */
	class screenRefreshHandler extends Thread{
		/**
		 * Refresh the screen 60 times a second
		 */
		public void runScreenRefesher() {
			//This line of code is nessary to avoid a synchronization error
			synchronized(this) {
				while(true) {
					//try catch is nessary for the wait method
					try {
						wait(1000/60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//redraw the screen
					repaint();
				}
			}

		}
		
		/**
		 * Refresh the screen every some amount of milliseconds
		 * Not used currently but can be added for debugging
		 * @param waitTimeMs The amount of time that the refresh handler will wait before refreshing the screen 
		 */
		public void runScreenRefesher(int waitTimeMs) {
			//This line of code is nessary to avoid a synchronization error
			synchronized(this) {
				while(true) {
					//try catch is nessary for the wait method
					try {
						wait(waitTimeMs);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//redraw the screen
					repaint();
				}
			}
		}
		public void run() {
			runScreenRefesher();
		}
	}



}	
