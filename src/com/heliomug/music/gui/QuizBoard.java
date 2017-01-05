package com.heliomug.music.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.heliomug.music.Chord;
import com.heliomug.music.ChordShape;
import com.heliomug.music.MidiPlayer;
import com.heliomug.music.Note;

@SuppressWarnings("serial")
public abstract class QuizBoard extends JPanel {
	private static final int MIDI_CHANNEL = 1;
	private static final int SUSTAIN_TIME = 3000; // milliseconds duration
	private static final int INTERVAL_TIME = 500; // milliseconds duration
	

	private int attempted;
	private int correct;
	
	private boolean isLastAttempted;

	private Thread noteMuteThread;
	
	public QuizBoard() {
		attempted = 0;
		correct = 0;
		isLastAttempted = false;

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (e.isShiftDown()) {
						repeat();
					} else {
						playNew();
					}
				}
			}
		});
		setFocusable(true);
	

		
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		JPanel subpanel = new JPanel();
		subpanel.add(new JButton("Play") {
			{
				setFocusable(false);
				addActionListener((ActionEvent e) -> {
					playNew();
				});
			}
		});
		subpanel.add(new JButton("Repeat") {
			{
				setFocusable(false);
				addActionListener((ActionEvent e) -> {
					repeat();
				});
			}
		});
		panel.add(subpanel, BorderLayout.WEST);
		panel.add(new JLabel("Score") {
			@Override
			public void paint(Graphics g) {
				String scoreString;  
				if (attempted > 0) {
					scoreString = String.format("Score: %d/%d (%.1f%%)", correct, attempted, correct * 100.0 / attempted);
				} else {
					scoreString = "Score: -";
				}
				setText(scoreString);
				super.paint(g);
			}
		}, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
	}
	
	public void playNew() {
		isLastAttempted = false;
	}
	
	public void repeat() {
	}
	
	public void answerCorrect() {
		if (!isLastAttempted) {
			attempted++;
			correct++;
		}
		isLastAttempted = true;
		repaint();
	}
	
	public void answerWrong() {
		if (!isLastAttempted) {
			attempted++;
		}
		isLastAttempted = true;
		repaint();
	}
	
    public void playChord(Chord chord) {
        Chord toPlay = ChordShape.fillChord(chord);
    	// System.out.println(toPlay);

    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        toPlay.play(MIDI_CHANNEL, 100);
	        try {
	            Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }

    public void playInParallel(Note a, Note b) {
    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        a.play(MIDI_CHANNEL, 100);
	        b.play(MIDI_CHANNEL, 100);
	        try {
	            Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }
    
    public void playInSeries(Note a, Note b) {
    	if (noteMuteThread != null) {
    		noteMuteThread.interrupt();
	        MidiPlayer.notesOff(1);
    	}
    	
    	noteMuteThread = new Thread(() -> {
	        a.play(MIDI_CHANNEL, 100);
	        try {
	            Thread.sleep(INTERVAL_TIME);
		        b.play(MIDI_CHANNEL, 100);
		        Thread.sleep(SUSTAIN_TIME);
		        MidiPlayer.notesOff(MIDI_CHANNEL);
		        noteMuteThread = null;
	        } catch (InterruptedException e) {
	        	// no problem, chord doesn't need to 
	        	// shut itself down because it was done above.  
	        	// we just don't want to cut off new chord.  
	        }
    	});
    	
    	noteMuteThread.start();
    }
    
}
