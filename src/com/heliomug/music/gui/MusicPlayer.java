package com.heliomug.music.gui;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordShape;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;

public class MusicPlayer {
	private static final int MIDI_CHANNEL = 1;

    private static final int VOLUME = 100;
    
	private static final int SUSTAIN_TIME = 3000; // milliseconds duration
	private static final int INTERVAL_TIME = 500; // milliseconds duration
	
	private static Thread noteMuteThread;
	
	private static void prepareForPlay() {
        MidiPlayer.setChannel(MIDI_CHANNEL, QuizFrame.getOptions().getInstrument());
	}
	
    public static void playChord(Chord chord) {
    	prepareForPlay();
    	Chord toPlay;
        if (QuizFrame.getOptions().isGuitarChords()) {
        	toPlay = ChordShape.fillChord(chord);
        } else {
        	toPlay = chord;
        }
    	// System.out.println(toPlay);

    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        toPlay.play(MIDI_CHANNEL, VOLUME);
	        try {
	            Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }

    public static void playInParallel(Note a, Note b) {
    	prepareForPlay();
    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        a.play(MIDI_CHANNEL, 100);
	        b.play(MIDI_CHANNEL, 100);
	        try {
	            Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }
    
    public static void playInSeries(Note a, Note b) {
    	prepareForPlay();
    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        a.play(MIDI_CHANNEL, 100);
	        try {
	            Thread.sleep(INTERVAL_TIME);
		        b.play(MIDI_CHANNEL, 100);
		        Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }

}
