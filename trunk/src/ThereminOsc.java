import com.softsynth.jsyn.AddUnit;
import com.softsynth.jsyn.SineOscillator;
import com.softsynth.jsyn.SynthCircuit;
import com.softsynth.jsyn.SynthException;
import com.softsynth.jsyn.SynthInput;
import com.softsynth.jsyn.SynthOutput;

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
	public SynthInput amplitude;
	public SynthInput vibratoRate;
	public SynthInput vibratoDepth;
	public SynthOutput output;
	
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
		addPort( amplitude = primaryFreq.amplitude, "amplitude" );
		addPort( vibratoRate = vibrato.frequency, "vibratoRate" );
		addPort( vibratoDepth = vibrato.amplitude, "vibratoDepth" );
		addPort( output = adder2.output, "output" );
		
/* Feed first oscillators through adder to offset center frequency. */
		
		adder3.output.connect(primaryFreq.frequency);

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
		vibratoRate.setup(   0.0, 9.0, 10.0 );
		vibratoDepth.setup(  0.0, 0.0, 500.0 );
		amplitude.setup(   0.0, 0.9, 0.999 );
	}

}
