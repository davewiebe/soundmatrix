package Theremin;

/*//////////////////////////////////////////////////////*

Chris.
Install Jsyn the way the website tells you to (Link Source and External Archives).

You should be able to run this Theremin and play with it. 
I haven't figured out how to set the default size of the JApplet,
but you can resize it to play with the sound, and check that it works.

Anyways, I'm off to bed for the night.

Have a good one.

///////////////////////////////////////////////////////////////*/

//import MainFrame;
//import MainPanel;

import java.awt.event.MouseMotionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
//import java.applet.Applet;
//import java.awt.Button;
import java.awt.Checkbox;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;/*
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;*/
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;


import com.softsynth.jsyn.*;/*
import com.softsynth.jsyn.circuits.RingModBell;
import com.softsynth.jsyn.tutorial.TUT_SineMix;
import com.softsynth.jsyn.util.BussedVoiceAllocator;
import com.softsynth.jsyn.view102.SynthScope;
import com.softsynth.jsyn.view102.UsageDisplay;
import com.softsynth.jsyn.view11x.DecibelPortFader;

import com.softsynth.jsyn.view11x.DecibelPortFader;*/
import com.softsynth.jsyn.view11x.PortFader;


public class DavesSoundSynth extends JFrame implements ItemListener, KeyListener, MouseMotionListener
{
	ThereminOsc 			thereminOsc;
	LineOut               	lineOut;

	AddUnit 				mixer;
	AddUnit 				mixer2;
	
	double					thereminX = 0;
	double 					thereminY;
	
	Checkbox				wiiChecker;
	Boolean					usingWii = false;
	Timer 					timer;
	
	Filter_1o1p1z   		filter;
	double		 			Bandwidth_Value = 0.2;
	double					Freq_Value = 400.0;
	
	LongEcho           		myEcho;

	private final int 		POLLING_RATE = 20;
	private final double 	MAX_FREQUENCY = 800;
	
	/** Returns an ImageIcon, or null if the path was invalid. (From Java Swing tutorial)*/
	protected static ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = DavesSoundSynth.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	public static void main(String[] args){
		DavesSoundSynth mainFrame = new DavesSoundSynth();
		mainFrame.setSize(new Dimension(700, 700));
	}


/* Can be run as either an application or as an applet. * /
    public static void main(String args[])
	{
  		JFrame frame = new JFrame("Theremin");
        JApplet newContentPane = new DavesSoundSynth();
		//newContentPane.setOpaque(true); //content panes must be opaque
		
        frame.setContentPane(newContentPane);

        frame.setPreferredSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		frame.setVisible(true);
		
	}*/
    
    class RemindTask extends TimerTask {


