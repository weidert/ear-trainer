package com.heliomug.music.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordType;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;

public class ChordBoard extends JFrame {
	private static final long serialVersionUID = 3177499161338508351L;

	public ChordBoard() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(getPanel());
		this.pack();
	}
	
	public JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(ChordType.values().length, Note.NOTE_NAMES.length));
		
		for (ChordType type : ChordType.values()) {
			for (String name : Note.NOTE_NAMES) {
				Chord chord = type.constructChord(Note.getNote(name));
				panel.add(new ChordButton(chord));
			}
		}
		
		return panel;
	}
	
	private class ChordButton extends JButton {
		private static final long serialVersionUID = 8613034120318621229L;

		public ChordButton(Chord chord) {
			super(chord.getShortName());
			addActionListener((ActionEvent e) -> {
				Thread t = new Thread(() -> {
					playChord(chord);
				});
				t.start();
			});
		}
	}
	
    public static void playChord(Chord chord) {
        System.out.println(chord);
        chord.play(1, 100);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
        System.out.println("off");
        MidiPlayer.notesOff(1);
    }
}
