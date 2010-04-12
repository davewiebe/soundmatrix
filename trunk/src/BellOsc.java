import com.softsynth.jsyn.*;

public class BellOsc
{
	EnvelopePlayer   		envPlayer;
	EnvelopePlayer   		envPlayer2;
	SynthEnvelope     		envData;
	SynthEnvelope     		envData2;
	double[] 				data;
	double[] 				data2;

	SineOscillator  		primaryFreq;
	SineOscillator  		vibrato;
	BellOsc		 			bellOsc;
	LineOut               	lineOut;
	AddUnit					adder;
		
    public BellOsc() {


    	System.out.println("Sound()");
		try
		{
			/* Start synthesis engine.  */
			Synth.startEngine( 0 );
			Synth.setTrace(Synth.SILENT);

			envData = new SynthEnvelope( /*numFrames*/ 20 );
			envData2 = new SynthEnvelope( /*numFrames*/ 20 );
			
			primaryFreq = new SineOscillator();
			vibrato		= new SineOscillator();
			lineOut 	= new LineOut();
			envPlayer 	= new EnvelopePlayer();
			envPlayer2 	= new EnvelopePlayer();
			adder	 	= new AddUnit();
	
			adder.inputA.set(200); //fc !!!
			
			vibrato.frequency.set( 280 ); // fm!!!
			vibrato.output.connect( adder.inputB );
			vibrato.amplitude.set( 280*2 ); // fm*2!!!
			
			envPlayer2.output.connect( 0, vibrato.amplitude, 0);
			envPlayer.output.connect( 0, primaryFreq.amplitude, 0 );
			
			primaryFreq.output.connect( 0, lineOut.input, 0 );
			primaryFreq.output.connect( 0, lineOut.input, 1 );	
						
			primaryFreq.frequency.connect( adder.output );
			primaryFreq.amplitude.set(0.6);
	
			lineOut.start();			
			primaryFreq.start();	
			envPlayer.start();	
			envPlayer2.start();		
			adder.start();
			vibrato.start();

			data = new double[16*2];
			data2 = new double[16*2];
		
		} catch (SynthException e) {
			System.out.println("ERROR");
		}
	}
    
    // amplitude below 1.0
    // duration in seconds
    void hit(int frequency, double duration, double amplitude){
    	System.out.println("Frequency: "+frequency+", Duration: "+duration+", Amplitude: "+amplitude);
    	

		adder.inputA.set(frequency); //fc !!!
		vibrato.frequency.set( frequency+80 ); // fm!!!
		vibrato.amplitude.set( (frequency+80)*2 ); // fm*2!!!
    	
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

		i = 0;
		amplitude = 280*2;
		data2[i++] = 0.01;			// Duration of first segment. 
		data2[i++] = amplitude; 		// value 
		data2[i++] = duration*0.13; 	// duration
			amplitude/=2;
		data2[i++] = amplitude; 		// value 
		data2[i++] = duration*0.23; 	// duration 
			amplitude/=2;
		data2[i++] = amplitude; 		// value 
		data2[i++] = duration*0.23; 	// duration 
			amplitude/=2;
		data2[i++] = amplitude; 		// value 
		data2[i++] = duration*0.4; 	// duration 
		data2[i++] = 0.0; 			// value 

		envData.write( 0, data, 0, i/2 );
		envData2.write( 0, data2, 0, i/2 );
    	envPlayer.envelopePort.clear();
    	envPlayer2.envelopePort.clear();
    	envPlayer.envelopePort.queue( envData, 0, i/2 );
    	envPlayer2.envelopePort.queue( envData2, 0, i/2 );
    }
    
    void delete(){
    	primaryFreq.delete();
		lineOut.delete();
		envPlayer.delete();
		envPlayer2.delete();
		adder.delete();
		
		primaryFreq	= null;
		lineOut		= null;
		envPlayer	= null;
		
		Synth.stopEngine();
    }
}