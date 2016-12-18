package com.heliomug.music;

import java.util.Set;
import java.util.HashSet;

public enum ChordType {
    MAJ("Major", "", new int[] {0, 4, 7}, true),
    MIN("Minor", "m", new int[] {0, 3, 7}, true),
    AUG("Augmented", "+", new int[] {0, 4, 8}, false),
    DIM("Diminished", "\u00b0", new int[] {0, 3, 6}, false),
    DIM_7("Diminished 7th", "\u00b07", new int[] {0, 3, 6, 9}, false),
    HALF_DIM("Half-diminished 7th", "\u00D87", new int[] {0, 3, 6, 10}, false),
    MIN_7("Minor 7th", "m7", new int[] {0, 3, 7, 10}, true),
    MIN_MAJ_7("Min Maj 7th", "mM7", new int[] {0, 3, 7, 11}, false),
    DOM_7("7th", "7", new int[] {0, 4, 7, 10}, true),
    MAJ_7("Major 7th", "\u03947", new int[] {0, 4, 7, 11}, true),
    AUG_7("Augmented 7th", "+7", new int[] {0, 4, 6, 10}, false),
    AUG_MAJ_7("Augmented Major 7th", "+M7", new int[] {0, 4, 8, 11}, false);

    private String name;
    private String abbrev;
    private int[] offsets;
    private boolean common;

    private ChordType(String name, String abbrev, int[] offsets, boolean common) {
        this.name = name;
        this.abbrev = abbrev;
        this.offsets = offsets;
        this.common = common;
    }

    public String getName() {
        return this.name;
    }

    public String getAbbrev() {
        return this.abbrev;
    }

    public int[] getOffsets() {
        return this.offsets;
    }

    public Set<Integer> getOffsetSet() {
        Set<Integer> offsetSet = new HashSet<>();
        for (int offset : offsets) {
            offsetSet.add(offset % Note.INTERVALS_IN_CHROMATIC);
        }
        return offsetSet;
    }

    public boolean isCommon() {
        return this.common;
    }

    public Chord constructChord(Note base) {
        Chord c = new Chord();
        for (int offset : offsets) {
            c.addNote(base.getHigher(offset));
        }

        return c;
    }
}
