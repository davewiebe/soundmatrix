import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Node {
final int SIG_LIM = 800;
int x;
int y;
int d;
int sigR = 0;
int[] wR;
int[] wS;
int wRind;
int sigStrength = SIG_LIM;
boolean multiSig = false;
 
boolean boolSig = false;


Node(){
	x = 0;
	y = 0;
	d = 0;
wR = new int[8] ;
wS = new int[8] ;
wRind= 0;
}
Node(int a, int b, int c){
x = a;
y = b;
d= c;
wR = new int[8];
wS = new int[8] ;
wRind = 0;
}

public void Set(int a, int b, int c){
	x = a;
	y = b;
	d = c;
}
public void Reset(){
	x = 0;
	y = 0;
	d = 0;
}
public void paint(Graphics g){
	Graphics2D g2D = (Graphics2D)g;
	
	if(boolSig){
		if(multiSig)
		g2D.setColor( new Color((int)(35*((double)sigStrength/(double)SIG_LIM) - 35*((double)sigR/(double)SIG_LIM)), (int)(55*((double)sigStrength/(double)SIG_LIM) - 55*((double)sigR/(double)SIG_LIM)), (int)(255*((double)sigStrength/(double)SIG_LIM) - 255*((double)sigR/(double)SIG_LIM)))); 
		else if(sigStrength != SIG_LIM)
			g2D.setColor( new Color(0, (int)(155*((double)sigStrength/(double)SIG_LIM) - 155*((double)sigR/(double)SIG_LIM)), (int)(85*((double)sigStrength/(double)SIG_LIM) - 85*((double)sigR/(double)SIG_LIM))));
		else
			g2D.setColor( new Color((int)(5*((double)sigStrength/(double)SIG_LIM) - 5*((double)sigR/(double)SIG_LIM)), (int)(35*((double)sigStrength/(double)SIG_LIM) - 35*((double)sigR/(double)SIG_LIM)), (int)(85*((double)sigStrength/(double)SIG_LIM) - 85*((double)sigR/(double)SIG_LIM))));

		g2D.drawOval(x - sigR/2, y - sigR/2, sigR, sigR);
		
	}
	for(int i = 0; i < wRind; i++){
		if(wR[i]!=0){
		g2D.setColor( new Color(0, (int)(155*((double)wS[i]/(double)SIG_LIM) - 155*((double)wR[i]/(double)SIG_LIM)), (int)(255*((double)wS[i]/(double)SIG_LIM) - 255*((double)wR[i]/(double)SIG_LIM))));
		g2D.drawOval(x - wR[i]/2, y - wR[i]/2, wR[i], wR[i]);
		}
	}
	if(d!=0){
		if(boolSig)
			g2D.setColor( Color.cyan );
		else
			g2D.setColor( Color.white );
		
		g2D.fillOval(x - d/2, y - d/2, d, d);
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
	if(d>0)
	{
		boolSig = true;
	}
}
public void SendSigOut(int s){
	if(multiSig){
		if((boolSig)&&(wRind < 8)){
			wR[wRind] = 0;
			wS[wRind] = SIG_LIM - s;
			wRind++;
		}else if((d>0)&&!boolSig){
			boolSig = true;
			sigStrength -= s;
		}
	}
	else if((d>0)&&!boolSig){
		boolSig = true;
		sigStrength -= s;
	}
		
}
public void RunSignal(){
	for(int i = 0; i < wRind; i++){
		wR[i] += 4;
		if(wR[i] > wS[i]){
			for(int j = i+1; j <8; j++){
				wR[j-1] = wR[j];
				wS[j-1] = wS[j];
			}
			wS[7] = 0;
			wR[7] = 0;
			wRind --;			
		}
	}
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
