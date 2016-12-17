package com.heliomug.music;

public class MainMusic {
    public static void main(String[] args) {
        System.out.println("Starting");
        StandardInstrument guitar = StandardInstrument.GUITAR_STEEL;
        MidiPlayer.setChannel(1, guitar);
        for (ChordType type : ChordType.values()) {
            Chord chord = type.constructChord(Note.G);
            if (chord.getType().isCommon()) {
                System.out.println(chord);
                chord.play(1, 100);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // whatever;
                }
                MidiPlayer.notesOff(1);
            }
        }
    }
}
