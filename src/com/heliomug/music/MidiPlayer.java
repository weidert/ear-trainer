package com.heliomug.music;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {
	private static Synthesizer synth;
	private static MidiChannel[] channels;
	private static Instrument[] instruments;
	
	static {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			channels = synth.getChannels();
			instruments = synth.getDefaultSoundbank().getInstruments();
		} catch (MidiUnavailableException e) {
			System.err.println("CANNOT FIND MIDI.  EXITING...");
			e.printStackTrace();
		}
	}
	
	public static void setChannel(int chan, StdInstrument instr) {
		channels[chan].programChange(instr.getCode());
	}
	
	private static void noteOn(int chan, int note, int vol) {
		channels[chan].noteOn(note, vol);
	}
	
	public static void noteOn(int chan, int note, double vol) {
		noteOn(chan, note, (int)(100 * vol));
	}
	
	public static void noteOn(int chan, Note note, double vol) {
		noteOn(chan, note.getValue(), (int)(100 * vol));
	}
	
	public static void notesOff(int chan) {
		channels[chan].allNotesOff();
	}
	
    private static void wait(double d) {
		try {
			Thread.sleep((int)(1000*d));
		} catch (InterruptedException e) {
			System.err.println("error: interrupted");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args)  {
		System.out.println("----Starting----");

		//int i = 0;
		for (Instrument inst : instruments) {
			System.out.println(inst);
/*			setChannel(1, i);
			noteOn(1, 60, 100);
			wait(.5);
			notesOff(1);
			wait(.5);
			i++;
*/
		}
		
		
		setChannel(1, StdInstrument.PIANO_GRAND);
		noteOn(1, 60, 100);
		wait(.5);
		noteOn(1, 64, 100);
		wait(.5);
		noteOn(1, 67, 100);
		wait(1.5);
		notesOff(0);
		noteOn(1, 60, 100);
		noteOn(1, 64, 100);
		noteOn(1, 67, 100);
		wait(3.0);
		notesOff(0);

		setChannel(2, StdInstrument.ORGAN_CHURCH);
		noteOn(2, 60, 100);
		wait(.5);
		noteOn(2, 64, 100);
		wait(.5);
		noteOn(2, 67, 100);
		wait(1.5);
		notesOff(0);
		noteOn(2, 60, 100);
		noteOn(2, 64, 100);
		noteOn(2, 67, 100);
		wait(3.0);
		notesOff(0);

		wait(.5);
        
	}
}
