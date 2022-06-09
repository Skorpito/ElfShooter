package package01;

import java.awt.Graphics2D;
//import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Image;
import java.lang.Math;

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
	
	public void paint(Graphics2D g2d) {
		
	}
}

/**
 * 
 */
class Elf extends Drawables{
	private static int delay=250;
	private static int bowXLoc = 800;
	private static int bowYLoc = 100;
	private static RefreshHandler r;
	private static boolean canShoot=true;

	public Elf() {
		r= new RefreshHandler();
		r.start();
	}
	
	public void setCanShoot(boolean newStatus) {
		canShoot=newStatus;
	}
	
	public boolean getCanShoot() {
		return canShoot;
	}

	public void paint(Graphics2D g2d) {
		 
	}
	
	public static int getBowX() {
		return bowXLoc;
	}
	public static int getBoyY() {
		return bowYLoc;
	}
	
	public void shoot(int x, int y) {
		ArrayList<Integer> removedDwarfs = new ArrayList<Integer>();
		ArrayList<Dwarf> dwarfList = Game.getDwarfList();
		
		//calculates which dwarfs need to be killed 
		for(int i=0;i<dwarfList.size();i++) {
			Dwarf dwarf=dwarfList.get(i);
			 if(x==bowXLoc) { //to avoid issues with divide by zero
				 if(dwarf.getXLoc()<=x && x<=dwarf.getXLoc()+dwarf.getDwarfWidth() && //between the x's of the hitbox 
					y<=dwarf.getYLoc()) { //assuming that the dwarf is below the elf
				System.out.println("HIT");
				removedDwarfs.add(i);
				 }
			 }
			 else {
				//checks to see if it intersects the line for any of the dwarf's hitboxes
				 
				if(dwarf.getYLoc()<=calcVertIntersect(x, y, dwarf.getXLoc()) &&
					calcVertIntersect(x, y, dwarf.getXLoc()) <= dwarf.getYLoc()+dwarf.getDwarfHeight()
						){
					removedDwarfs.add(i);
					System.out.println("HIT");
				} else if(dwarf.getYLoc()<=calcVertIntersect(x, y, dwarf.getXLoc()+dwarf.getDwarfWidth()) &&
					calcVertIntersect(x, y, dwarf.getXLoc()+dwarf.getDwarfWidth()) <= dwarf.getYLoc()+dwarf.getDwarfHeight()
						){
					removedDwarfs.add(i);
					System.out.println("HIT");
					
				} else if(dwarf.getXLoc()<=calcHorizontalIntersect(x, y, dwarf.getYLoc()) &&
						calcHorizontalIntersect(x, y, dwarf.getYLoc())<=dwarf.getYLoc()+dwarf.getDwarfHeight()) {
					removedDwarfs.add(i);
					System.out.println("HIT");
				} else if(dwarf.getXLoc()<=calcHorizontalIntersect(x, y, dwarf.getYLoc()+dwarf.getDwarfHeight()) &&
						calcHorizontalIntersect(x, y, dwarf.getYLoc()+dwarf.getDwarfHeight())<=dwarf.getYLoc()+dwarf.getDwarfHeight()) {
					removedDwarfs.add(i);
					System.out.println("HIT");
				} else {
					System.out.println("MISS");
				}
			 }
		}
		
		
		for(int i= removedDwarfs.size()-1; i>=0; i--) {
			dwarfList.add(new Dwarf(dwarfList.get(removedDwarfs.get(i))));
			dwarfList.remove((int)removedDwarfs.get(i));
		}
		
		Game.setDwarfList(dwarfList);
	}
	
	/**
	 * Calculates where the line given by the user intersects with a vertical line 
	 * @param x 
	 * @param y
	 * @param lineLoc
	 * @return Returns the vertical height of where the lines intersect
	 */
	public int calcVertIntersect(int x, int y, int lineLoc) {
		double m = ( (bowYLoc-y)/(bowXLoc-x) );
		double b = (y-m*x);
		return (int) (m*lineLoc + b);
	}
	public int calcHorizontalIntersect(int x, int y, int lineLoc) {
		double m = ( ((double)bowYLoc-y)/(bowXLoc-x) );
		double b = (y-m*x);
		return (int) ((lineLoc-b)/m);
	}
	
	/**
	 * Class that deals with removing the line and making sure that the user can't shoot rapidly
	 */
	protected class RefreshHandler extends Thread{
		private final int refreshTimeMs=200;
		public void run () {
			while (true) {
				try {wait(refreshTimeMs);}
				catch (InterruptedException e) {e.printStackTrace();}
				
				if(!getCanShoot()) {
					setCanShoot(true);
				}
			}		
		}
	}
}


/**
 *
 *
 *
 *
 *
 *
 */
class Dwarf extends Drawables {
	protected int xLoc;
	protected int yLoc;
	
	private int xEnterLoc;
	private int yPathHeight;
	
	protected int xExitLoc;
	
	private int dwarfWidth;
	private int dwarfHeight;
	
	private DwarfMovement m;
	private Image img;
	
	
	
	public Dwarf(int pathHeight, int xEnter, int xExit) {
		String path = "src/images/dwarf.png";
		img=(new ImageIcon(path)).getImage();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		
		yPathHeight=pathHeight;
		
		xEnterLoc=xEnter-dwarfWidth;
		xExitLoc=xExit;
		
		xLoc=xEnterLoc;
		yLoc=yPathHeight;
		
		m = new DwarfMovement();
		m.start();
	}
	
	
	public Dwarf(Dwarf dwarf) {
		img=dwarf.getImg();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		xEnterLoc=dwarf.getXEnterLoc();
		xExitLoc=dwarf.getXExitLoc();
		
		yPathHeight=dwarf.getY();
		
		xLoc=xEnterLoc;
		yLoc=yPathHeight;
		
		m = new DwarfMovement();
		m.start();
	}

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
	public int getY () {
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
	
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,xLoc,yLoc,null);
	}
	
	
	
	/**
	 * Class that handles the dwarf moving to the right
	 * 
	 * @author ryanh
	 *
	 */
	class DwarfMovement extends Thread {
		/**
		 * The amount that the dwarf moves per the delay
		 */
		private final int movePixels=4;
		
		/**
		 * Delay until the dwarf moves
		 */
		private final int moveDelayMs=20;
		
		public void run () {
			while(true) {
				xLoc=xLoc+movePixels;
				if(xLoc>=xExitLoc) {
					xLoc=xEnterLoc;
				}
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
 * 
 * 
 *
 */
class Background extends Drawables{
	private Image img;
	public Background () {
		String path = "src/images/background.png";
		img=(new ImageIcon(path)).getImage();
}
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,0,0,null);
	}
		
}


/**
 * Class that handles the arrow fired by the bow
 * @author ryanh
 *
 */
class Line extends Drawables{
	private final int xStarting = Elf.getBowX();
	private final int yStarting = Elf.getBoyY();
	
	private int x;
	private int y;

	public Line(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void paint(Graphics2D g2d) {
		g2d.drawLine(xStarting, yStarting, x, y);
	}
}