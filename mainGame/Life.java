package mainGame;
import java.awt.*;



public class Life {
	
	private double x;
	private double y;
	private int r;
	
	private int numLives;
	
	Color color3;
	
	public boolean hurt;
	
	public Life(int spacing){
		
		spacing++;
		
		x = 10 * spacing;
		y = 30;
		r = 5;
		
		color3 = Color.RED;
		
		hurt = false;
		
	}
	
	public void hurt(){
		if(hurt == true && numLives > 0){
			numLives--;
			hurt = false;
		}
	}
	
	public void draw(Graphics2D g){
		g.fillOval((int) x, (int) y, 2 * r, 2 * r);
	}
	
}
