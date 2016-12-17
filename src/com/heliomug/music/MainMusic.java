package com.heliomug.music;

public class MainMusic {
    public static void main(String[] args) {
        System.out.println("Starting");
        StandardInstrument guitar = StandardInstrument.GUITAR_STEEL;
        MidiPlayer.setChannel(1, guitar);
        Chord chord = ChordType.MAJOR.constructChord(Note.G);
        chord.play(1, 100, 1.0);
    }
}
