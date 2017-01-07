package com.heliomug.music.quizzer;

import com.heliomug.music.KeyType;
import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;

public class QuizOptions {
    public static final StandardInstrument DEFAULT_INSTRUMENT = StandardInstrument.PIANO_GRAND;
    public static final boolean DEFAULT_IS_GUITAR_CHORDS = true;
    public static final boolean DEFAULT_IS_DRONE_ON = false;
    public static final boolean DEFAULT_IS_CONSTANT_ROOT = true;
    public static final Note DEFAULT_ROOT_NOTE = Note.C;
    public static final KeyType DEFAULT_KEY_TYPE = KeyType.MAJOR;
	public static final int DEFAULT_INTERVAL_DELAY = 500; // milliseconds duration
	public static final int DEFAULT_ARPEGGIO_DELAY = 30; // milliseconds duration
	public static final int DEFAULT_STRUM_DELAY = 15; // milliseconds duration
	public static final int DEFAULT_SUSTAIN_TIME = 3000; // milliseconds duration
	public static final int DEFAULT_VOLUME = 100;
	public static final int DEFAULT_DRONE_VOLUME = 1;
    public static final StandardInstrument DEFAULT_DRONE_INSTRUMENT = StandardInstrument.PIANO_ELECTRIC_1;
    
	private static QuizOptions theOptions;
	
	private StandardInstrument activeInstrument;
	private boolean isGuitarChords;
	private boolean isDroneOn;
	private int droneVolume;
	private StandardInstrument droneInstrument;
	private boolean isConstantRoot;
	private Note constantRoot;
	private int arpeggioDelay;
	private int strumDelay;
	private int intervalDelay;
	private int sustainTime;
	private int volume;
	
	private QuizOptions() {
		activeInstrument = DEFAULT_INSTRUMENT;
		isGuitarChords = DEFAULT_IS_GUITAR_CHORDS;
		isConstantRoot = DEFAULT_IS_CONSTANT_ROOT;
		isDroneOn = DEFAULT_IS_DRONE_ON;
		droneVolume = DEFAULT_DRONE_VOLUME;
		constantRoot = DEFAULT_ROOT_NOTE;
		arpeggioDelay = DEFAULT_ARPEGGIO_DELAY;
		intervalDelay = DEFAULT_INTERVAL_DELAY;
		strumDelay = DEFAULT_STRUM_DELAY;
		sustainTime = DEFAULT_SUSTAIN_TIME;
		volume = DEFAULT_VOLUME;
		droneInstrument = DEFAULT_DRONE_INSTRUMENT;
	}

	public static QuizOptions getOptions() {
		if (theOptions == null) {
			theOptions = new QuizOptions();
		}
		return theOptions;
	}
	
	public boolean isConstantRoot() { return isConstantRoot; }
	public Note getConstantRoot() {	return constantRoot; }
	public boolean isGuitarChords() { return isGuitarChords; }
	public boolean isDroneOn() { return isDroneOn; }
	public int getDroneVolume() { return droneVolume; }
	public StandardInstrument getDroneInstrument() { return droneInstrument; }
	public StandardInstrument getInstrument() { return activeInstrument; }
	public int getArpeggioDelay() { return arpeggioDelay; }
	public int getStrumDelay() { return strumDelay; }
	public int getIntervalDelay() { return intervalDelay; }
	public int getSustainTime() { return sustainTime; }
	public int getVolume() { return volume; }
	public int getDelay() {
		int delay = 0;
		if (isGuitarChords) {
			delay = strumDelay;
		} else {
			delay = arpeggioDelay;
		}
		return delay;
	}
	

	public void setConstantRoot(boolean b) { isConstantRoot = b; }
	public void setConstantRoot(Note root) { constantRoot = root; }
	public void setGuitarChords(boolean b) { isGuitarChords = b; }
	public void setDroneOn(boolean b) { isDroneOn = b; }
	public void setDroneVolume(int vol) { droneVolume = vol; }
	public void setDroneInstrument(StandardInstrument instrument) { droneInstrument = instrument; }
	public void setInstrument(StandardInstrument instrument) { activeInstrument = instrument; }
	public void setArpeggioDelay(int del) { arpeggioDelay = del; }
	public void setIntervalDelay(int del) { intervalDelay = del; }
	public void setStrumDelay(int del) { strumDelay = del; }
	public void setSustainTime(int time) { sustainTime = time; }
	public void setVolume(int vol) { volume = vol; }
}
