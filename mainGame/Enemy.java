package mainGame;
import java.awt.*;

public class Enemy {
	double x;
	double y;
	int r;
	int width;
	int height;
	
	double dx;
	double dy;
	double rad;
	double speed;
	
	int score;
	int health;
	int type;
	int rank; 
	
	
	Color color1;
	
	boolean ready;
	boolean dead;
	
	
	//CONSTRUCTOR
	
	public Enemy(int type, int rank){
		this.type = type;
		this.rank = rank;
		
		type = type % 2;
		
		if(type == 1){
			color1 = Color.RED;
			
			speed = 4 * rank /2;
			r = 5 + rank;
			health = 1 * rank;
			score = health;
		}
		else{
			color1 = Color.black;
			
			speed = 5 * rank /2;
			r = 4 + rank;
			health = 1 * rank;
			score = health;
		}
		
		x = Math.random() * GamePanel.WIDTH /2 + GamePanel.WIDTH / 4;
		y = -r;
		
		double angle = Math.random() * 140 * 20;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
		ready = false;
		dead = false;
		width = 2 * r;
		height = width;
	}
	
	//Functions
	public int getScore() { return score; }
	public double getx() { return x; }
	public double gety() { return y; }
	public double getr() { return r; }
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, width, height);
	}
	public boolean isDead() { return dead; }
	
	
	public void hit() {
		//TODO: Draw blood if dead.
		
		health--;
		if(health <= 0) {
			dead = true;
		}
	}
	
	public void update() {
		x += dx;
		y += dy;
		
		if(!ready) {
			if(x > r && x < GamePanel.WIDTH - r && 
				y > r && y < GamePanel.HEIGHT - r){
				ready = true;
			}
		}
		
		if(x < r && dx < 0) dx = -dx;
		if(y < r && dy < 0) dy = -dy;
		if(x > GamePanel.WIDTH - r && dx > 0) dx = -dx;
		if(y > GamePanel.HEIGHT - r && dy > 0) dy = - dy;
		
	}
	
	public void draw(Graphics2D g) {
		//TODO: depending on what type, draw object as triangle, square, or more
		//polygons. 
		g.setColor(color1);
		//g.fillOval((int) x, (int) y, 2 * r, 2 * r);
		//g.fillRect((int) (x), (int) (y), 10, 10);
	    		
	    
	    
		g.setStroke(new BasicStroke(3));
		g.setColor(color1);
		g.drawRect((int) (x), (int) (y), width, height);
		g.setStroke(new BasicStroke(1));
	}
	
	
	
	
	
}
