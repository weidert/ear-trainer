package com.heliomug.music.trainer;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

public class QuizFrame extends JFrame {
	private static final long serialVersionUID = -8687531351328013961L;
	
	private static final String FRAME_TITLE = "Ear Trainer";

	public static QuizFrame theFrame; 
	
	public TabPanel oldPanel;
	
	private QuizFrame() {
		super(FRAME_TITLE);
		
		oldPanel = null;

        setFocusable(false);
        
        setJMenuBar(QuizMenuBar.getTheBar());
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(getMainPanel());
		pack();
	}
	
	public static void quit() {
		MusicPlayer.kill();
		System.exit(0);
	}
	
	public static QuizFrame getTheFrame() {
		if (theFrame == null) {
			theFrame = new QuizFrame();
		} 
		return theFrame;
	}
	
	private JTabbedPane getMainPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.setFocusable(false);
		
		tabbedPane.addTab("Chord Type", null, new TabType(), "Identify Chord Types");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_T);
		tabbedPane.addTab("Key Chords", null, new TabKey(), "Identify Chords in Key");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_K);
		tabbedPane.addTab("Intervals", null, new TabInterval(), "Identify Intervals");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_I);
		
		tabbedPane.addChangeListener((ChangeEvent e) -> {
			if (oldPanel != null) {
				oldPanel.blur();
			}
			oldPanel = (TabPanel)tabbedPane.getSelectedComponent();
			oldPanel.focus();
		});
		
		return tabbedPane;
	}
	

}
