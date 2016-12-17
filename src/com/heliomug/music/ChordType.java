package com.heliomug.music;

public enum ChordType {
    MAJOR("Major", "", new int[] {0, 4, 7}),
    MINOR("Minor", "m", new int[] {0, 3, 7});

    private String name;
    private String abbrev;
    private int[] offsets;

    private ChordType(String name, String abbrev, int[] offsets) {
        this.name = name;
        this.abbrev = abbrev;
        this.offsets = offsets;
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

    public Chord constructChord(Note base) {
        Chord c = new Chord();
        for (int offset : offsets) {
            c.addNote(base.getHigher(offset));
        }

        return c;
    }
}
