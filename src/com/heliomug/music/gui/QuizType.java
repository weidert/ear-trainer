package com.heliomug.music.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordType;
import com.heliomug.music.Note;

public class QuizType extends QuizBoard {
	private static final long serialVersionUID = 3177499161338508351L;

	private static final Set<ChordType> STANDARD_TYPES = new HashSet<ChordType>();
	{
		STANDARD_TYPES.add(ChordType.MAJ);
		STANDARD_TYPES.add(ChordType.MIN);
		STANDARD_TYPES.add(ChordType.MIN_7);
		STANDARD_TYPES.add(ChordType.DOM_7);
		STANDARD_TYPES.add(ChordType.MAJ_7);
	}
	
	private Set<ChordType> activeTypes;

	private Chord lastPlayed;

	public QuizType() {
		activeTypes = new HashSet<>();
		lastPlayed = null;
		
		JPanel panel;
		
		// toggle for active chords
		panel = new JPanel();
		panel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			panel.add(new ChordTypeToggleBox(type));
		}
		add(panel, BorderLayout.WEST);
		
		// play buttons
		panel = new JPanel();
		panel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			panel.add(new ChordPlayButton(type)); 
		}
		add(panel, BorderLayout.CENTER);
		
		// response buttons
		panel = new JPanel();
		panel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			panel.add(new ChordResponseButton(type)); 
		}
		add(panel, BorderLayout.EAST);
		
		// scoreboard
	}

	
	@Override
	public void playNew() {
		Note n = Note.C; //Note.getRandomGuitarNote();
		Chord toPlay = getRandomChordType().constructChord(n);
		lastPlayed = toPlay;
		playChord(toPlay);
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			playChord(lastPlayed);
		}
	}
	
	private void playWithType(ChordType type) {
		if (lastPlayed != null) {
			playChord(type.constructChord(lastPlayed.getRoot()));
		}
	}
	
	private ChordType getRandomChordType() {
		// not the most efficient;
		int ind = (int)(Math.random() * activeTypes.size());
		int i = 0;
		for (ChordType type : activeTypes) {
			if (i == ind) {
				return type;
			}
			i++;
		}
		return null;
	}
	
	private void receiveResponse(ChordType type) {
		if (lastPlayed != null) {
			if (type == lastPlayed.getType()) {
				answerCorrect();
				lastPlayed = null;
			} else {
				answerWrong();
				System.out.println("no; " + lastPlayed.getType());
			}
			repaint();
		}
	}
	
	
	private class ChordResponseButton extends JButton {
		private static final long serialVersionUID = -5311484929712227987L;

		public ChordResponseButton(ChordType type) {
			super(type.getName());
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				receiveResponse(type);
			});
		}
	}
	
	private class ChordPlayButton extends JButton {
		private static final long serialVersionUID = -5311484929712227987L;

		public ChordPlayButton(ChordType type) {
			super(type.getName());
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				playWithType(type);
			});
		}
	}
	
	private class ChordTypeToggleBox extends JCheckBox {
		private static final long serialVersionUID = -4909498331879261312L;

		public ChordTypeToggleBox(ChordType type) {
			super(type.getName());
			setFocusable(false);
			if (STANDARD_TYPES.contains(type)) {
				setSelected(true);
				activeTypes.add(type);
			} else {
				setSelected(false);
			}
			addActionListener((ActionEvent e) -> {
				if (isSelected()) {
					activeTypes.add(type);
				} else {
					activeTypes.remove(type);
				}
			});
		}
	}
}