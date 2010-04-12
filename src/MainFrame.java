
	import javax.swing.*; 
	import java.awt.*;
import java.awt.event.*;
	
	 
	 
// water backdrop courtesy of pachd and can be found at: http://www.pachd.com/free-images/abstract-images/water-04.jpg
//image converted to .java file thanks to ImageToClass; a project undertaken by Stephen Ware: http://stephengware.com/projects/imagetoclass/
	public class MainFrame extends JApplet implements Runnable, ActionListener 
	{
		Thread t;
		MainPanel mainPanel;
		public MainFrame(){
			mainPanel = new MainPanel();
			t = new Thread(this);
			t.start();
		}
		public void init(){
			this.setContentPane(mainPanel);
			this.setSize(600, 600);
		}
		public void paint(Graphics g) {
		mainPanel.paint(g);
		}
		
		public void run() {
			while(true){
			mainPanel.HandlingFunction();
			mainPanel.repaint();
				try{
				t.sleep(35);
				}
				catch(InterruptedException e){
						System.out.println(e); 
					
				}
			}
		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "I am happy."); 
			
		}
	}