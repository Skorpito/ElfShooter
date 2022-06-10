package package01;

import java.awt.Graphics2D;
//import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Image;
import java.awt.BasicStroke;
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
	private static int bowXLoc = 871;
	private static int bowYLoc = 243;
	private static boolean canShoot=true;
	private static int numKilled=0;
	private static int streak=0;

	public Elf() {

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
		
		int numHit=0;
		//calculates which dwarfs need to be killed 
		for(int i=0;i<dwarfList.size();i++) {
			Dwarf dwarf=dwarfList.get(i);

			//checks to see if the endpoint is inside the hitbox
			if(dwarf.getXLoc()<=x && x<=dwarf.getXLoc()+dwarf.getDwarfHeight() && // between left and right parts
					dwarf.getYLoc()<= y && y<=dwarf.getYLoc()+dwarf.getDwarfHeight()) {
				System.out.println("HIT");
				removedDwarfs.add(i);
			
				numHit++;
				dwarf.speedUp();
				
			} else {
				System.out.println("MISS");
			}
		}
		
		for(int i= removedDwarfs.size()-1; i>=0; i--) {
			dwarfList.add(new Dwarf(dwarfList.get(removedDwarfs.get(i))));
			dwarfList.remove((int)removedDwarfs.get(i));
		}
		
		Game.setDwarfList(dwarfList);
		
		if(numHit==0) {
			streak=0;
			Dwarf.resetAllSpeed();
		} else {
			streak+=numHit;
			numKilled+=numHit;
			if(Game.getMaxStreak()<streak) Game.setMaxStreak(streak);
		}
		
		Game.getStatusbar().setText("Dwarfs Killed: " + (numKilled)
				+"         Streak is " + (streak) + " dwarfs "
				+"         Max Streak is " + Game.getMaxStreak() + " dwarfs");
		

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
	
	
	
	public Dwarf(int pathHeight, int xEnter, int xExit) {
		String path = "src/images/dwarf.png";
		img=(new ImageIcon(path)).getImage();
		dwarfHeight=img.getHeight(null);
		dwarfWidth=img.getWidth(null);
		
		dwarfSpeed=baseDwarfSpeed;
		
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
		
		yPathHeight=dwarf.getPathY();
		
		xLoc=xEnterLoc;
		yLoc=yPathHeight;
		
		dwarfSpeed=dwarf.getSpeed();
		
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
	
	public void speedUp() {
		dwarfSpeed++;
	}
	public void resetSpeed() {
		dwarfSpeed=baseDwarfSpeed;
	}
	public int getSpeed() {
		return dwarfSpeed;
	}
	public static void resetAllSpeed() {
		ArrayList<Dwarf> dwarfList=Game.getDwarfList();
		for(Dwarf d : dwarfList) {
			d.resetSpeed();
		}
		Game.setDwarfList(dwarfList);
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
		 * Delay until the dwarf moves
		 */
		private final int moveDelayMs=16;
		
		public void run () {
			while(true) {
				xLoc=xLoc+dwarfSpeed;
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
 *
 */
class Line extends Drawables{
	private final int xStarting = Elf.getBowX();
	private final int yStarting = Elf.getBoyY();
	
	private int x;
	private int y;
	private boolean isEmpty;
	
	public Line(int x, int y) {
		this.x=x;
		this.y=y;
		
		isEmpty=false;
	}
	
	public Line() {
		isEmpty=true;
		}

	public void paint(Graphics2D g2d) {
		if(!isEmpty) {
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.RED);
			g2d.drawLine(xStarting, yStarting, x, y);
		}
	}
}