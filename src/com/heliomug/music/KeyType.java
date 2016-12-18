package com.heliomug.music;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public enum KeyType {
    MAJOR("Major", new int[] {0, 2, 4, 5, 7, 9, 11}),
    MINOR("Minor", new int[] {0, 2, 3, 5, 7, 8, 10});

    private String name;
    private int[] offsets;

    private KeyType(String name, int[] offsets) {
        this.name = name;
        this.offsets = offsets;
    }

    public String getName() {
        return this.name;
    }

    public int[] getOffsets() {
        return this.offsets;
    }

    public String toString() {
        return this.name + ": " + Arrays.toString(offsets);
    }
}
