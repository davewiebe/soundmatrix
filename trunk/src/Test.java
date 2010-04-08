
import java.applet.Applet;
import java.awt.Button;
import java.awt.Event;

import com.softsynth.jsyn.AppletFrame;
import com.softsynth.jsyn.Synth;
import com.softsynth.jsyn.SynthAlert;
import com.softsynth.jsyn.SynthException;

public class Test extends Applet
{
	Button startOne, startTwo;
	Sound mySound;
	
	public static void main(String args[])
	{
	   Test  applet = new Test();
	   AppletFrame frame = new AppletFrame("Test JSyn", applet);
	   frame.resize(600,400);
	   frame.show();
	   /* Begin test after frame opened so that DirectSound will use Java window. */
	   frame.test();

	}

 /*
  * Setup synthesis by overriding start() method.
  */
	public void start()  
	{
	   try
	   {
		  Synth.startEngine(0);
			mySound = new Sound();
			
			add( startOne = new Button("Test Sound") );
			add( startTwo = new Button("Another Sound") );
			
			getParent().validate();
			getToolkit().sync();
			
	   } catch(SynthException e) {
		  SynthAlert.showError(this,e);
	   }
	}

  /*
   * Clean up synthesis by overriding stop() method.
   */
	public void stop()  
	{
		mySound.delete();
		removeAll();
		Synth.stopEngine();
    }
	
   	public boolean action(Event evt, Object what)
   	{
 		if( evt.target == startOne )
   		{
 			mySound.hit(400, 5, .9);
 			return true;
   		}
 		
		else if ( evt.target == startTwo )
		{
 			mySound.hit(800, 5, .9);
			return(true);
		}
   		return false;
    }
}