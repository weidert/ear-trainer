package com.heliomug.music.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.heliomug.music.Note;

public class QuizInterval extends QuizBoard {
	private static final long serialVersionUID = 1825689907037878532L;
	
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
			

	private Note lastA;
	private Note lastB;
	
	private boolean isParallel;
	
	public QuizInterval() {
		super();
		
		JPanel panel;
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		PPButton butt;
		ButtonGroup group  = new ButtonGroup();
		butt = new PPButton("In Parallel", true);
		panel.add(butt);
		group.add(butt);
		butt = new PPButton("In Series", false);
		panel.add(butt);
		group.add(butt);
		
		add(panel, BorderLayout.WEST);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0 ; i <= 12 ; i++) {
			panel.add(new IntervalSelector(i, INTERVAL_NAMES[i]));
		}
		add(panel, BorderLayout.EAST);
	}
	
	
	
	public void playNew() {
		lastA = Note.getRandomStandardNote();
		lastB = lastA.getHigher((int)(Math.random() * 13));
		if (isParallel) {
			playInParallel(lastA, lastB);
		} else {
			playInSeries(lastA, lastB);
		}
		super.playNew();
	}
	
	@Override
	public void repeat() {
		if (isParallel) {
			playInParallel(lastA, lastB);
		} else {
			playInSeries(lastA, lastB);
		}
	}

	public int getCurrentInterval() {
		if (lastB != null && lastA != null) {
			return lastA.distanceTo(lastB); 
		} else {
			return -1;
		}
	}
	
	private void answerInterval(int interval) {
		if (lastA != null && lastB != null) {
			if (interval == getCurrentInterval()) {
				answerCorrect();
			} else {
				answerWrong();
			}
		}
	}
	
	private class PPButton extends JRadioButton {
		public PPButton(String label, boolean isParOn) {
			super(label);
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				isParallel = isParOn;
			});
		}
	}
	
	private class IntervalSelector extends JButton {
		public IntervalSelector(int interval, String label) {
			super(String.format("%s (%d)", label, interval));
			setFocusable(false);
			addActionListener((ActionEvent e) -> {
				answerInterval(interval);
			});
		}
	}
}
