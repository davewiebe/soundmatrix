import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JPanel;

public class DisplacePanel extends JPanel
{	
	ConvolveOp convolveOp;
	 float[] waveMat = new float[121];
	 Kernel krn;
	BufferedImage image;
	int size = 0;
	
	DisplacePanel(){
		
	}
	DisplacePanel(int s){
		 for(int i=0; i<121;i = i+11){
			 waveMat[i] = 0.01f;
			 waveMat[i+1] = 0.04f;
			 waveMat[i+2] = 0.07f;
			 waveMat[i+3] = 0.11f;
			 waveMat[i+4] = 0.17f;
			 waveMat[i+5] = 0.20f;
			 waveMat[i+6] = 0.17f;
			 waveMat[i+7] = 0.11f;
			 waveMat[i+8] = 0.07f;
			 waveMat[i+9] = 0.04f;
			 waveMat[i+10] = 0.01f;
			  
		 }
		 final float[] SHARP = { 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
		      0.0f, 0.0f, 0.0f, 0.0f };
		 float[] kTest = new float[49];

			 kTest[0] = 0.33f;
			 kTest[1] = 0.00f;
			 kTest[2] = 0.00f;
			 kTest[3] = 0.0f;
			 kTest[4] = 0.0f;
			 kTest[5] = 0.0f;
			 kTest[6] = 0.0f;
			 kTest[7] = 0.0f;
			 kTest[8] = 0.00f;
			 kTest[9] = 0.00f;
			 kTest[10] = 0.0f;
			 kTest[11] = 0.00f;
			 kTest[12] = 0.00f;
			 kTest[13] = 0.0f;
			 kTest[14] = 0.0f;
			 kTest[15] = 0.0f;
			 kTest[16] = 0.0f;
			 kTest[17] = 0.0f;
			 kTest[18] = 0.0f;
			 kTest[19] = 0.0f;
			 kTest[20] = 0.0f;
			 kTest[21] = 0.0f;
			 kTest[22] = 0.0f;
			 kTest[23] = 0.0f;
			 kTest[24] = 0.0f;
			 kTest[25] = 0.33f;
			 kTest[26] = 0.0f;
			 kTest[27] = 0.0f;
			 kTest[28] = 0.0f;
			 kTest[29] = 0.0f;
			 kTest[30] = 0.0f;
			 kTest[31] = 0.0f;
			 kTest[32] = 0.0f;
			 kTest[33] = 0.0f;
			 kTest[34] = 0.0f;
			 kTest[35] = 0.0f;
			 kTest[36] = 0.0f;
			 kTest[37] = 0.0f;
			 kTest[38] = 0.0f;
			 kTest[39] = 0.0f;
			 kTest[40] = 0.0f;
			 kTest[41] = 0.0f;
			 kTest[42] = 0.0f;
			 kTest[43] = 0.0f;
			 kTest[44] = 0.0f;
			 kTest[45] = 0.0f;
			 kTest[46] = 0.0f;
			 kTest[47] = 0.0f;
			 kTest[48] = 0.33f;

	    krn = new Kernel(49, 1,kTest);
		setSize(s, s);
		size = s;
		image = new BufferedImage(s, s, BufferedImage.TYPE_BYTE_GRAY);
		 Graphics2D big = image.createGraphics();
		 AffineTransform affineTransform = new AffineTransform();
		 big.drawImage(image, affineTransform, this);
		 convolveOp = new ConvolveOp(krn, ConvolveOp.EDGE_ZERO_FILL, null);
	}
	
	public void paint(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor( new Color(127, 127, 127) );
		g2D.fillRect (0, 0, getWidth(), getHeight());
		if(image != null) 
			g2D.drawImage(image, convolveOp, 0, 0);
	}
	public void Clear(){
		image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D tr = image.createGraphics();	
		tr.setColor( new Color(127, 127, 127) );
		tr.fillRect (0, 0, getWidth(), getHeight());
	}
}
