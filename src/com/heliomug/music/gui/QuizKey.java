package com.heliomug.music.gui;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.heliomug.music.Chord;
import com.heliomug.music.Key;
import com.heliomug.music.KeyType;
import com.heliomug.music.Note;

public class QuizKey extends QuizBoard {
	private static final long serialVersionUID = -359077746517787753L;

	private JComboBox<Note> rootSelector;
	private JComboBox<KeyType> typeSelector;

	private Chord lastPlayed;
	
	public QuizKey() {
		super();
		
		lastPlayed = null;
		
		setFocusable(false);
		
		setLayout(new FlowLayout());

		add(new JLabel("Key: "));
		rootSelector = new JComboBox<>(Note.STANDARD_NOTES);
		add(rootSelector);
		typeSelector = new JComboBox<>(KeyType.values());
		add(typeSelector);
	}

	public void playNew() {
		lastPlayed = getRandomChord();
		playChord(lastPlayed);
	}
	
	@Override
	public void repeat() {
		playChord(lastPlayed);
	}

	public Key getKey() {
		return new Key((Note)rootSelector.getSelectedItem(), (KeyType)typeSelector.getSelectedItem());
	}
	
	public Chord getRandomChord() {
		Key key = getKey();
		List<Chord> chords = key.getChords();
		Chord chord = chords.get((int)(Math.random() * chords.size()));
		return chord;
	}
	
}
