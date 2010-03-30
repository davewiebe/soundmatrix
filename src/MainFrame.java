
	import javax.swing.*; 
	import java.awt.*;
import java.awt.event.*;
	
	 
	 

	public class MainFrame extends JApplet implements Runnable, ActionListener 
	{
		Thread t;
		MainPanel mainPanel;
		public MainFrame(){
			mainPanel = new MainPanel();
			t = new Thread(this);
			t.start();
			JFrame frame = new JFrame("Java Swing Demo");
			frame.setPreferredSize(new Dimension(600, 600));
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setContentPane(mainPanel);
			frame.pack();
			frame.setVisible(true);
		}
		public static void main(String[] args){
			MainFrame mainFrame = new MainFrame();
			
		}
		public void init(){
			

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