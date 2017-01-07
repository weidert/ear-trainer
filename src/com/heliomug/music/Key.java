package com.heliomug.music;

import java.util.ArrayList;
import java.util.List;

public class Key {
    KeyType type;
    Note root;

    public Key(Note root, KeyType type) {
        this.type = type;
        this.root = root;
    }

    public void setRoot(Note n) {
    	this.root = n;
    }
    
    public void setType(KeyType type) {
    	this.type = type;
    }
    
    public Note getRoot() {
    	return this.root;
    }
    
    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        for (int offset : type.getOffsets()) {
            notes.add(root.getHigher(offset));
        }
        return notes;
    }

    public List<Chord> getChords() {
        List<Chord> chords = new ArrayList<>();
        int[] offsets = type.getOffsets();
        for (int i = 0 ; i < offsets.length ; i++) {
            int first = offsets[i];
            int second = offsets[(i + 2) % offsets.length];
            int third = offsets[(i + 4) % offsets.length];
            if (i + 2 >= offsets.length) {
                second += Note.INTERVALS_IN_CHROMATIC;
            }
            if (i + 4 >= offsets.length) {
                third += Note.INTERVALS_IN_CHROMATIC;
            }

            Chord c = new Chord();
            c.addNote(root.getHigher(first));
            c.addNote(root.getHigher(second));
            c.addNote(root.getHigher(third));
            chords.add(c);
        }
        return chords;
    }

    public String toString() {
        return String.format("%s %s: %s", root, type.getName(), getNotes());
    }
}
