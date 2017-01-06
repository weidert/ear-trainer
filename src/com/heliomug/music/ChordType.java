package com.heliomug.music;

import java.util.Set;
import java.util.HashSet;

public enum ChordType {
    MAJ("Major", "", "Maj", new int[] {0, 4, 7}, true),
    MIN("Minor", "m", "Min", new int[] {0, 3, 7}, true),
    AUG("Augmented", "+", "Aug", new int[] {0, 4, 8}, false),
    DIM("Diminished", "\u00b0", "Dim", new int[] {0, 3, 6}, false),
    DIM_7("Diminished 7th", "\u00b07", "Dim7", new int[] {0, 3, 6, 9}, false),
    HALF_DIM("Half-diminished 7th", "\u00D87", "hDim7", new int[] {0, 3, 6, 10}, false),
    MIN_7("Minor 7th", "m7", "Min7", new int[] {0, 3, 7, 10}, true),
    MIN_MAJ_7("Min Maj 7th", "mM7", "mM7", new int[] {0, 3, 7, 11}, false),
    DOM_7("7th", "7", "7th", new int[] {0, 4, 7, 10}, true),
    MAJ_7("Major 7th", "\u03947", "Maj7", new int[] {0, 4, 7, 11}, true),
    AUG_7("Augmented 7th", "+7", "Aug7", new int[] {0, 4, 6, 10}, false),
    AUG_MAJ_7("Augmented Major 7th", "+M7", "+M7", new int[] {0, 4, 8, 11}, false);

    private String name;
    private String shortName;
    private String abbrev;
    private int[] offsets;
    private boolean common;

    private ChordType(String name, String abbrev, String shortName, int[] offsets, boolean common) {
        this.name = name;
        this.shortName = shortName;
        this.abbrev = abbrev;
        this.offsets = offsets;
        this.common = common;
    }

    public String getName() {
        return this.name;
    }
    
    public String getShortName() {
    	return this.shortName;
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
