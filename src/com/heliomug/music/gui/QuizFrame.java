package com.heliomug.music.gui;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class QuizFrame extends JFrame {
	private static final long serialVersionUID = -8687531351328013961L;

	private static final String FRAME_TITLE = "Note Quizzer";

	public static QuizFrame theFrame; 
	
	public QuizOptions options;
	
	private QuizFrame() {
		super(FRAME_TITLE);
		
		options = new QuizOptions();

        setFocusable(false);
        
        setJMenuBar(QuizMenuBar.getBar());
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(getMainPanel());
		pack();
	}
	
	public static QuizFrame getTheFrame() {
		if (theFrame == null) {
			theFrame = new QuizFrame();
		} 
		return theFrame;
	}
	
	public static QuizOptions getOptions() {
		return getTheFrame().options;
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
		
		return tabbedPane;
	}
	

}
