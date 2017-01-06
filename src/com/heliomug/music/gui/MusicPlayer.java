package com.heliomug.music.gui;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordShape;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;

public class MusicPlayer {
	private static final int MIDI_CHANNEL = 1;

    private static final int VOLUME = 100;
    
	private static final int DEFAULT_SUSTAIN_TIME = 3000; // milliseconds duration
	private static final int DEFAULT_INTERVAL_TIME = 500; // milliseconds duration
	private static final int DEFAULT_GUIT_STRUM_DELAY = 15; // milliseconds duration
	
	private static Thread noteMuteThread;
	
    public static void playChord(Chord chord, int millis, int sustain) {
        MidiPlayer.setChannel(MIDI_CHANNEL, QuizFrame.getOptions().getInstrument());

        Chord toPlay;
        if (QuizFrame.getOptions().isGuitarChords()) {
        	toPlay = ChordShape.fillChord(chord);
        } else {
        	toPlay = chord;
        }
        
    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(MIDI_CHANNEL);
    	}
    	
        if (chord.size() > 0) {
        	noteMuteThread = new Thread(() -> {
	        	Note note;
	        	note = toPlay.getNote(0);
	        	MidiPlayer.noteOn(MIDI_CHANNEL, note, VOLUME);
		        try {
		            for (int i = 1 ; i < toPlay.size() ; i++) {
		            	Thread.sleep(millis);
		            	note = toPlay.getNote(i);
			        	MidiPlayer.noteOn(MIDI_CHANNEL, note, VOLUME);
		            }
			        Thread.sleep(sustain);
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
    
    public static void playChord(Chord chord) {
    	int delay = 0;
    	if (QuizFrame.getOptions().isGuitarChords()) {
    		delay = DEFAULT_GUIT_STRUM_DELAY;
    	}
    	playChord(chord, delay, DEFAULT_SUSTAIN_TIME);
    }

    public static void playInParallel(Note... notes) {
    	Chord chord = new Chord();
    	for (Note note : notes) {
    		chord.addNote(note);
    	}
    	playChord(chord);
    }
    
    public static void playInSeries(Note... notes) {
    	Chord chord = new Chord();
    	for (Note note : notes) {
    		chord.addNote(note);
    	}
    	playChord(chord, DEFAULT_SUSTAIN_TIME, DEFAULT_INTERVAL_TIME);
    }

}
