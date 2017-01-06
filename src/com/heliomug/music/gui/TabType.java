package com.heliomug.music.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordType;
import com.heliomug.music.Note;

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

	public TabType() {
		activeTypes = new HashSet<>();
		lastPlayed = null;
		
		add(getOptionPanel(), BorderLayout.WEST);
		add(getResponsePanel(), BorderLayout.EAST);
	}

	public JPanel getOptionPanel() {
		JPanel panel = new EtchedPanel("Active Types");
		panel.setLayout(new GridLayout(ChordType.values().length, 1));
		for (ChordType type : ChordType.values()) {
			panel.add(new ChordTypeToggleBox(type));
		}
		return panel;
	}
	
	public JPanel getResponsePanel() {
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
		boolean isConstantRoot = QuizFrame.getOptions().isConstantRoot();
		if (isConstantRoot) {
			n = QuizFrame.getOptions().getConstantRoot();
		} else {
			n = Note.getRandomGuitarNote();
		}
		Chord toPlay = getRandomChordType().constructChord(n);
		lastPlayed = toPlay;
		MusicPlayer.playChord(toPlay);
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			MusicPlayer.playChord(lastPlayed);
		}
		super.repeat();
	}
	
	private void playWithType(ChordType type) {
		if (lastPlayed != null) {
			MusicPlayer.playChord(type.constructChord(lastPlayed.getRoot()));
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