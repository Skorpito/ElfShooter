package package01;

import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Image;
import java.awt.BasicStroke;

/**
 * This class is the superclass to all of the things can be drawn
 * can probably be better done in an abstract class but idk
 * to draw something new just make sure it has the paint() method
 *
 */
public class Drawables {
	protected static GameSetupAndPlayer Game;
	
	public static void setGame(GameSetupAndPlayer newGame) {
		Game=newGame;
	}
	
	/**
	 * Default paint method, should be overwritten
	 * Everytime an object is drawn this method is called
	 * @param g2d the graphics object that is being written on
	 */
	public void paint(Graphics2D g2d) {
		System.out.println("PLEASE OVERRIDE THIS");
	}
}

/**
 * Elf class, handles shooting 
 */
class Elf extends Drawables{
	private static int bowXLoc = 871;
	private static int bowYLoc = 243;
	private static boolean canShoot=true;
	private static int numKilled=0;
	private static int streak=0;
	
	public void setCanShoot(boolean newStatus) {
		canShoot=newStatus;
	}
	
	public boolean getCanShoot() {
		return canShoot;
	}

	public void paint(Graphics2D g2d) {
		 //elf is never drawn
	}
	
	//getters and setters
	public static int getBowX() {
		return bowXLoc;
	}
	public static int getBoyY() {
		return bowYLoc;
	}
	
	/**
	 * handles shooting the dwarfs, respawns them if killed 
	 * @param x X location of where the user clicked
	 * @param y Y location of where the user clicked
	 */
	public void shoot(int x, int y) {
		ArrayList<Integer> removedDwarfs = new ArrayList<Integer>();
		ArrayList<Dwarf> dwarfList = Game.getDwarfList();
		
		int numHit=0;
		//calculates which dwarfs need to be killed 
		for(int i=0;i<dwarfList.size();i++) {
			Dwarf dwarf=dwarfList.get(i);

			//checks to see if the endpoint is inside the hitbox
			if(dwarf.getXLoc()<=x && x<=dwarf.getXLoc()+dwarf.getDwarfHeight() && // between left and right parts
					dwarf.getYLoc()<= y && y<=dwarf.getYLoc()+dwarf.getDwarfHeight()) { 
				System.out.println("HIT");
				removedDwarfs.add(i);
			
				//if so incriment the number of dwarfs hit and increase that ones speed
				numHit++;
				dwarf.speedUp();
				
			} else {
				System.out.println("MISS");
			}
		}
		
		//for the index of each removed the dwarf create a copy of the removed dwarf and then remove the original
		//Note goes in reverse order to prevent the wrong inex from being hit
		for(int i= removedDwarfs.size()-1; i>=0; i--) {
			dwarfList.add(new Dwarf(dwarfList.get(removedDwarfs.get(i))));
			dwarfList.remove((int)removedDwarfs.get(i));
		}
		
		Game.setDwarfList(dwarfList);
		
		//handels the display on the bottom of the string
		if(numHit==0) {
			streak=0;
			Dwarf.resetAllSpeed();
		} else {
			streak+=numHit;
			numKilled+=numHit;
			if(Game.getMaxStreak()<streak) Game.setMaxStreak(streak);
		}
		
		//set the text of the dwarf to reflect how the user is doing
		Game.getStatusbar().setText("Dwarfs Killed: " + (numKilled)
				+"         Streak is " + (streak) + " dwarfs "
				+"         Max Streak is " + Game.getMaxStreak() + " dwarfs");
		

	}
}


/**
 * Handles the dwarfs
 */
class Dwarf extends Drawables {
	private int xLoc;
	private int yLoc;
	
	private int xEnterLoc;
	private int yPathHeight;
	
	protected int xExitLoc;
	
	private int dwarfWidth;
	private int dwarfHeight;
	
	private int baseDwarfSpeed=4;
	private int dwarfSpeed;
	private DwarfMovement m;
	private Image img;
	
	
	/**
	 * Creates a dwarfs with predefined features
	 * @param pathHeight the height that the dwarf will stroll at
	 * @param xEnter where the dwarf will respawn at
	 * @param xExit where the dwarf will exit and return to the respawn point at
	 */
	public Dwarf(int pathHeight, int xEnter, int xExit) {
		//load the image through the path
		String path = "src/images/dwarf.png";
		img=(new ImageIcon(path)).getImage();
		
		//set the dwarfs to have the attributes of the image
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		//define local variables from differing parameters
		dwarfSpeed=baseDwarfSpeed;
		
		yPathHeight=pathHeight;
		
		xEnterLoc=xEnter-dwarfWidth;
		xExitLoc=xExit;
		
		xLoc=xEnterLoc;
		yLoc=yPathHeight;
		
		//Start the thread that handles dwarf movement
		m = new DwarfMovement();
		m.start();
	}


