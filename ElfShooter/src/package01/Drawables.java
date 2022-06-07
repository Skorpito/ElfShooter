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
	private static boolean canShoot=true;
	private static int bowXLoc = 800;
	private static int bowYLoc = 100;
	
	public boolean canShoot() {
		return canShoot;
	}

	public void paint(Graphics2D g2d) {
		 
	}

	public void shoot(int x, int y) {
		ArrayList<Integer> removedDwarfs = new ArrayList<Integer>();
		ArrayList<Dwarf> dwarfList = Dwarf.getDwarfList();
		
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
		
		for(int i= dwarfList.size()-1; i>=0; i--) {
			dwarfList.add(dwarfList.get(i));
			dwarfList.remove(i);
		}
	}
	/**
	 * Calculates where the line given by the user intesects with a veritcal line 
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
}


/**
 *
 */

class Dwarf extends Drawables {
	protected int xLoc;
	protected int yLoc;
	
	private int xEnterLoc;
	private int yEnterLoc;
	
	protected int xExitLoc;
	protected int yExitLoc;
	
	private int dwarfWidth=128;
	private int dwarfHeight=128;
	
	private static ArrayList <Dwarf> dwarfList = new ArrayList <Dwarf>();
	private DwarfMovement m;
	private Image img;
	
	public Dwarf(int xEnter, int yEnter, int xExit, int yExit) {
		xEnterLoc=xEnter;
		yEnterLoc=yEnter;
		
		xExitLoc=xExit;
		yExitLoc=yExit;
	}
	
	
	public Dwarf () {
		String path = "src/images/dwarf.png";
		img=(new ImageIcon(path)).getImage();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		//m = new DwarfMovement();
		//m.start();
		//dwarfList.add(this);
	}
	
	
	
	public Dwarf(Dwarf dwarf) {
		String path = "src/images/dwarf.png";
		img=(new ImageIcon(path)).getImage();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		xEnterLoc=dwarf.getXEnterLoc();
		yEnterLoc=dwarf.getYEnterLoc();
		
		xExitLoc=dwarf.getXExitLoc();
		yExitLoc=dwarf.getYExitLoc();
	}
	
	public void setDwarfList(ArrayList<Dwarf> newDwarfList) {
		dwarfList = newDwarfList;
	}
	public static ArrayList<Dwarf> getDwarfList(){
		return dwarfList;
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
	public int getYEnterLoc () {
		return (yEnterLoc);
	}
	public int getXExitLoc () {
		return (xExitLoc);
	}
	public int getYExitLoc () {
		return (yExitLoc);
	}
	public int getDwarfWidth () {
		return (dwarfWidth);
	}
	public int getDwarfHeight () {
		return (dwarfHeight);
	}
	
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,20,20,null);
	}
	
	
	class DwarfMovement extends Thread {
		public void run () {
			
		}
	}
}

/**
*
*/
class Background extends Drawables{
	private Image img;
	public Background () {
		String path = "src/images/background.png";
		img=(new ImageIcon(path)).getImage();
}
	public void paint(Graphics2D g2d) {
		g2d.drawImage(img,20,20,null);
	}
		
}