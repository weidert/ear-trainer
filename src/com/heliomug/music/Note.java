package com.heliomug.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Note {
	public static final int INTERVALS_IN_CHROMATIC = 12;

	public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static final String[] NOTE_LABELS = {"C", "C#/D\u266D", "D", "D#/E\u266D", "E", "F", "F#/G\u266D", "G", "G#/A\u266D", "A", "A#/B\u266D", "B"};
	
	public static final Map<String, Integer> NOTE_NAME_MAP = new HashMap<>();
	{
		NOTE_NAME_MAP.put("C", 0);
		NOTE_NAME_MAP.put("C#", 1);
		NOTE_NAME_MAP.put("D", 2);
		NOTE_NAME_MAP.put("D#", 3);
		NOTE_NAME_MAP.put("E", 4);
		NOTE_NAME_MAP.put("F", 5);
		NOTE_NAME_MAP.put("F#", 6);
		NOTE_NAME_MAP.put("G", 7);
		NOTE_NAME_MAP.put("G#", 8);
		NOTE_NAME_MAP.put("A", 9);
		NOTE_NAME_MAP.put("A#", 10);
		NOTE_NAME_MAP.put("B", 11);
		NOTE_NAME_MAP.put("Db", 1);
		NOTE_NAME_MAP.put("Eb", 3);
		NOTE_NAME_MAP.put("Gb", 6);
		NOTE_NAME_MAP.put("Ab", 8);
		NOTE_NAME_MAP.put("Bb", 10);
	}
	
	public static final Note E = new Note(40);
	public static final Note F = new Note(41);
	public static final Note F_SHARP = new Note(42);
	public static final Note G_FLAT = new Note(42);
	public static final Note G = new Note(43);
	public static final Note G_SHARP = new Note(44);
	public static final Note A_FLAT = new Note(44);
	public static final Note A = new Note(45);
	public static final Note A_SHARP = new Note(46);
	public static final Note B_FLAT = new Note(46);
	public static final Note B = new Note(47);
	public static final Note C = new Note(48);
	public static final Note C_SHARP = new Note(49);
	public static final Note D_FLAT= new Note(49);
	public static final Note D = new Note(50);
	public static final Note D_SHARP = new Note(51);
	public static final Note E_FLAT = new Note(51);
	
	public static final Note[] STANDARD_NOTES = new Note[] {E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B, C, C_SHARP, D, D_SHARP}; 

	private final int value;
	
	public static Note getNote(String searchString) {
		String strPattern = "([ABCDEFG])([#b])?([0-9]+)?";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(searchString);
		if (matcher.find()) {
			String name = matcher.group(1);
			int nameVal = NOTE_NAME_MAP.get(name);
			String sharpFlat = matcher.group(2);
			int sharpVal = sharpFlat == null ? 0 : (sharpFlat.equals("#") ? 1 : -1);
			String num = matcher.group(3);
			int octaveVal = num == null ? 48 : Integer.valueOf(num);
			return new Note(nameVal + sharpVal + octaveVal);
		}
		
		throw new IllegalArgumentException("Note name not properly formatted");
	}

	public static List<Note> getNoteRange(int start, int fin) {
		List<Note> notes = new ArrayList<>();
		for (int i = start ; i < fin ; i++) {
			notes.add(new Note(i));
		}
		return notes;
	}
	
	public static Note getRandomStandardNote() {
		return new Note((int)(40 + Math.random() * 12));
	}
	
	public static Note getRandomGuitarNote() {
		return new Note((int)(40 + Math.random() * 12));
	}

	
	
	
	public Note(int val) {
		assert (val >= 0 && val < 1024);
		this.value = val;
	}
	
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Note)) {
			return false;
		}
		
		Note otherNote = (Note) other;
		return otherNote.value == this.value;
	}
	
	public String longName() {
		return String.format("%2s%d", getNoteName(), value / 12);
	}

	public String toString() {
		return String.format("%s", getNoteName());
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
	
	public String getNoteLabel() {
		return NOTE_LABELS[value % INTERVALS_IN_CHROMATIC];
	}
	
	public static void maing(String[] args) {
		for (int i = 0 ; i < 100 ; i++) {
			System.out.println(new Note(i).getNoteLabel());
		}
		
		for (String name : NOTE_NAMES) {
			System.out.println(getNote(name));
		}
	}
}
