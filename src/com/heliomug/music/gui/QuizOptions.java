package com.heliomug.music.gui;

import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;

public class QuizOptions {
    public static final StandardInstrument DEFAULT_INSTRUMENT = StandardInstrument.PIANO_GRAND;
    public static final boolean DEFAULT_IS_GUITAR_CHORDS = true;
    public static final boolean DEFAULT_IS_DRONE_ON = false;
    public static final boolean DEFAULT_IS_CONSTANT_ROOT = true;
    public static final Note DEFAULT_ROOT_NOTE = Note.C;
    
    
	private StandardInstrument activeInstrument;
	private boolean isGuitarChords;
	private boolean isDroneOn;
	private boolean isConstantRoot;
	private Note constantRoot;
	
	public QuizOptions() {
		activeInstrument = DEFAULT_INSTRUMENT;
		isGuitarChords = DEFAULT_IS_GUITAR_CHORDS;
		isConstantRoot = DEFAULT_IS_CONSTANT_ROOT;
		isDroneOn = DEFAULT_IS_DRONE_ON;
		constantRoot = DEFAULT_ROOT_NOTE; 
	}

	public boolean isConstantRoot() { return isConstantRoot; }
	public Note getConstantRoot() {	return constantRoot; }
	public boolean isGuitarChords() { return isGuitarChords; }
	public boolean isDroneOn() { return isDroneOn; }
	public StandardInstrument getInstrument() { return activeInstrument; }
	
	public void setConstantRoot(boolean b) { isConstantRoot = b; }
	public void setConstantRoot(Note root) { constantRoot = root; }
	public void setGuitarChords(boolean b) { isGuitarChords = b; }
	public void setDroneOn(boolean b) { isDroneOn = b; }
	public void setInstrument(StandardInstrument instrument) { activeInstrument = instrument; }
}
