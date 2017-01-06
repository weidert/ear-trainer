package com.heliomug.music;

import com.heliomug.music.gui.MusicPlayer;

public class MainMusic {
    public static void playChord(Chord chord) {
        System.out.println(chord);
        MusicPlayer.playChord(chord);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // whatever;
        }
        MidiPlayer.notesOff(1);
    }

    public static void main(String[] args) {
        System.out.println("Starting");
        StandardInstrument guitar = StandardInstrument.GUITAR_STEEL;
        MidiPlayer.setChannel(1, guitar);

        /*
        for (ChordType type : ChordType.values()) {
            Chord chord = type.constructChord(Note.G);
            if (chord.getType().isCommon()) {
                playChord(chord);
            }
        }
        */

        Key key = new Key(Note.G, KeyType.MAJOR);
        for (Chord chord : key.getChords()) {
            playChord(ChordShape.fillChord(chord));
        }
        playChord(ChordShape.fillChord(key.getChords().get(0)));
    }
}
