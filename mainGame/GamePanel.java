package mainGame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable, KeyListener {
	//Fields
	public static int WIDTH = 400;
	public static int HEIGHT = 400;
	
	private Thread thread;
	boolean running;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 30;
	private double averageFPS;
	
	private double scoreMultiplier = 1;
	private double score;
	
	public static Player player;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Life> lives;
	//Constructor
	
	private int spawnAmount = 3;
	public int spawnLevel = 1;
	private int numLives = 2;
	
	
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	//Functions
	public void addNotify() {
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	public void run() {
		
		running = true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		player = new Player();
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		lives = new ArrayList<Life>();
		
		for(int i = 0; i < 3; i++){
			enemies.add(new Enemy(1 , 1));
		}
		
		for(int i = 0; i <= numLives; i++){
			lives.add(new Life(i));
		}
		
		score = 0;
		
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		
		int frameCount = 0;
		int maxFrameCount = 30;
		
		long targetTime = 1000 / FPS;
		
		//Game Loop
		while(running){
			
			startTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			
			waitTime = targetTime - URDTimeMillis;
			
			try{
				Thread.sleep(waitTime);
			}
			catch(Exception e){
			}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if(frameCount == maxFrameCount){
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
			

		}
		
	}

	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	private void gameRender() {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		//g.drawString("FPS: " + averageFPS, 10, 10);
		g.drawString("Multiplier: " + round(scoreMultiplier, 1), 10, 10);
		//g.drawString("Number of bullets: " + bullets.size(), 10, 25);
		g.drawString("Score: " + ((int) score), 10, 25);
		
		if(numLives < 0){
			g.drawString("GAME OVER", 10, 40);
		}
		//draw Player
		
		if(player.isAlive()){
			player.draw(g);
		}
		
		//draw bullets;
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(g);
		}
		//draw enemies
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		for(int i = 0; i < lives.size(); i++){
			lives.get(i).draw(g);
		}
		
		
	}

	private void gameUpdate() {
		
		//Create Enemies.
		
		
		//Level increase
		if(enemies.size() < 3){
			scoreMultiplier += 0.3;
			spawnAmount++;
			if(spawnAmount % 5 == 1){
				spawnLevel++;
				if(spawnAmount > 14){
					spawnAmount = 3;
				}
			}
			for(int i = 0; i < spawnAmount; i++){
				enemies.add(new Enemy(spawnLevel, spawnLevel));
			}
			
		}
		
		//player update
		if(player.isAlive()){
			player.update();
		}
		//bullet update
		for(int i = 0; i < bullets.size(); i++){
			boolean remove = bullets.get(i).update();
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		//enemy update
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).update();
		}
		
		//bullet-enemy collision
		for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			
			
			for(int j = 0; j < enemies.size(); j++){
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if(dist < br + er){
					e.hit();
					bullets.remove(i);
					i--;
					break;
				}
				
			}
			
		}
		
		// enemy-player collision
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			double ex = e.getx();
			double ey = e.gety();
			double er = e.getr();
			
			Player p = player;
			
			double px = p.getx();
			double py = p.gety();
			double pr = p.getr();
			
			double dx = ex - px;
			double dy = ey - py;
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			
			if(dist < pr + er && player.isAlive()){
				enemies.remove(i);
				if(numLives >= 0){
					scoreMultiplier = 1;
					lives.remove(numLives);
					numLives--;
					if(numLives < 0){
						player.isDead(true);
					}
				}
			}
		}
		
		//check dead enemies
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).isDead()){
				score = score + enemies.get(i).getScore() * 35 * scoreMultiplier;
				enemies.remove(i);
				i--;
			}
		}
	}

	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if(keyCode == KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(keyCode == KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(keyCode == KeyEvent.VK_DOWN){
			player.setDown(true);
		}
		if(keyCode == KeyEvent.VK_UP){
			player.setUp(true);
		}
		if(keyCode == KeyEvent.VK_Z){
			player.setFiring(true);
		}
	}

	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if(keyCode == KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(keyCode == KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(keyCode == KeyEvent.VK_DOWN){
			player.setDown(false);
		}
		if(keyCode == KeyEvent.VK_UP){
			player.setUp(false);
		}
		if(keyCode == KeyEvent.VK_Z){
			player.setFiring(false);
		}
		
	}

	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub
		
	}
	// round code copied from 
	// http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	// Thanks Jonik.
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
