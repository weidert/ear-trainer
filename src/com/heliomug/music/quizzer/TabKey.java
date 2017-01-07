package com.heliomug.music.quizzer;

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
	
	private JComboBox<Note> rootSelector;
	private JComboBox<KeyType> typeSelector;

	private List<Chord> chords = new Key(Note.C, KeyType.MAJOR).getChords();
	private Chord lastPlayed;
	
	private Drone drone;

	private JPanel responsePanel;

	public TabKey() {
		super();
		
		MidiPlayer.setChannel(DRONE_CHANNEL, QuizOptions.getOptions().getDroneInstrument());
		
		lastPlayed = null;
		chords = null;
		drone = null;
		
		updateKey();
		
	}

	@Override
	public void paint(Graphics g) {
		updateDrone();
		super.paint(g);
	}
	
	public JPanel getStatusPanel() {
		return new JPanel();
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

		return panel;
	}
	
	public JPanel getResponsePanel() {
		responsePanel = new EtchedPanel("Responses");
		updateResponsePanel();
		return responsePanel;
	}
	
	public void updateResponsePanel() {
		responsePanel.removeAll();
		chords = getKey().getChords();
		
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
		repaint();
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
		strikeDroneNote();
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (lastPlayed != null) {
			MusicPlayer.playChord(lastPlayed, getOptions().getDelay());
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
	
	private void strikeDroneNote() {
		if (drone.isOn) {
			MidiPlayer.noteOn(DRONE_CHANNEL, getRoot(), QuizOptions.getOptions().getDroneVolume());
		}
	}
	
	private void updateDrone() {
		Drone newDrone = getCurrentDrone();
		if (newDrone.isOn) {
			if (!newDrone.equals(drone)) {
				killDrone();
				strikeDroneNote();
			} 
		} else {
			killDrone();
		}
		drone = newDrone;
	}
	
	private void updateKey() {
		updateDrone();
		updateResponsePanel();
	}
	
	
	public Drone getCurrentDrone() {
		QuizOptions opt = QuizOptions.getOptions();
		boolean on = opt.isDroneOn();
		Note root = getRoot();
		StandardInstrument instrument = opt.getDroneInstrument();
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
		StandardInstrument instrument;
		Note root;
		boolean isOn;
		
		public Drone(StandardInstrument instrument, Note root, int vol, boolean isOn) {
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
