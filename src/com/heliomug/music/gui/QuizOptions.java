package com.heliomug.music.gui;

import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;

public class QuizOptions {
    private static final StandardInstrument DEFAULT_INSTRUMENT = StandardInstrument.PIANO_GRAND;

	private StandardInstrument activeInstrument;
	private boolean isGuitarChords;
	private boolean isDroneOn;
	private boolean isConstantRoot;
	private Note constantRoot;
	
	public QuizOptions() {
		activeInstrument = DEFAULT_INSTRUMENT;
		isGuitarChords = false;
		isConstantRoot = true;
		isDroneOn = false;
		constantRoot = Note.C; 
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
