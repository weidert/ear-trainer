package com.heliomug.music;

public class Note {
	public static final int INTERVALS_IN_CHROMATIC = 12;

	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	public static final Note E_FLAT = new Note(39);
	public static final Note E = new Note(40);
	public static final Note F = new Note(41);
	public static final Note F_SHARP = new Note(42);
	public static final Note G_FLAT = new Note(42);
	public static final Note G = new Note(43);
	public static final Note G_SHARP = new Note(44);
	public static final Note A_FLAT = new Note(44);
	public static final Note A = new Note(45);
	public static final Note A_SHARP = new Note(46);
	public static final Note B_FLAT = new Note(47);
	public static final Note B = new Note(47);
	public static final Note C = new Note(48);
	public static final Note C_SHARP = new Note(49);
	public static final Note D_FLAT= new Note(49);
	public static final Note D = new Note(50);
	public static final Note D_SHARP = new Note(51);
	
	private final int value;
	
	public Note(int val) {
		assert (val >= 0 && val < 1024);
		this.value = val;
	}
	
	public String toString() {
		return String.format("%-2s(%2d)", getNoteName(), value);
	}

	public Note getHigher(int offset) {
		return new Note(value + offset);
	}
	
	public int difference(Note other) {
		return this.value - other.value;
	}

    public int distanceTo(Note other) {
        return other.value - this.value;
    }

    public Note nextAbove(Note other) {
        return getHigher(distanceTo(other) % 12);
    }
	
	public boolean sameNoteLetter(Note other) {
		return ((this.value - other.value) % INTERVALS_IN_CHROMATIC == 0); 
	}
	
	public int getValue() { return this.value; }
	
	public String getNoteName() {
		return NOTE_NAMES[value % INTERVALS_IN_CHROMATIC];
	}
}