        public void run() {
        	 	
        	
        }
      }
	// The GUI Stuff
    public DavesSoundSynth() {

		//wiimote = new StdAudio();

		setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	
        ImageIcon icon = createImageIcon("theremin.jpg", "splash");
		JLabel logo = new JLabel(icon);
		logo.setBorder(BorderFactory.createEtchedBorder());
		
		logo.addMouseMotionListener(this);
		
		//c.fill = GridBagConstraints.VERTICAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		c.gridheight=3;
		c.gridwidth=3;
		add(logo, c);
		
		
		try
		{
/* Start synthesis engine.  */
		Synth.startEngine( 0 );
		Synth.setTrace(Synth.SILENT);

		filter = new Filter_1o1p1z();
		myEcho = new LongEcho( 0.01 );
		thereminOsc = new ThereminOsc();
		lineOut = new LineOut();
		mixer = new AddUnit();
		mixer2 = new AddUnit();

		thereminOsc.output.connect( 0, mixer.inputA, 0 );

		mixer.output.connect(filter.input);
		
		mixer2.output.connect(myEcho.input);
		filter.output.connect(mixer2.inputA);
		myEcho.output.connect(mixer2.inputB);
		
		mixer2.output.connect( 0, lineOut.input, 0 );   // connect to left channel
		mixer2.output.connect( 0, lineOut.input, 1 );   // connect to right channel
		
		myEcho.amplitude.set(0.3);
		
		thereminOsc.frequency.set(400);
		thereminOsc.amplitude.set(0.6);

		PortFader vibRateFader = new PortFader( thereminOsc.vibratoRate, "Vibrato Rate",  6.0, 0.0, 20.0 );		
		PortFader vibDepthFader = new PortFader( thereminOsc.vibratoDepth, "Vibrato Depth", 10.0, 0.0, 500.0 );
		
		
		JPanel pane = new JPanel();
		GridBagLayout dave = new GridBagLayout();
    	pane.setLayout(dave);
    	GridBagConstraints d = new GridBagConstraints();

    	//d.fill = GridBagConstraints.VERTICAL;
		d.gridy = 0;
    	d.gridx = 0;
    	//pane.add( wiiChecker = new Checkbox("Use Wiimote"), d );
    	//wiiChecker.addItemListener(this);


    	d.gridy = 1;
    	d.gridx = 0;
    	d.fill = GridBagConstraints.HORIZONTAL;
		pane.add(vibRateFader, d);
		d.gridy = 2;
		pane.add( vibDepthFader, d);
		
		
		c.gridx = 3;
		c.gridy = 0;
		add(pane, c);
		
		timer = new Timer();
        timer.schedule(new RemindTask(), 0, //initial delay
        POLLING_RATE); //subsequent rate
		
        
        //filter.lowPass( Freq_Value, Bandwidth_Value );
        //filter.deleteAll();
        filter.A0.set(1);
        filter.A1.set(0);
        filter.B1.set(0);
        
        myEcho.start();
		lineOut.start();
		mixer.start();
		mixer2.start();
		filter.start();
		
		thereminOsc.start();
		
		} catch (SynthException e) {
			SynthAlert.showError(this,e);
		}
		
	
/* Synchronize Java display. */
		//getParent().validate();
		//getToolkit().sync();
	}



	
/* Start note like a key press on a MIDI keyboard. */
	public void keyPressed( KeyEvent e )
	{
		//e.getKeyChar());
	}
	public void keyTyped( KeyEvent e ) {}
	public void keyReleased( KeyEvent e ){}
	
    void eventOutput(String eventDescription, MouseEvent e) {
    	if(!usingWii){
	    	thereminX =  1 - (double)Math.abs(e.getX() - 711)/711;
	    	
	        if (e.getY() < 447){
	        	thereminY = (double)(447 - e.getY())/447;
	        }else{thereminY = 0;}
	    	
	        thereminOsc.frequency.set(thereminX*MAX_FREQUENCY);
	        thereminOsc.amplitude.set(thereminY);
	        
	    	//System.out.println(thereminX +":"+ thereminY);
    	}
    }
    
    public void mouseMoved(MouseEvent e) {
        eventOutput("Mouse moved", e);
    }
    
    public void mouseDragged(MouseEvent e) {
        eventOutput("Mouse dragged", e);
    }
	
	
	
	public class ThereminOsc extends SynthCircuit{
		/* Declare units that will be part of the circuit. */
		SineOscillator  primaryFreq;
		SineOscillator  firstHarmonic; // use band limited version
		SineOscillator 	secondHarmonic;
		AddUnit			adder1;
		AddUnit     	adder2;

		AddUnit     	adder3;
		SineOscillator  vibrato;
		
	/* Declare ports. */
		public SynthInput frequency;
		//public SynthInput modulationRate;
		//public SynthInput modulationDepth;
		public SynthInput amplitude;
		public SynthInput vibratoRate;
		public SynthInput vibratoDepth;
		public SynthOutput output;
	// public SynthOuput output; // is already declared as part of SynthCircuit
		
