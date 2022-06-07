package package01;

import java.awt.Graphics2D;
//import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Image;


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
	private int bowXLoc = 800;
	private int bowYLoc = 100;
	
	public boolean canShoot() {
		return canShoot;
	}

	public void shoot(int x, int y) {
		ArrayList<Integer> removedDwarfs = new ArrayList<Integer>();
		ArrayList<Dwarf> dwarfList = Dwarf.getDwarfList();
		
		for(int i=0;i<dwarfList.size();i++) {
			 
		}
	}
	
	public int calcVertIntersect(int x, int y, int lineLoc) {
		int m = ( (bowYLoc-y)/() );
		int b = ()
		return m*
	}
}
}


/**
 *
 */
class Dwarf extends Drawables {
	protected int xLoc;
	protected int yLoc;
	protected int xEnterLoc;
	protected int yEnterLoc;
	protected int xExitLoc;
	protected int yExitLoc;
	protected int dwarfWidth;
	protected int dwarfHeight;
	
	private static ArrayList <Dwarf> dwarfList = new ArrayList <Dwarf>();
	private DwarfMovement m;
	public Dwarf () {
		m = new DwarfMovement();
		m.start();
		dwarfList.add(this);
		ID = IDAt;
		IDAt++;
	}
	
	public void setDwarfList(ArrayList<Dwarf> newDwarfList) {
		dwarfList = newDwarfList;
	}
	public ArrayList<Dwarf> getDwarfList(){
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
	class DwarfMovement extends Thread {
		public void run () {
			while (true) {
				try {
					wait(100);
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (xLoc == 1023) {
					xLoc=0;
				}
				else { 
					xLoc+=1;		
				}
			}
		}
	}
}

class Background extends Drawables{
	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.GREEN);
		g2d.fillRect(0, 0, Game.getWidth(), Game.getHeight());
		g2d.setColor(Color.GRAY);
		g2d.fillRect(Game.getWidth()-240, 0, Game.getWidth(), Game.getHeight());
		g2d.fillRect(0, 300, Game.getWidth(), 20);
	}
}
