import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.*;

public class MainPanel extends JPanel implements MouseListener{
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
	

MainPanel(){
	dif = new int[2];
	c = new Canvas();
	ballLocX = 0;
	ballLocY = 0;
	ballDiameter = 0;
	increment = 1.0;
	charge = false;
	addMouseListener(this);
	setSize(600, 600);
	this.add(c);
	noteList = new Node[NODE_LIM]; 
	for(int i = 0; i < NODE_LIM; i ++)
		noteList[i] = new Node();
}
	public void paint(Graphics g){
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor( Color.black ); 
		g2D.fillRect (0, 0, getWidth(), getHeight());
		g2D.setColor( Color.red ); 
		g2D.fillOval((int)(ballLocX - ballDiameter/2), (int)(ballLocY - ballDiameter/2), (int)ballDiameter, (int)ballDiameter);
		
		for(int i = 0; i < NODE_LIM; i ++)
			noteList[i].paint(g);
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
						 
			
							 if(j == i+1)
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
		if(resting)
			frmCounter++;
		else
			frmCounter = 0;
		if(frmCounter > 20)
			noteList[0].SendSigOut();
		
	}

}

