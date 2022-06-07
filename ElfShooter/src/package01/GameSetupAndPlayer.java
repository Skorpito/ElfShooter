package package01;

import java.awt.Graphics2D;
import java.awt.Component;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameSetupAndPlayer extends JPanel {
	private final int GAME_WIDTH = 1040;
	private final int GAME_HEIGHT = 720;

	private ArrayList<Drawables> baseSprites = new ArrayList<Drawables>();
	private ArrayList<Dwarf> dwarfList = new ArrayList<Dwarf>();
	private JLabel statusbar;
	private Elf elf;

	// private JFrame testFrame =new JFrame("JFrame Color Example");

	public GameSetupAndPlayer(JLabel statusbar) {
		this.setSize(GAME_WIDTH, GAME_HEIGHT);

		this.statusbar = statusbar;

		elf = new Elf();
		newGame();
		
		
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
	
	/**
	 * Contains the basic information needed for the game
	 * 
	 */
	private void newGame() {
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		addMouseListener(new CardListener());
		Drawables.setGame(this);
		
		
		baseSprites.add(new Background());
		baseSprites.add(new Elf());
		dwarfList.add(new Dwarf(0,0,0,1020));
		dwarfList.add(new Dwarf(600,0,600,1020));
		repaint();
		
		
	}

	/**
	 * 
	 * okay this is a son of a female dog This is what is executed whenever you want
	 * to paint something Can not have multiple seperate methods for separate things
	 * Thus we go through the drawables arraylist and paint each one of those
	 * instead to modify what is painted modify that
	 */

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// draw base sprites
		Graphics2D g2d = (Graphics2D) g.create();
		for (Drawables drawable : baseSprites) {
			drawable.paint(g2d);
		}

		// draw all the dwarfs
		for (Drawables dwarf : dwarfList) {
			dwarf.paint(g2d);
		}
		g2d.dispose();
		// drawables=new ArrayList<Drawables>();
	}

	/**
	 * user interaction, runs in separate thread
	 * @author 2201690
	 *
	 */
	private class CardListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			if (elf.canShoot()) {
				elf.shoot(x, y);

			}

			repaint();
			System.out.println("PRESSED");
		}
	}
	
	
	/**
	 * 
	 * @author 2201690
	 *
	 */
	class screenRefreshHandler extends Thread{
		/**
		 * Refresh the screen 60 times a second
		 */
		public void runScreenRefesher() {
			synchronized(this) {
				while(true) {
					try {
						wait(1000/60);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					repaint();
				}
			}

		}
		
		/**
		 * Refresh the screen every some amount of milliseconds
		 * @param waitTimeMs The amount of time that the refresh handler will wait before refreshing the screen 
		 */
		public void runScreenRefesher(int waitTimeMs) {
			synchronized(this) {
				while(true) {
					try {
						wait(waitTimeMs);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					repaint();
				}
			}
		}
		public void run() {
			runScreenRefesher();
		}
	}
	
}