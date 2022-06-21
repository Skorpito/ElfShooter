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
	private Elf elf = new Elf();
	private Background b = new Background();
	private Line line = new Line();

	private int maxStreak=0;

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
	public void clearLine() {
		line= new Line();
	}
	
	public JLabel getStatusbar() {
		return statusbar;
	}
	
	
	public int getMaxStreak() {
		return maxStreak;
	}

	public void setMaxStreak(int maxStreak) {
		this.maxStreak = maxStreak;
	}

	/**
	 * Contains the basic information needed for the game
	 * 
	 */
	private void newGame() {
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		addMouseListener(new CardListener());
		Drawables.setGame(this);
		
		
		dwarfList.add(new Dwarf(50,0,440));
		dwarfList.add(new Dwarf(GAME_HEIGHT-143-60,0,GAME_WIDTH));
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
		
		b.paint(g2d);
		elf.paint(g2d);
	
		
		// draw all the dwarfs
		for (Drawables dwarf : dwarfList) {
			dwarf.paint(g2d);
		}
		
		//will be null until
		line.paint(g2d);
		
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

			if (elf.getCanShoot()) {
				System.out.println(x +", " + y);
				line =new Line(x,y);
				
				elf.shoot(x, y);
				elf.setCanShoot(false);
				
				RefreshHandler r= new RefreshHandler();
				r.start();
			}

			repaint();
			System.out.println("PRESSED");
		}
		
		protected class RefreshHandler extends Thread{
			private final int refreshTimeMs=500;
			public void run () {
				 synchronized(this){
					try {wait(refreshTimeMs);}
					catch (InterruptedException e) {e.printStackTrace();}
					
					if(!elf.getCanShoot()) {
						elf.setCanShoot(true);
						}
					clearLine();
				 }
			}
		}
	}
	
	
	/**
	 * 
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