package com.heliomug.music.trainer;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordShape;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;

public class MusicPlayer {
	private static final int MIDI_CHANNEL = 1;

	private static Thread noteMuteThread;
	
	public static void kill() {
        MidiPlayer.notesOff(MIDI_CHANNEL);
		noteMuteThread.interrupt();
	}
	
	public static void playChord(Chord chord) {
		playChord(chord, 0);
	}
	
    public static void playChord(Chord chord, int arpeggioDelay) {
        MidiPlayer.setChannel(MIDI_CHANNEL, QuizOptions.getOptions().getInstrument());

        Chord toPlay;
        if (QuizOptions.getOptions().isGuitarChords()) {
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
	        	MidiPlayer.noteOn(MIDI_CHANNEL, note, QuizOptions.getOptions().getVolume());
		        try {
		            for (int i = 1 ; i < toPlay.size() ; i++) {
		            	Thread.sleep(arpeggioDelay);
		            	note = toPlay.getNote(i);
			        	MidiPlayer.noteOn(MIDI_CHANNEL, note, QuizOptions.getOptions().getVolume());
		            }
			        Thread.sleep(QuizOptions.getOptions().getSustainTime());
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

}
