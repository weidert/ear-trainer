package com.heliomug.music.gui;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class QuizMain extends JFrame {
	private static final long serialVersionUID = -8687531351328013961L;

	private static final String FRAME_TITLE = "Chord Quizzer";

	public QuizMain() {
		super(FRAME_TITLE);

        setFocusable(false);
        
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(getMainPanel());
		this.pack();
	}
	
	public JTabbedPane getMainPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.setFocusable(false);
		
		tabbedPane.addTab("Chord Type", null, new QuizType(), "Identify Chord Types");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_T);
		tabbedPane.addTab("Key Chords", null, new QuizKey(), "Identify Chords in Key");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_K);
		tabbedPane.addTab("Intervals", null, new QuizInterval(), "Identify Intervals");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_I);
		
		return tabbedPane;
	}
	

}