		public ThereminOsc()  throws SynthException
		{

	/* Create various unit generators and add them to circuit.
	 * Units that are added to the circuit will be compiled into the circuit
	 * and started together when one starts the circuit.
	 */
			add( primaryFreq     = new SineOscillator() );
			add( firstHarmonic  = new SineOscillator() );
			add( secondHarmonic  = new SineOscillator() );
			add( vibrato 		= new SineOscillator() );	
			add( adder1  = new AddUnit() );
			add( adder2  = new AddUnit() );
			add( adder3  = new AddUnit() );


	/* Make ports on internal units appear as ports on circuit. */ 
	/* Optionally give some circuit ports more meaningful names. */
			addPort( frequency = adder3.inputB, "frequency" );
			//addPort( modulationRate = modOsc.frequency, "modRate" );
			//addPort( modulationDepth = modOsc.amplitude, "modDepth" );
			addPort( amplitude = primaryFreq.amplitude, "amplitude" );
			addPort( vibratoRate = vibrato.frequency, "vibratoRate" );
			addPort( vibratoDepth = vibrato.amplitude, "vibratoDepth" );
			addPort( output = adder2.output, "output" );
			
	/* Feed first oscillators through adder to offset center frequency. */
			
			adder3.output.connect(primaryFreq.frequency);

			//firstHarmonic.frequency.set(primaryFreq.frequency.get()*2);
			//secondHarmonic.frequency.set(primaryFreq.frequency.get()*4);
			
			//firstHarmonic.amplitude.set(primaryFreq.amplitude.get()*0.2);
			//secondHarmonic.amplitude.set(primaryFreq.amplitude.get()*0.06);
			
			///////

			firstHarmonic.frequency.set(primaryFreq.frequency.get()*0.5);
			secondHarmonic.frequency.set(primaryFreq.frequency.get()*2);
			
			firstHarmonic.amplitude.set(primaryFreq.amplitude.get()*0.23);
			secondHarmonic.amplitude.set(primaryFreq.amplitude.get()*0.23);
			////////
			primaryFreq.output.connect( adder1.inputA );
			firstHarmonic.output.connect( adder1.inputB );
			adder1.output.connect( adder2.inputA );
			secondHarmonic.output.connect( adder2.inputB );
			
			vibrato.output.connect( adder3.inputA );

	/* Set ports to useful values and ranges. */
			frequency.setup( 0.0, 300.0, 1000.0 );
			vibratoRate.setup(   0.0, 6.0, 10.0 );
			vibratoDepth.setup(  0.0, 10.0, 500.0 );
			amplitude.setup(   0.0, 0.9, 0.999 );
		}
		
	/** Define a behavior for setStage(). This is a flexible way to do something like ON/OFF
	 * control of a circuit.
	 */
		public void setStage( int time, int stage )
		throws SynthException
		{
			if( stage == 0 )
			{
				start( time );
			}
			else
			{
				stop( time );
			}
		}

	}

	class LongEcho extends SynthCircuit
	{
		SynthSample       delayLine;
		SampleReader_16F1 unitReader;
		SampleWriter_16F1 unitWriter;
		MultiplyAddUnit   feedbackMixer;

		int               numSamples;
		
		public SynthInput input;
		public SynthInput feedback;
		public SynthInput amplitude;
		
		public LongEcho( double delayTime )  throws SynthException
		{
			super();

	/* Calculate how many sample correspond to a given delay time. */
			numSamples = (int) (delayTime * Synth.getFrameRate());

			delayLine = new SynthSample( numSamples );

			add( unitReader = new SampleReader_16F1() );
			add( unitWriter = new SampleWriter_16F1() );
			add( feedbackMixer = new MultiplyAddUnit() );

			unitReader.samplePort.queue( delayLine, 30, numSamples-30 );
			unitReader.samplePort.queueLoop( delayLine, 0, numSamples );
			unitWriter.samplePort.queueLoop( delayLine, 0, numSamples );

			unitReader.output.connect( feedbackMixer.inputA );
			feedbackMixer.output.connect( unitWriter.input );

			addPort( input = feedbackMixer.inputC, "input" );
			addPort( feedback = feedbackMixer.inputB, "feedback" );
			addPort( amplitude = unitReader.amplitude );
			addPort( output = unitReader.output );
		}
	}


	void handleChecker()
	{
  		try {
  			if( wiiChecker.getState() )
			{
  				usingWii = true;
			}
			else
			{
				usingWii = false;
			}
   		} catch (SynthException e) {
   			SynthAlert.showError(this,e);
   		}
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();

		if( source == wiiChecker ) handleChecker();
	}
	
}
