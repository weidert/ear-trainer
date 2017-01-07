package com.heliomug.music.trainer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.Note;

public class TabInterval extends TabPanel {
	private static final long serialVersionUID = 1825689907037878532L;
	
	private Set<Integer> activeIntervals;
	
	private static final String[] INTERVAL_NAMES = new String[] {
			"Unison",
			"Minor 2nd",
			"Major 2nd",
			"Minor 3rd",
			"Major 3rd",
			"Perf 4th",
			"Tritone",
			"Perf 5th",
			"Minor 6th",
			"Major 6th",
			"Minor 7th",
			"Major 7th",
			"Octave"
		};
				
	private static final int[] INTERVAL_KEYS = new int[] {
			KeyEvent.VK_0,
			KeyEvent.VK_1,
			KeyEvent.VK_2,
			KeyEvent.VK_3,
			KeyEvent.VK_4,
			KeyEvent.VK_5,
			KeyEvent.VK_6,
			KeyEvent.VK_7,
			KeyEvent.VK_8,
			KeyEvent.VK_9,
			KeyEvent.VK_A,
			KeyEvent.VK_B,
			KeyEvent.VK_O,
		};
				

	private Note lastA;
	private Note lastB;
	
	public TabInterval() {
		super();
		activeIntervals = new HashSet<>();
		for (int i = 0 ; i <= 12 ; i++) {
			activeIntervals.add(i);
		}
	}
	
	public JPanel getStatusPanel() {
		return new JPanel();
	}
	
	public JPanel getOptionPanel() {
		JPanel panel = new EtchedPanel("Intervals");
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0 ; i <= 12 ; i++) {
			int interval = i;
			JCheckBox box = new JCheckBox(INTERVAL_NAMES[i]);
			box.setFocusable(false);
			box.addActionListener((ActionEvent e) -> {
				if (box.isSelected()) {
					activeIntervals.add(interval);
				} else {
					activeIntervals.remove(interval);
				}
			});
			panel.add(box);
		}
		return panel;
	}

	public JPanel getResponsePanel() {
		JPanel panel = new EtchedPanel("Reponses");
		
		JPanel subpanel;
		
		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(0, 1));
		for (int i = 0 ; i <= 12 ; i++) {
			int key = INTERVAL_KEYS[i];
			String name = INTERVAL_NAMES[i];
			subpanel.add(new IntervalAnswerButton(i, name, key));
		}
		panel.add(subpanel, BorderLayout.CENTER);
		
		subpanel = new JPanel();
		subpanel.setLayout(new GridLayout(0, 1));
		for (int i = 0 ; i <= 12 ; i++) {
			subpanel.add(new IntervalDemoButton(i, INTERVAL_NAMES[i]));
		}
		panel.add(subpanel, BorderLayout.EAST);

		return panel;
	}
	
	private int getRandomInterval() {
		int index = (int)(Math.random() * activeIntervals.size());
		
		int i = 0;
		for (int interval : activeIntervals) {
			if (i == index) {
				return interval;
			}
			i++;
		}
		return -1;
	}
	
	public void playNew() {
		if (activeIntervals.size() > 0) {
			if (QuizOptions.getOptions().isConstantRoot()) {
				lastA = QuizOptions.getOptions().getConstantRoot();
			} else {
				lastA = Note.getRandomStandardNote();
			}
			lastB = lastA.getHigher(getRandomInterval());
			playNotes(lastA, lastB);
			super.playNew();
		} else {
			JOptionPane.showMessageDialog(QuizFrame.getTheFrame(), "Please pick at least one interval");
		}
	}
	
	@Override
	public void repeat() {
		playNotes(lastA, lastB);
		super.repeat();
	}

	public void playNotes(Note noteA, Note noteB) {
		Chord chord = new Chord();
		chord.addNote(noteA);
		chord.addNote(noteB);
		MusicPlayer.playChord(chord, QuizOptions.getOptions().getIntervalDelay());
	}
	
	public int getCurrentInterval() {
		if (lastB != null && lastA != null) {
			return lastA.distanceTo(lastB); 
		} else {
			return -1;
		}
	}
	
	private String getIntervalName(int interval) {
		return INTERVAL_NAMES[interval];
	}
	
	private void answerInterval(int interval) {
		if (lastA != null && lastB != null) {
			if (interval == getCurrentInterval()) {
				answerCorrect(getIntervalName(interval));
			} else {
				answerWrong();
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class IntervalDemoButton extends JButton {
		public IntervalDemoButton(int interval, String label) {
			super("hear");
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				if (lastB != null && lastA != null) {
					playNotes(lastA, lastA.getHigher(interval));
				} else {
					playNotes(Note.C, Note.C.getHigher(interval));
				}
			});
		}
	}

	@SuppressWarnings("serial")
	private class IntervalAnswerButton extends JButton {
		public IntervalAnswerButton(int interval, String label, int key) {
			super(String.format("%s (%s)", label, (char)key));
			setFocusable(false);
			setMnemonic(key);
			addActionListener((ActionEvent e) -> {
				answerInterval(interval);
			});
		}
	}
}
