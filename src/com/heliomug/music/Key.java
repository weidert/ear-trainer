package com.heliomug.music;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Key {
    KeyType type;
    Note base;

    public Key(Note base, KeyType type) {
        this.type = type;
        this.base = base;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        for (int offset : type.getOffsets()) {
            notes.add(base.getHigher(offset));
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
            c.addNote(base.getHigher(first));
            c.addNote(base.getHigher(second));
            c.addNote(base.getHigher(third));
            chords.add(c);
        }
        return chords;
    }

    public String toString() {
        return String.format("%s %s: %s", base, type.getName(), getNotes());
    }
}
