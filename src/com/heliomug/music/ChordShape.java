package com.heliomug.music;

import java.util.List;
import java.util.ArrayList;

public enum ChordShape {
    E       (new int[] {0, 2, 2, 1, 0, 0}, Note.E, ChordType.MAJ, "E"),
    E_MIN   (new int[] {0, 2, 2, 0, 0, 0}, Note.E, ChordType.MIN, "Em"),
    E7      (new int[] {0, 2, 0, 1, 0, 0}, Note.E, ChordType.DOM_7, "E7"),
    Em7     (new int[] {0, 2, 0, 0, 0, 0}, Note.E, ChordType.MIN_7, "Em7"),
    EM7     (new int[] {0, -1, 1, 1, 0, -1}, Note.E, ChordType.MAJ_7, "EM7"),
    A       (new int[] {-1, 0, 2, 2, 2, 0}, Note.A, ChordType.MAJ, "A"),
    A_MIN   (new int[] {-1, 0, 2, 2, 1, 0}, Note.A, ChordType.MIN, "Am"),
    A7      (new int[] {-1, 0, 2, 0, 2, 0}, Note.A, ChordType.DOM_7, "A7"),
    Am7     (new int[] {-1, 0, 2, 0, 1, 0}, Note.A, ChordType.MIN_7, "Am7"),
    AM7     (new int[] {-1, 0, 2, 1, 2, 0}, Note.A, ChordType.MAJ_7, "AM7"),
    D       (new int[] {-1, -1, 0, 2, 3, 2}, Note.D, ChordType.MAJ, "D"),
    D_MIN   (new int[] {-1, -1, 0, 2, 3, 1}, Note.D, ChordType.MIN, "Dm"),
    D7      (new int[] {-1, -1, 0, 2, 1, 2}, Note.D, ChordType.DOM_7, "D7"),
    Dm7     (new int[] {-1, -1, 0, 2, 1, 1}, Note.D, ChordType.MIN_7, "Dm7"),
    DM7     (new int[] {-1, -1, 0, 2, 2, 2}, Note.D, ChordType.MAJ_7, "DM7"),
    G       (new int[] {3, 2, 0, 0, 0, 3}, Note.G, ChordType.MAJ, "G"),
    G7      (new int[] {3, 2, 0, 0, 0, 1}, Note.G, ChordType.DOM_7, "G7"),
    C       (new int[] {-1, 3, 2, 0, 1, 0}, Note.C, ChordType.MAJ, "C"),
    C7      (new int[] {-1, 3, 2, 3, 1, 0}, Note.C, ChordType.DOM_7, "C7");

    private List<Integer> offsets;
    private ChordType type;
    private String abbrev;
    private Note defaultRoot;

    private ChordShape(int[] frets, Note dr, ChordType type, String abbrev) {
        int[] GUITAR_OFFSETS = new int[] {0, 5, 10, 15, 19, 24};
        this.defaultRoot = dr;
        this.type = type;
        this.abbrev = abbrev;

        this.offsets = new ArrayList<>();
        int baseString = 0;
        for (int i = 0 ; i < frets.length ; i++) {
            if (frets[i] != -1) {
                baseString = i;
                break;
            }
        }
        for (int i = baseString ; i < frets.length ; i++) {
            int offset = GUITAR_OFFSETS[i] + frets[i];
            offset -= GUITAR_OFFSETS[baseString] + frets[baseString];
            offsets.add(offset);
        }
    }

    public static ChordShape getShape(Chord chord) {
        return getShape(chord, 0);
    }

    public static ChordShape getShape(Chord chord, int minFret) {
        ChordType type = chord.getType();
        Note root = chord.getRoot();

        if (type == null) {
            return null;
        }
        
        ChordShape bestShape = null;
        int fretsAway = Integer.MAX_VALUE;

        for (ChordShape shape : ChordShape.values()) {
            if (type == shape.type) {
                int dist = shape.defaultRoot.distanceTo(root);
                dist = (dist + 12) % 12;
                if (dist >= minFret && dist < fretsAway) {
                    fretsAway = dist;
                    bestShape = shape;
                }
            }
        }

        return bestShape;
    }

    public static Chord fillChord(Chord chord) {
        ChordShape shape = getShape(chord);
        if (shape == null) {
            return chord;
        } else {
            Note newRoot = shape.defaultRoot.nextAbove(chord.getRoot());
            return shape.makeChord(newRoot);
        }
    }

    public Chord makeChord(Note base) {
        Chord chord = new Chord();
        for (int offset : offsets) {
            chord.addNote(base.getHigher(offset));
        }

        return chord;
    }
}
