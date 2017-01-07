package com.heliomug.music.trainer;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.Key;
import com.heliomug.music.KeyType;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;
import com.heliomug.music.StdInstrument;
import com.heliomug.utils.gui.ComboBoxSelector;

public class TabKey extends TabPanel {
	private static final long serialVersionUID = -359077746517787753L;

	private static final int DRONE_CHANNEL = 3;
	
	private Key key = new Key(QuizOptions.DEFAULT_ROOT_NOTE, QuizOptions.DEFAULT_KEY_TYPE);
	private List<Chord> chords = new Key(Note.C, KeyType.MAJOR).getChords();
	private Chord lastPlayed;
	private Drone drone;

	public TabKey() {
		super();
		
		lastPlayed = null;
		chords = null;
		drone = null;
		
		updateKey();

		MidiPlayer.setChannel(DRONE_CHANNEL, QuizOptions.getOptions().getDroneInstrument());
	}

	@Override
	public void paint(Graphics g) {
		updateDrone();
		super.paint(g);
	}
	
	public JPanel getStatusPanel() {
		JPanel panel = new JPanel();
		
		@SuppressWarnings("serial")
		JCheckBox guitBox = new JCheckBox("Guitarify Chords") {
			@Override
			public void paint(Graphics g) {
				this.setSelected(QuizOptions.getOptions().isGuitarChords());
				super.paint(g);
			}
		};
		guitBox.addActionListener((ActionEvent e) -> {
			QuizOptions.getOptions().setGuitarChords(guitBox.isSelected());
		});
		panel.add(guitBox);

		return panel;
	}
	
	public JPanel getOptionPanel() {
		JPanel panel = new EtchedPanel("Key");
		panel.setLayout(new GridLayout(0, 1));
		
		JPanel subpanel = new JPanel();
		subpanel.add(new JLabel("Key: "));
		subpanel.add(new ComboBoxSelector<Note>( 
				Note.getNoteRange(48, 61),
				(Note n) -> {
					key.setRoot(n);
					updateKey();
				}
		));
		subpanel.add(new ComboBoxSelector<KeyType>(
				Arrays.asList(KeyType.values()),
				(KeyType type) -> {
					key.setType(type);
					updateKey();
				}
		));
		panel.add(subpanel);

		return panel;
	}
	
	public JPanel getResponsePanel() {
		@SuppressWarnings("serial")
		JPanel responsePanel = new EtchedPanel("Responses") {
			@Override 
			public void paint(Graphics g) {
				updateResponsePanel(this);
				super.paint(g);
			}
		};
		
		return responsePanel;
	}
	
	public void updateResponsePanel(JPanel responsePanel) {
		responsePanel.removeAll();
		chords = key.getChords();
		
		JPanel subpanel;
		
		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(0, 1));
		for (Chord chord : chords) {
			subpanel.add(new ResponseButton(chord));
		}
		responsePanel.add(subpanel, BorderLayout.CENTER);
		
		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(0, 1));
		for (Chord chord : chords) {
			subpanel.add(new DemoButton(chord));
		}
		responsePanel.add(subpanel, BorderLayout.EAST);
		revalidate();
	}
	
	
	@Override
	public void blur() {
		killDrone();
	}
	
	@Override
	public void focus() {
		updateDrone();
	}
	
	
	public void playNew() {
		lastPlayed = getRandomChord();
		MusicPlayer.playChord(lastPlayed, getOptions().getDelay());
		strikeDroneNote(drone);
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			MusicPlayer.playChord(lastPlayed, getOptions().getDelay());
		}
		super.repeat();
	}

	private Chord getRandomChord() {
		return chords.get((int)(Math.random() * chords.size()));
	}

	private void receiveResponse(Chord chord) {
		if (lastPlayed != null) {
			boolean typeRight = chord.getType() == lastPlayed.getType();
			boolean rootRight = chord.getRoot() == lastPlayed.getRoot();
			if (typeRight && rootRight) {
				answerCorrect(chord.getShortName());
			} else {
				answerWrong();
			}
			repaint();
		}
	}

	private void killDrone() {
		if (drone != null) drone.isOn = false;
		MidiPlayer.notesOff(DRONE_CHANNEL);
	}
	
	private void strikeDroneNote(Drone drone) {
		if (drone.isOn) {
			MidiPlayer.noteOn(DRONE_CHANNEL, drone.root, drone.volume);
		}
	}
	
	private void updateDrone() {
		Drone newDrone = getCurrentDrone();
		if (newDrone.isOn) {
			if (!newDrone.equals(drone)) {
				killDrone();
				strikeDroneNote(newDrone);
			} 
		} else {
			killDrone();
		}
		drone = newDrone;
	}
	
	private void updateKey() {
		updateDrone();
		repaint();
	}
	
	
	public Drone getCurrentDrone() {
		QuizOptions opt = QuizOptions.getOptions();
		boolean on = opt.isDroneOn();
		Note root = key.getRoot();
		StdInstrument instrument = opt.getDroneInstrument();
		int vol = opt.getDroneVolume();
		return new Drone(instrument, root, vol, on);
	}
	
	@SuppressWarnings("serial")
	private class DemoButton extends JButton {
		public DemoButton(Chord chord) {
			super("hear");
			setFocusable(false);
			addActionListener((ActionEvent e) -> MusicPlayer.playChord(chord, getOptions().getDelay()));
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
	
	private class Drone {
		int volume;
		StdInstrument instrument;
		Note root;
		boolean isOn;
		
		public Drone(StdInstrument instrument, Note root, int vol, boolean isOn) {
			this.instrument = instrument;
			this.volume = vol;
			this.root = root;
			this.isOn = isOn;
		}
		
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (!(other instanceof Drone)) {
				return false;
			}
			Drone otherDrone = (Drone) other;
			return (otherDrone.root.equals(root) && otherDrone.volume == volume && otherDrone.instrument == instrument && otherDrone.isOn == isOn);
		}
	}
}
