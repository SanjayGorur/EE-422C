/** MenuState ********************************************
 * Manages the current state of the GUI menu
 * Contains the logic for menu interfacing
 * 
 * Section : F 2:00 - 3:30pm
 * UT EID: cdr2678 ,rpm953
 * @author Cooper Raterink, Ronald Macmaster
 * @version 1.01 4/25/2016
 ************************************************************/
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class MenuState extends GameState {
	
	/** initialization flag */
	private boolean hasBeenInitialized = false;
	/** list of brains to manage / draw */
	private ArrayList<RandomBrain> brains;
	
	/** threshold constant for brain arrival */
	private double brainArrivalThreshold = .13;

	/** current menu choice */
	private int currentChoice = 0;
	
	/** menu options */
	private String[] options = {
		"Play",
		"Settings",
		"Quit"
	};
	
	/** Font color for title */
	private Color titleColor;
	/** Font object for title */
	private Font titleFont;
	/** Font object for menu options*/
	private Font optionFont;
	
	/** animation unit for brains */
	private int frameUnit;
	
	/** collision detection menu rectangles */
	private ArrayList<Rectangle2D> optionRects;
	
	/** generate the menu state initially */
	public MenuState(GameStateManager gsm) {
		// initialize the state variablea
		this.gsm = gsm;
		this.frameUnit = 0;
		try {
			// brain list
			brains = new ArrayList<RandomBrain>();
			
			// title init
			titleColor = Color.white;
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					27*GamePanel.HEIGHT/320);
			// menu options init
			optionFont = new Font("Arial", Font.PLAIN, 4*GamePanel.HEIGHT/80);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * initialize the brain animations
	 */
	public void init(GameState lastState) {
		if (!hasBeenInitialized) {
			for (int i = 0; i < 3000; i++) {
				update();
			}
			hasBeenInitialized = true;
		}
	}
	
	/**
	 * generate and clean random brains
	 */
	public void update() {
		
		//generate random arrivals:
		boolean brainArrival = Math.random() < brainArrivalThreshold;
		if(brainArrival) {
			brains.add(new RandomBrain());
		}
		
		//update brains
		for(int i = 0; i < brains.size(); i++) {
			brains.get(i).update();
		}
		
		//remove brains that go off the screen
		for(int i = 0; i < brains.size(); i++) {
			if(brains.get(i).x > GamePanel.WIDTH) {
				brains.remove(i);
			}
		}
	}
	
	public void draw(Graphics2D g) {
		//draw bg
		g.setColor(Color.black);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		//draw brains
		int sizeThreshold = 30;
		for(int i = 0; i < brains.size(); i++) {
			if(frameUnit < 240 || brains.get(i).index < sizeThreshold) brains.get(i).draw(g);
		}
		
		// animate brains
		if(frameUnit < 240) {
			if(frameUnit > 120) {
				Composite comp = g.getComposite();
				float alpha = (float)((240 - frameUnit) * (0.1/12.0));
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			    g.fillRect(0,0,GamePanel.WIDTH, GamePanel.HEIGHT);
			    g.setComposite(comp);
			}
			else {
				g.fillRect(0,0,GamePanel.WIDTH, GamePanel.HEIGHT);
			}
			frameUnit++;
		}
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		FontMetrics metrics = g.getFontMetrics(titleFont);
		int titleY = 7 * GamePanel.HEIGHT / 24;
		int titleX = (GamePanel.WIDTH - metrics.stringWidth(Game.GAME_NAME)) / 2;
		g.drawString(Game.GAME_NAME, titleX, titleY);
		
		// draw menu options
		g.setFont(optionFont);
		int optionsYStart = GamePanel.HEIGHT * 10 / 20;
		int optionsYDelta = GamePanel.HEIGHT * 4 / 40;
		int optionsX = GamePanel.WIDTH * 9 / 20;
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], optionsX, optionsYStart + i * optionsYDelta);
		}
		
		//draw brains
		if(frameUnit >= 240) {
			for(int i = 0; i < brains.size(); i++) {
				RandomBrain brain = brains.get(i);
				if(brain.index >= sizeThreshold) brain.draw(g);
			}
		}
		
		//set up text dimensions
		if(optionRects == null) {
			optionRects = new ArrayList<Rectangle2D>();
			int optIndex = 0;
			for(String option : options) {
				FontMetrics optMetrics = g.getFontMetrics(optionFont);
				int hgt = optMetrics.getHeight();
				int width = optMetrics.stringWidth(option);
				Rectangle2D.Double rect = new Rectangle2D.Double();
				rect.setRect(optionsX - 5, optionsYStart + optionsYDelta * optIndex - hgt - 5, width + 10, hgt + 10);
				optionRects.add(rect);
				optIndex++;
			}
		}
		
	}
	
	/**
	 * select the new game state
	 */
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.GAMESTATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.SETTINGSSTATE);
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	/**
	 * Arrow key handler
	 * switch the selection / select
	 */
	public void keyPressed(int k) {
		// submit selection
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		// up arrow selection
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		// down arrow selection
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	public void keyReleased(int k) {}

	public void keyTyped(char key){}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (optionRects != null) {
			//check all option boxes
			int rectIndex = 0;
			for (Rectangle2D rect : optionRects) {
				if(rect.contains(me.getPoint())) {
					currentChoice = rectIndex;
					select();
				}
				rectIndex++;
			} 
		}
	}

}