import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends Applet implements KeyListener,ActionListener
{	
	int presentShipLocation = 250;
	ArrayList<Bullets> bulletObj = new ArrayList<Bullets>();
	ArrayList<Enemy> enemyObj = new ArrayList<Enemy>();
	Image enemyPicture;
	Image consolePicture;
	int sleeptime = 0;
	int totalPoints = 0;
	boolean gameStarted = false;
	public void init()
	{
		setBackground(Color.BLACK);
		setSize(500,600);
		enemyPicture = getImage(getCodeBase(),"Enemy.png"); 
		consolePicture = getImage(getCodeBase(),"Console.png");
		this.addKeyListener(this);
		swarmenemy();
	}
	public void StartButton()
	{
		Button StartB = new Button("Start Game");
		StartB.setForeground(Color.WHITE);
		StartB.setBackground(Color.BLACK);
		StartB.setBounds(215,288,70,24);
		StartB.addActionListener(this);
		this.add(StartB);
	}	
	public void StartGame(Graphics g)
	{
		if(enemyObj.size() == 0)
		{
			g.drawString("GAME OVER", 10, 560);
			g.drawString("Total Point: " + totalPoints,10,540);
			g.drawString("Congratulation!!!",10,50);
			return;
		}
		for(Enemy x: enemyObj)
			if(x.y + 40  < 450)
				drawEnemy(x.y , x.x , g);
			else
			{
				g.drawString("GAME OVER", 10, 560);
				g.drawString("Total Point: " + totalPoints,10,540);
				return;
			}
		
		for(Bullets x : bulletObj)
			if(x.y != 0)
				drawbullet(x.x,x.y,g);
		
		g.drawLine(0,500,500,500);
		g.drawString("Total Points: " + totalPoints,10,540);
		try
		{
			collision();
			gamer();
		}
		catch(InterruptedException e)
		{
			repaint();
		}
		
	}
	public void paint(Graphics g)
	{
		drawship(g);
		if(!gameStarted)
		{
			g.drawImage(consolePicture,150,10, this);
			StartButton();
		}
		else
			StartGame(g);
	}
	public void swarmenemy()
	{
		int counter = 0;
		for(int i = 50 ; counter<5 ; i-= 55)
		{
			for(int j = 30 ; j < 430 ; j+=55)
			{
				Enemy obj = new Enemy(j,i);
				enemyObj.add(obj);
			}
			counter++;
		}

	}
	public void gamer() throws InterruptedException
	{
		for(Bullets x: bulletObj)
			if(x.y > 0)
				x.y-=5;
		
		if(sleeptime%900 == 0)
		{
			for(Enemy x : enemyObj)
				if(x.y < 450)
					x.y += 10;
		}
		sleeptime += 15;
		Thread.sleep(15);
		repaint();
	}
	public void collision() throws InterruptedException
	{	 
		for(int i = 0 ; i < bulletObj.size() ; i++)
		{
			Bullets x = bulletObj.get(i);
			
			for(int j = 0 ; x != null && j < enemyObj.size(); j++)
			{
				Enemy y = enemyObj.get(j);
				
				if(x.x >= y.x-20 && x.x <= y.x+20)
				{
					if(x.y <= y.y + 20)
					{
						totalPoints += y.points;
						bulletObj.remove(x);
						enemyObj.remove(y);
						break;
					}
				}
			}
		}
	}
	public void drawship(Graphics g)
	{
		int x = presentShipLocation;
		g.setColor(Color.GREEN);
		g.fillRect(x,470,30,20);
		g.fillRect(x+5,460,20,10);
		g.drawLine(x+15,460,x+15,450);
		g.drawLine(x+16,460,x+16,450);
	}
	public void drawEnemy(int y,int x , Graphics g)
	{
		g.drawImage(enemyPicture,x,y,this);
	}
	public void drawbullet(int x,int y,Graphics g)
	{
		g.setColor(Color.WHITE);
		g.drawLine(x+15,y+10,x+15,y);
		g.drawLine(x+16,y+10,x+16,y);
		g.setColor(Color.GREEN);
	}
	@Override
	public void keyPressed(KeyEvent arg0) 
	{
		if(arg0.getKeyChar() == 'D' || arg0.getKeyChar() == 'd')
		{
			if(presentShipLocation+10 < 470)
				presentShipLocation += 10 ;
			repaint();
		}
		else if(arg0.getKeyChar() == 'A' || arg0.getKeyChar() == 'a')
		{
			if(presentShipLocation-10 > 15)
				presentShipLocation -= 10 ;
			repaint();
		}
		else if(arg0.getKeyChar() == 'W' || arg0.getKeyChar() == 'w')
		{
			Bullets obj = new Bullets(presentShipLocation);
			bulletObj.add(obj);
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	public void keyTyped(KeyEvent arg0) {
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		gameStarted = true;
		this.removeAll();
		repaint();
	}
}
