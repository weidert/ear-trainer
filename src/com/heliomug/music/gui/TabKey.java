package com.heliomug.music.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.Key;
import com.heliomug.music.KeyType;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;

public class TabKey extends TabPanel {
	private static final long serialVersionUID = -359077746517787753L;

	private static final int DRONE_CHANNEL = 3;
	private static final int DRONE_VOLUME = 1;
	private static final StandardInstrument DRONE_INSTRUMENT = StandardInstrument.ORGAN_CHURCH; 
	
	private JComboBox<Note> rootSelector;
	private JComboBox<KeyType> typeSelector;

	private List<Chord> chords = new Key(Note.C, KeyType.MAJOR).getChords();
	private Chord lastPlayed;
	
	private boolean isDroneOn;
	
	public TabKey() {
		super();
		
		MidiPlayer.setChannel(DRONE_CHANNEL, DRONE_INSTRUMENT);
		
		isDroneOn = false;
		lastPlayed = null;
		chords = null;
		
		updateKey();
		
	}

	public JPanel getOptionPanel() {
		JPanel panel = new EtchedPanel("Options");
		panel.setLayout(new GridLayout(0, 1));
		
		ActionListener al;
		
		al = (ActionEvent e) -> updateKey();
		JPanel subpanel = new JPanel();
		subpanel.add(new JLabel("Key: "));
		rootSelector = new JComboBox<>(Note.STANDARD_NOTES);
		rootSelector.addActionListener(al);
		subpanel.add(rootSelector);
		typeSelector = new JComboBox<>(KeyType.values());
		typeSelector.addActionListener(al);
		subpanel.add(typeSelector);
		panel.add(subpanel);

		updateKey();
		
		return panel;
	}
	
	@SuppressWarnings("serial")
	public JPanel getResponsePanel() {
		JPanel panel = new EtchedPanel("Responses");
		JPanel subpanel;
		
		subpanel = new JPanel() {
			{
				add(new JLabel("placeholder"));
				setLayout(new GridLayout(0, 1));
			}
		
			@Override
			public void paint(Graphics g) {
				removeAll();
				for (Chord chord : chords) {
					this.add(new ResponseButton(chord));
				}
				super.paint(g);
			}
		};
		panel.add(subpanel, BorderLayout.WEST);
		
		subpanel = new JPanel() {
			{
				add(new JLabel("placeholder"));
				setLayout(new GridLayout(0, 1));
			}
		
			@Override
			public void paint(Graphics g) {
				removeAll();
				for (Chord chord : chords) {
					this.add(new DemoButton(chord));
				}
				super.paint(g);
			}
		};
		panel.add(subpanel, BorderLayout.EAST);
		
		return panel;
	}
	
	public void playNew() {
		lastPlayed = getRandomChord();
		MusicPlayer.playChord(lastPlayed);
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			MusicPlayer.playChord(lastPlayed);
		}
		super.repeat();
	}

	public Note getRoot() {
		return (Note)rootSelector.getSelectedItem();
	}
	
	public Key getKey() {
		return new Key(getRoot(), (KeyType)typeSelector.getSelectedItem());
	}
	
	private Chord getRandomChord() {
		return chords.get((int)(Math.random() * chords.size()));
	}

	private void receiveResponse(Chord chord) {
		if (lastPlayed != null) {
			boolean typeRight = chord.getType() == lastPlayed.getType();
			boolean rootRight = chord.getRoot() == lastPlayed.getRoot();
			if (typeRight && rootRight) {
				answerCorrect();
			} else {
				answerWrong();
			}
			repaint();
		}
	}
	
	private void updateKey() {
		MidiPlayer.notesOff(DRONE_CHANNEL);
		if (isDroneOn) {
			MidiPlayer.noteOn(DRONE_CHANNEL, getRoot(), DRONE_VOLUME);
		} 
		chords = getKey().getChords();
		repaint();
	}
	
	@SuppressWarnings("serial")
	private class DemoButton extends JButton {
		public DemoButton(Chord chord) {
			super("hear");
			setFocusable(false);
			addActionListener((ActionEvent e) -> MusicPlayer.playChord(chord));
		}
	}
	
	@SuppressWarnings("serial")
	private class ResponseButton extends JButton {
		public ResponseButton(Chord chord) {
			super(chord.getShortName());
			setFocusable(false);
			addActionListener((ActionEvent e) -> receiveResponse(chord));
		}
	}
	
}
