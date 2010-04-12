import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.geom.AffineTransform;
import java.awt.image.Kernel;

import javax.swing.*;

public class MainPanel extends JPanel implements MouseListener{
	final int SIDE = 600;
	int ballLocX;
	int ballLocY;
	double ballDiameter;
	double increment;
	boolean charge;
	Canvas c ;
	Node[] noteList;
	final int NODE_LIM = 32;
	int nodeInd = 0;
	int frmCounter;
	int[] dif;
	Background bg;
	int[][] xDisp = new int[SIDE][SIDE];
	int[][] yDisp = new int[SIDE][SIDE];
	Image img;
	BufferedImage bImg;
	boolean debug = false;
	ConvolveOp convolveOp;
	Kernel krn;
	DisplacePanel xPan;
	DisplacePanel yPan;
	

MainPanel(){
	xPan = new DisplacePanel(SIDE);
	yPan = new DisplacePanel(SIDE);
	
	if(debug)
	{
		JFrame xFrame = new JFrame("Horizontal Displacement");
		JFrame yFrame = new JFrame("Veritcal Displacement");
		yFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		xFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		xFrame.getContentPane().add(xPan, BorderLayout.CENTER);
		yFrame.getContentPane().add(yPan, BorderLayout.CENTER);
		yFrame.pack();
		xFrame.pack();
		xFrame.setSize(SIDE, SIDE);
		yFrame.setSize(SIDE, SIDE);
		xFrame.setLocation(650, 20);
		yFrame.setLocation(1300, 20);
	yFrame.setVisible(true);
	xFrame.setVisible(true);
	
	}
	dif = new int[2];
	c = new Canvas();
	ballLocX = 0;
	ballLocY = 0;
	ballDiameter = 0;
	increment = 1.0;
	charge = false;
	bg = new Background();
	addMouseListener(this);
	setSize(SIDE, SIDE);
	this.add(c);
	noteList = new Node[NODE_LIM]; 
	img = bg.getImage();
   float[] waveMat = new float[9];
   for(int i=0; i<9;i++)
		   waveMat[i] = 0.0f;
   waveMat[4] = 1.0f;
   Kernel krn = new Kernel(3, 3, waveMat);
	for(int i = 0; i < NODE_LIM; i ++)
		noteList[i] = new Node();
	 bImg = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_RGB);
	 Graphics2D big = bImg.createGraphics();
	 AffineTransform affineTransform = new AffineTransform();
	 big.drawImage(img, affineTransform, this);
	 convolveOp = new ConvolveOp(krn, ConvolveOp.EDGE_NO_OP, null);
	   for(int i = 0; i < SIDE; i++)
		   for(int j = 0; j < SIDE; j++)
		   {
			   yDisp[i][j] = 0;
			   xDisp[i][j] = 0;
			   if((i > 100)&&(i<200))
				   yDisp[i][j] = i/2 - 50;
		   }
}

	public void paint(Graphics g){
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor( Color.black );
		g2D.fillRect (0, 0, getWidth(), getHeight());
		
			g2D.drawImage(img, 0, 0, null);
		xPan.Clear();
		for(int i = 0; i < nodeInd; i ++){
			noteList[i].paint(g);
			noteList[i].wavePaint(xPan);
		} 
		
		xPan.repaint();
		yPan.repaint();
		
	}
	public void mousePressed(MouseEvent e) {
	      ballLocX = e.getX(); 
		  ballLocY = e.getY();
		  charge = true;
	}

	public void mouseReleased(MouseEvent e) {
		charge = false;
		AddNode(ballLocX, ballLocY, (int)ballDiameter);
	}

	public void mouseEntered(MouseEvent e) {
		       
	}

	public void mouseExited(MouseEvent e) {
		      
	}

	public void mouseClicked(MouseEvent e) {
		       
	}
	class TimerAction implements ActionListener {
		 
		public void actionPerformed(ActionEvent arg0) {
			
		        repaint();   
			
		}
	}
	public void AddNode(int a, int b, int c){
		if(nodeInd < NODE_LIM){
	noteList[nodeInd].Set(a, b, c);
	nodeInd++;
		}
	}
	public void GrowNode() {
		if(charge){
				if(ballDiameter < 30)
					ballDiameter += 5/ballDiameter;
		}
		else
			ballDiameter = 6;
		
	}
	public boolean CastSignals(){
		boolean flag = true;
		for(int i = 0; i < NODE_LIM; i++){
			 noteList[i].RunSignal();
			 if(noteList[i].boolSig)
				 flag = false;
			 for(int j = 0; j < NODE_LIM; j++){
				 if(i!=j){
				 double distance = Math.pow(Math.pow(Math.abs(noteList[i].x - noteList[j].x), 2) + Math.pow(Math.abs(noteList[i].y - noteList[j].y), 2), 0.5);  
					 if(  Math.abs(((int)distance - noteList[i].sigR/2)) < 2){
							 if((j == i+1)||((j==0)&&(i+1==nodeInd))) //trigger next node; or first node if signal is from last node.
							 	noteList[j].SendSigOut();
						 	else
						 		noteList[j].SendSigOut(noteList[i].sigR);
						
						 
					}
				 }
			 }

		}
		return flag;
			
	}
	public void HandlingFunction() {
		GrowNode();
		boolean resting = CastSignals();
		if(resting)  //if the system has not caught a signal it should reset istelf
			frmCounter++;
		else
			frmCounter = 0;
		if(frmCounter > 120)
			noteList[0].SendSigOut();
		
	}
	private void ProcessDisplacement(){
		
	}


}


