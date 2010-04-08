
import java.util.Timer;
import java.awt.Checkbox;

import com.softsynth.jsyn.*;


public class Sound
{
	EnvelopePlayer   		envPlayer;
	SynthEnvelope     		envData;
	double[] 				data;
	
	ThereminOsc 			thereminOsc;
	LineOut               	lineOut;
		
    public Sound() {
    			
		envData = new SynthEnvelope( /*numFrames*/ 20 );

    	System.out.println("Sound()");
		try
		{
			/* Start synthesis engine.  */
			Synth.startEngine( 0 );
			Synth.setTrace(Synth.SILENT);
	
			thereminOsc = new ThereminOsc();
			lineOut 	= new LineOut();
			envPlayer 	= new EnvelopePlayer();
	
	
			envPlayer.output.connect( 0, thereminOsc.amplitude, 0 );
			
			thereminOsc.output.connect( 0, lineOut.input, 0 );
			thereminOsc.output.connect( 0, lineOut.input, 1 );			
			
			thereminOsc.frequency.set(400);
			thereminOsc.amplitude.set(0.6);
	
			lineOut.start();			
			thereminOsc.start();
			envPlayer.start();		
			
			data = new double[16*2];
		
		} catch (SynthException e) {
			System.out.println("ERROR");
		}
	}
    
    // amplitude below 1.0
    // duration in seconds
    void hit(int frequency, double duration, double amplitude){
    	System.out.println("Frequency: "+frequency+", Duration: "+duration+", Amplitude: "+amplitude);
    	
    	thereminOsc.frequency.set(frequency);
    	
		int i=0;
		data[i++] = 0.01;			// Duration of first segment. 
		data[i++] = amplitude; 		// value 
		data[i++] = duration*0.13; 	// duration
			amplitude/=2;
		data[i++] = amplitude; 		// value 
		data[i++] = duration*0.23; 	// duration 
			amplitude/=2;
		data[i++] = amplitude; 		// value 
		data[i++] = duration*0.23; 	// duration 
			amplitude/=2;
		data[i++] = amplitude; 		// value 
		data[i++] = duration*0.4; 	// duration 
		data[i++] = 0.0; 			// value 
		
		envData.write( 0, data, 0, i/2 );
    	envPlayer.envelopePort.clear();
    	envPlayer.envelopePort.queue( envData, 0, i/2 );
    }
}