	/**
	 * Creates a copy of a dwarf except that it is at the enter point
	 * @param dwarf The dwarf that is being  
	 */
	public Dwarf(Dwarf dwarf) {
		img=dwarf.getImg();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		xEnterLoc=dwarf.getXEnterLoc();
		xExitLoc=dwarf.getXExitLoc();
		
		yPathHeight=dwarf.getPathY();
		
		xLoc=xEnterLoc;
		yLoc=yPathHeight;
		
		dwarfSpeed=dwarf.getSpeed();
		
		//Start the thread that handles dwarf movement

		m = new DwarfMovement();
		m.start();
	}

	//getters and setters
	public Image getImg() {
		return this.img;
	}
	
	public int getXLoc () {
		return (xLoc);
	}
	public int getYLoc () {
		return (yLoc);
	}
	public int getXEnterLoc () {
		return (xEnterLoc);
	}
	public int getPathY () {
		return (yPathHeight);
	}
	public int getXExitLoc () {
		return (xExitLoc);
	}

	public int getDwarfWidth () {
		return (dwarfWidth);
	}
	public int getDwarfHeight () {
		return (dwarfHeight);
	}
	
	//methods that handle the speed of the dwarf
	public void speedUp() {
		dwarfSpeed++;
	}
	public void resetSpeed() {
		dwarfSpeed=baseDwarfSpeed;
	}
	public int getSpeed() {
		return dwarfSpeed;
	}
	
	/**
	 * Resets the speed of all of the dwarfs 
	 */
	public static void resetAllSpeed() {
		ArrayList<Dwarf> dwarfList=Game.getDwarfList();
		for(Dwarf d : dwarfList) {
			d.resetSpeed();
		}
		Game.setDwarfList(dwarfList);
	}
	
	/**
	 * Draw the dwarf using the image loaded in the constructor
	 */
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,xLoc,yLoc,null); //having an image observer object is not nessary so thus it is null
	}
	
	
	
	/**
	 * Class that handles the dwarf moving to the right
	 *
	 */
	class DwarfMovement extends Thread {

		
		/**
		 * Delay until the dwarf moves
		 */
		private final int moveDelayMs=16;
		
		public void run () {
			//no memory overflow as when the dwarf is killed this is stopped automatically, 
			//no reason to stop it mid-game so it just runs continuously 
			while(true) {
				xLoc=xLoc+dwarfSpeed;
				if(xLoc>=xExitLoc) {
					xLoc=xEnterLoc;
				}
				
				//try and catch is nessary for the sleep method
				try {
					sleep(moveDelayMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


/**
 * Object that is the background
 */
class Background extends Drawables{
	private Image img;
	public Background () {
		//load the image through the path
		String path = "src/images/background.png";
		img=(new ImageIcon(path)).getImage();
}
	//draw the image
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,0,0,null); //having an image observer object is not nessary so thus it is null
	}
		
}


/**
 * Class that handles the arrow fired by the bow
 *
 */
class Line extends Drawables{
	private final int xStarting = Elf.getBowX();
	private final int yStarting = Elf.getBoyY();
	
	private int x;
	private int y;
	private boolean isEmpty;
	
	//this is only called when the user shoots an arrow thus it needs to be drawn always thus the isEmpty=false
	public Line(int x, int y) {
		this.x=x;
		this.y=y;
		
		isEmpty=false;
	}
	
	//When the Elf is created there is a line obj associated with it. We need it to be invisible (AKA EMPTY) as the user has not fired an arrow 
	public Line() {
		isEmpty=true;
		}

	public void paint(Graphics2D g2d) {
		//if not empty draw the line
		if(!isEmpty) {
			g2d.setStroke(new BasicStroke(3)); //line with thickness three
			g2d.setColor(Color.RED);		//make it red
			g2d.drawLine(xStarting, yStarting, x, y); //from the starting point to the ending point
		}
	}
}
