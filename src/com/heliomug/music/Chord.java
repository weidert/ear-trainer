package com.heliomug.music;

import java.util.List;
import java.util.ArrayList;

public class Chord {
    List<Note> notes;

    public Chord() {
        notes = new ArrayList<>();
    }

    public void addNote(Note n) {
        notes.add(n);
    }

    public void play(int channel, int vol, double dur) {
        Thread t = new Thread(() -> {
            for (Note n : notes) {
                MidiPlayer.noteOn(channel, n, vol);
            }
            try {
                Thread.sleep((int)(dur * 1000));
            } catch (InterruptedException e) {
                // if we get interrupted, just stop the music, I guess
            }
            MidiPlayer.notesOff(channel);
        });
        t.start();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + notes.get(0));
        for (int i = 1 ; i < notes.size() ; i++) {
            sb.append(", " + notes.get(i));
        }
        sb.append("]");

        return sb.toString();
    }
}
