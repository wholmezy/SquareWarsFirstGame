package mainGame;
import java.awt.*;
public class Player {
	
	// Fields
	int x;
	int y;
	int r;
	
	int dx;
	int dy;
	int speed;
	
	boolean left;
	boolean right;
	boolean up;
	boolean down;
	
	boolean firing;
	long firingTimer;
	long firingDelay;
	
	boolean alive;
	private Color color1;
	
	
	public Player() {
		
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2;
		r = 5;
		
		dx = 0;
		dy = 0;
		speed = 5;
		
		alive = true;
		color1 = Color.WHITE;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;
		
	}
	
	public double getx(){return x;}
	public double gety(){return y;}
	public double getr(){return r;}
	public boolean isAlive(){ return alive; }
	
	
	
	public void setLeft(boolean b){ left = b; }
	public void setRight(boolean b){ right = b; }
	public void setDown(boolean b){ down = b; }
	public void setUp(boolean b){ up = b; }
	public void isDead(boolean dead) { 
		if(dead = true){	
			alive = false; 
		}
	}
	
	public void setFiring(boolean b) { firing = b; }
	
	public void update() {
		if(left){
			dx = -speed;
		}
		if(right){
			dx = speed;
		}
		if(up){
			dy = -speed;
		}
		if(down){
			dy = speed;
		}
		
		x += dx;
		y += dy;
		
		if(x < r) x = r;
		if(y < r) y = r;
		if(x > GamePanel.WIDTH - r) x = GamePanel.WIDTH - r;
		if(y > GamePanel.HEIGHT - r) y = GamePanel.HEIGHT -r;
		
		dx = 0;
		dy = 0;
		
		if(firing){
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay){
				GamePanel.bullets.add(new Bullet(270, x, y));
				firingTimer = System.nanoTime();
			
			}
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color1);
		g.fillOval(x - r, y - r, 2 * r, 2 * r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}
}