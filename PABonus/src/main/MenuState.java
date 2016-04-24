package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MenuState extends GameState {
	
	private ArrayList<RandomBrain> brains;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	private int frameUnit;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		this.frameUnit = 0;
		
		try {
			
			brains = new ArrayList<RandomBrain>();
			
			titleColor = Color.white;
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					108);
			
			font = new Font("Arial", Font.PLAIN, 48);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {
		for(int i = 0; i < 3000; i++) {
			update();
		}
	}
	
	public void update() {
		
		//generate random arrivals:
		boolean brainArrival = Math.random() < .13;
		if(brainArrival) {
			brains.add(new RandomBrain());
		}
		
		//update brains
		for(RandomBrain brain : brains) {
			brain.update();
		}
		
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
		for(RandomBrain brain : brains) {
			if(frameUnit < 240 || brain.index < sizeThreshold) brain.draw(g);
		}
		
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
		g.drawString(Game.GAME_NAME, 320, 280);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 580, 560 + i * 60);
		}
		
		//draw brains
		if(frameUnit >= 240) {
			for(RandomBrain brain : brains) {
				if(brain.index >= sizeThreshold) brain.draw(g);
			}
		}
		
		
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.GAMESTATE);
		}
		if(currentChoice == 1) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
}










