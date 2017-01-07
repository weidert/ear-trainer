package com.heliomug.music.trainer;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordType;
import com.heliomug.music.Note;
import com.heliomug.utils.gui.UpdatingCheckBox;

public class TabType extends TabPanel {
	private static final long serialVersionUID = 3177499161338508351L;

	private static final int[] SHORTCUTS = new int[] {
			KeyEvent.VK_M, 
			KeyEvent.VK_N, 
			KeyEvent.VK_A, 
			KeyEvent.VK_D, 
			KeyEvent.VK_B, 
			KeyEvent.VK_H, 
			KeyEvent.VK_C, 
			KeyEvent.VK_E, 
			KeyEvent.VK_7, 
			KeyEvent.VK_J, 
			KeyEvent.VK_F, 
			KeyEvent.VK_EQUALS, 
	};
	
	private static final Set<ChordType> STANDARD_TYPES = new HashSet<>(Arrays.asList(new ChordType[] {
		ChordType.MAJ,
		ChordType.MIN,
		ChordType.MIN_7,
		ChordType.DOM_7,
		ChordType.MAJ_7,
	}));
	
	private Set<ChordType> activeTypes;

	private Chord lastPlayed;

	public TabType() {
		activeTypes = new HashSet<>();
		for (ChordType type : ChordType.values()) {
			if (STANDARD_TYPES.contains(type)) {
				activeTypes.add(type);
			}
		}
		lastPlayed = null;
	}

	@SuppressWarnings("serial")
	@Override
	public JPanel getTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel subpanel;

		panel.add(new JLabel("Instrument: ") {
			@Override
			public void paint(Graphics g) {
				setText("Instrument: " + QuizOptions.getOptions().getInstrument().getShortName());
				super.paint(g);
			}
		}, BorderLayout.NORTH);
		
		subpanel = new JPanel();
		subpanel.add(new UpdatingCheckBox(
				"Guitarify Chords",
				(Boolean b) -> QuizOptions.getOptions().setGuitarChords(b),
				() -> QuizOptions.getOptions().isGuitarChords()
		));

		subpanel.add(new UpdatingCheckBox(
				"Constant Root",
				(Boolean b) -> QuizOptions.getOptions().setConstantRoot(b),
				() -> QuizOptions.getOptions().isConstantRoot()
		));
		panel.add(subpanel, BorderLayout.SOUTH);

		return panel;
	}
	
	@Override
	public JPanel getLeftPanel() {
		JPanel panel = new EtchedPanel("Chord Types");
		panel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			JCheckBox box = new JCheckBox(type.getName());
			box.setFocusable(false);
			box.addActionListener((ActionEvent e) -> {
				if (box.isSelected()) {
					activeTypes.add(type);
				} else {
					activeTypes.remove(type);
				}
			});
			panel.add(box);
			if (STANDARD_TYPES.contains(type)) {
				box.setSelected(true);
			};
		}
		return panel;
	}
	
	@Override
	public JPanel getRightPanel() {
		JPanel panel = new EtchedPanel("Reponses");

		JPanel subpanel;
		
		// response buttons
		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (int i = 0 ; i < ChordType.values().length ; i++) {
			ChordType type = ChordType.values()[i];
			char shortcut = (char)SHORTCUTS[i % SHORTCUTS.length];
			subpanel.add(new ChordResponseButton(type, shortcut)); 
		}
		panel.add(subpanel, BorderLayout.WEST);

		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			subpanel.add(new ChordDemoButton(type)); 
		}
		panel.add(subpanel, BorderLayout.EAST);
		
		return panel;
	}
	
	
	@Override
	public void playNew() {
		Note n;
		boolean isConstantRoot = QuizOptions.getOptions().isConstantRoot();
		if (isConstantRoot) {
			n = QuizOptions.getOptions().getConstantRoot();
		} else {
			n = Note.getRandomGuitarNote();
		}
		if (activeTypes.size() > 0) {
			Chord toPlay = getRandomChordType().constructChord(n);
			lastPlayed = toPlay;
			MusicPlayer.playChord(toPlay, getOptions().getDelay());
			super.playNew();
		} else {
			JOptionPane.showMessageDialog(QuizFrame.getTheFrame(), "Please pick at least one type of chord");
		}
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			MusicPlayer.playChord(lastPlayed, getOptions().getDelay());
		}
		super.repeat();
	}
	
	private void playWithType(ChordType type) {
		if (lastPlayed != null) {
			int delay = getOptions().getDelay();
			Chord chord = type.constructChord(lastPlayed.getRoot());
			MusicPlayer.playChord(chord, delay);
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
				answerCorrect(type.getName());
			} else {
				answerWrong();
			}
			repaint();
		}
	}
	
	
	private class ChordResponseButton extends JButton {
		private static final long serialVersionUID = -5311484929712227987L;

		public ChordResponseButton(ChordType type, int key) {
			super(String.format("%s (%s)", type.getShortName(), (char)key));
			setFocusable(false);
			setMnemonic((char)key);
			addActionListener((ActionEvent e) -> {
				receiveResponse(type);
			});
		}
	}
	
	private class ChordDemoButton extends JButton {
		private static final long serialVersionUID = -5311484929712227987L;

		public ChordDemoButton(ChordType type) {
			super("hear");
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				playWithType(type);
			});
		}
	}
}