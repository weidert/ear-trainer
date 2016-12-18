package com.heliomug.music;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class Chord {
    List<Note> notes;

    public Chord() {
        notes = new ArrayList<>();
    }

    public void addNote(Note n) {
        notes.add(n);
    }

    public void play(int channel, int vol) {
        for (Note n : notes) {
            MidiPlayer.noteOn(channel, n, vol);
        }
    }

    public Note getRoot() {
        if (notes.size() < 1) {
            return null;
        }

        return notes.get(0);
    }

    public ChordType getType() {
        if (notes.size() < 1) {
            return null;
        }
        Set<Integer> offsets = new HashSet<>();
        Note base = notes.get(0);
        for (Note n : notes) {
            offsets.add(n.difference(base) % Note.INTERVALS_IN_CHROMATIC);
        }

        for (ChordType type : ChordType.values()) {
            if (type.getOffsetSet().equals(offsets)) {
                return type;
            }
        }

        return null;
    }

    public String getShortName() {
        if (notes.size() < 1) {
            return "";
        } 
        String base = notes.get(0).getNoteName();
        ChordType type = getType();
        String typeString = type == null ? "?" : type.getAbbrev();
        return base + typeString;
    }

    @Override
    public String toString() {
        if (notes.size() < 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getShortName() + ": [" + notes.get(0));
        for (int i = 1 ; i < notes.size() ; i++) {
            sb.append(", " + notes.get(i));
        }
        sb.append("]");

        return sb.toString();
    }
}
