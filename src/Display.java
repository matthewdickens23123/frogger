import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Display extends JPanel implements ActionListener{
	public static int GRID=50;
	public static int ERRORY=10;
	public static int ERRORX=16;
	public static int WIDTH=600+ERRORX;
	public static int HEIGHT=500-ERRORY;
	public enum STATE{
		MENU,
		GAME,
		HELP
	};
	public static STATE state=STATE.MENU;
	
	private Menu menu;
	private BufferedImage image;
	private Frog frog;
	private Cars cars1[];
	private Cars cars2[];
	private Logs logs1[];
	private Logs logs2[];
	private Logs logs3[];
	private int deaths=0;
	private int score=0;
	Display(){
		Timer timer= new Timer(16,this);
		frog= new Frog(250,HEIGHT-90,50,50);
		menu= new Menu();
		cars1= new Cars[2];
		cars2= new Cars[3];
		logs1= new Logs[2];
		logs2= new Logs[2];
		logs3= new Logs[2];
		
		loadMap("/map.png");
		for(int i=0;i<cars1.length;i++){
			cars1[i]= new Cars(0+i*290,HEIGHT-140,100,50,3);
		}
		for(int i=0;i<cars2.length;i++){
			cars2[i]= new Cars(0+i*270,HEIGHT-190,100,50,-2);
		}
		for(int i=0;i<logs1.length;i++){
			logs1[i]= new Logs(0+i*250,HEIGHT-290,170,50,+2);
		}
		for(int i=0;i<logs2.length;i++){
			logs2[i]= new Logs(0+i*300,HEIGHT-340,170,50,-2);
		}
		for(int i=0;i<logs3.length;i++){
		logs3[i]= new Logs(0+i*350,HEIGHT-390,170,50,+3);
		}
			
		this.addMouseListener(menu);
		this.addMouseMotionListener(menu);
		this.addKeyListener(frog);
		setFocusable(true);
		timer.start();
	}
	public void didIntersectCar(){
		for(Cars car:cars1){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				reset();
			}
		}
		for(Cars car:cars2){
			if(frog.getFrog().getBounds().intersects(car.getCar().getBounds())){
				reset();
			}
		}
	}
	public void isInsideLog(){
		Logs logarray[][]=new Logs[][] {logs1,logs2,logs3};

		for (int i = 0; i < logarray.length; i++) {

			if(frog.getFrog().getCenterY()<HEIGHT-240-i*50&&frog.getFrog().getCenterY()>HEIGHT-290-i*50){
				if(!((frog.getFrog().getMinX()>logarray[i][0].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][0].getLog().getMaxX())||
						(frog.getFrog().getMinX()>logarray[i][1].getLog().getMinX()&&frog.getFrog().getMaxX()<logarray[i][1].getLog().getMaxX()))){
					reset();
				}
				else{
					frog.mover(logarray[i][1].getSpeed());
				}
			}
		}
	}
	
	public void loadMap(String sprite){
		try {
			image= ImageIO.read(getClass().getResourceAsStream(sprite));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void score(){
		if(frog.getFrog().getCenterY()<HEIGHT-390){
			score++;
			deaths--;
			reset();
		}
	}
	public void showInfo(Graphics g){
		Graphics2D g2d= (Graphics2D)g;
		g.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.PLAIN, 18));
		g2d.drawString("Deaths: "+Integer.toString(deaths), 15, 20);
		g2d.drawString("Score: "+Integer.toString(score), 105, 20);
	}
	public void reset(){
		deaths++;
		frog.getFrog().x=250;
		frog.getFrog().y=HEIGHT-90;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(state==STATE.MENU||state==STATE.HELP){
			menu.render(g);
		}else if(state==STATE.GAME){
		g.drawImage(image, 0, 0, null);
		for(Logs log: logs1)
			log.render(g);
		for(Logs log: logs2)
			log.render(g);
		for(Logs log: logs3)
			log.render(g);
		frog.render(g);
		for(Cars car: cars1)
			car.render(g);
		for(Cars car: cars2)
			car.render(g);
		score();
		showInfo(g);
		didIntersectCar();
		isInsideLog();
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		repaint();
	}
}
