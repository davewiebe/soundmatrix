import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Node {
final int SIG_LIM = 1200;
int x;
int y;
int d;
int sigR = 0;
int wRind;
int sigStrength = SIG_LIM;
boolean mainSig = false;
boolean boolSig = false;
Sound instr1;
BellOsc instr2 = null;


Node(){
	x = 0;
	y = 0;
	d = 0;
	//System.out.println("222");
	//instr1 = new Sound();
	//instr2 = new BellOsc();

wRind= 0;
}
Node(int a, int b, int c){
x = a;
y = b;
d= c;

wRind = 0;
}

public void Set(int a, int b, int c){
	x = a;
	y = b;
	d = c;
	instr2 = new BellOsc();
	instr1 = new Sound();
}
public void Reset(){
	x = 0;
	y = 0;
	d = 0;
}
public void Play(int ins, int freq, int str){
	System.out.println("Playing sound." + ins +", "+ freq +", "+ str);
	if(instr1 == null){
		instr1 = new Sound();
	}
	if(instr2 == null){
		instr2 = new BellOsc();
	}
	
	
	instr2.hit(freq*20, 3, .9);
}
public void paint(Graphics g){
	Graphics2D g2D = (Graphics2D)g;
	
	if(boolSig){
		if(sigStrength != SIG_LIM)
			g2D.setColor( new Color((int)(155*((double)sigStrength/(double)SIG_LIM) - 155*((double)sigR/(double)SIG_LIM)), (int)(85*((double)sigStrength/(double)SIG_LIM) - 85*((double)sigR/(double)SIG_LIM)), 0));
		else
			g2D.setColor( new Color((int)(205*((double)sigStrength/(double)SIG_LIM) - 205*((double)sigR/(double)SIG_LIM)), (int)(205*((double)sigStrength/(double)SIG_LIM) - 205*((double)sigR/(double)SIG_LIM)), (int)(205*((double)sigStrength/(double)SIG_LIM) - 205*((double)sigR/(double)SIG_LIM))));
			
		g2D.drawOval(x - sigR/2, y - sigR/2, sigR, sigR);
		
	}

	if(d!=0){
		if(boolSig)
			g2D.setColor( new Color(255, 0, 0)  );
		else
			g2D.setColor( new Color(127, 0, 0) );
		g2D.fillOval(x - d/2, y - d/2, d, d);
		
		}
}
public void wavePaint(DisplacePanel dp){
	Graphics2D tr = dp.image.createGraphics();
	short greyScale = 255;
	short a = 127;
	if(boolSig){
		if(sigStrength != SIG_LIM)
			tr.setColor( new Color(greyScale, greyScale, greyScale));
		else
			tr.setColor( new Color(greyScale, greyScale, greyScale));
			
		//draw the distortionMap
		
			tr.setColor( new Color(greyScale, greyScale, greyScale));
			tr.drawOval(x - sigR/2, y - sigR/2, sigR, sigR);
	}

}

private int max(int i, int j) {
	if(i > j)
		return i;
	else
		return j;
}
private int min(int i, int j) {
	if(i < j)
		return i;
	else
		return j;
}
private int min(double i, double j) {
	if(i < j)
		return (int)i;
	else
		return (int)j;
	
}
public void SendSigOut(){
	if(d>0){
		boolSig = true;
		Play(1, d, sigStrength);
	}
		sigR = 0;
		sigStrength = SIG_LIM;
		
		
}
public void SendSigOut(int s){
	if((d>0)&&!boolSig){
			boolSig = true;
			sigStrength -= s;
		Play(2, d, sigStrength);
	}
		
}
public void RunSignal(){

	if(boolSig){
		sigR += 4;

	}
	if(sigR > sigStrength){
		sigR = 0;
		boolSig = false;
		sigStrength = SIG_LIM;
	}
	
}
}
