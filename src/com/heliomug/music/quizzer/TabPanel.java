package com.heliomug.music.quizzer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class TabPanel extends JPanel {
	private static final Color COLOR_CORRECT = Color.GREEN;
	private static final Color COLOR_WRONG = Color.RED;
	private static final Color COLOR_NEUTRAL = Color.GRAY;

	private static final int NOT_STARTED = -2;
	private static final int ANS_PENDING = 0;
	private static final int ANS_RIGHT = 1;
	private static final int ANS_WRONG = -1;
	
	private int attempted;
	private int correct;
	
	private int lastCorrect;
	private String lastAnswer;

	public TabPanel() {
	    attempted = 0;
		correct = 0;
		lastCorrect = NOT_STARTED;

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
		
		JPanel options = getOptionPanel();
		if (options != null) add(options, BorderLayout.WEST);
		add(getResponsePanel(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.SOUTH);
		add(getActualStatusPanel(), BorderLayout.NORTH);
	}
	
	public void blur() {}
	public void focus() {}
	public abstract JPanel getStatusPanel();
	public abstract JPanel getOptionPanel();
	public abstract JPanel getResponsePanel();

	private JPanel getActualStatusPanel() {
		JPanel panel = new EtchedPanel("");
		panel.setLayout(new BorderLayout());
		panel.add(makeScoreLabel(), BorderLayout.NORTH);
		panel.add(getStatusPanel(), BorderLayout.CENTER);
		panel.add(new JLabel("hey"), BorderLayout.SOUTH);
		return panel;
	}
	
	public QuizOptions getOptions() {
		return QuizOptions.getOptions();
	}
	
	private JPanel getControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel subpanel;
		
		subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		JPanel subsubpanel;
		subsubpanel = new JPanel();
		subsubpanel.setLayout(new GridLayout(1, 0));
		subsubpanel.setBackground(Color.RED);
		subsubpanel.add(new JButton("Play New") {
			{
				setFocusable(false);
				setMnemonic(KeyEvent.VK_P);
				addActionListener((ActionEvent e) -> {
					playNew();
				});
			}
		});
		subsubpanel.add(new JButton("Repeat") {
			{
				setFocusable(false);
				setMnemonic(KeyEvent.VK_R);
				addActionListener((ActionEvent e) -> {
					repeat();
				});
			}
		});
		subpanel.add(subsubpanel, BorderLayout.WEST);
		subpanel.add(new JLabel("???") {
			@Override
			public void paint(Graphics g) {
				if (lastCorrect == ANS_WRONG || lastCorrect == NOT_STARTED) {
					setText("  ");
				} else if (lastCorrect == ANS_PENDING) {
					setText("???");
				} else if (lastCorrect == ANS_RIGHT) {
					setText(lastAnswer);
				}
				super.paint(g);
			}
		}, BorderLayout.EAST);
		panel.add(subpanel, BorderLayout.NORTH);

		subpanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				if (lastCorrect == ANS_RIGHT) {
					setBackground(COLOR_CORRECT);
				} else if (lastCorrect == ANS_PENDING || lastCorrect == NOT_STARTED) {
					setBackground(COLOR_NEUTRAL);
				} else {
					setBackground(COLOR_WRONG);
				}
				super.paint(g);
			}
		};
		panel.add(subpanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public JLabel makeScoreLabel() {
		return new JLabel("Score: -") {
			@Override
			public void paint(Graphics g) {
				String scoreString;  
				if (attempted > 0) {
					double percent = correct * 100.0 / attempted;
					String fmt = "Score: %d/%d (%.1f%%)";
					scoreString = String.format(fmt, correct, attempted, percent);
				} else {
					scoreString = "Score: -";
				}
				setText(scoreString);
				super.paint(g);
			}
		};		
	}
	
	public void playNew() {
		lastCorrect = ANS_PENDING;
		repaint();
	}
	
	public void repeat() {
		//isLastAttempted = true;
	}
	
	
	public void answerCorrect(String answer) {
		if (lastCorrect == ANS_PENDING) {
			attempted++;
			correct++;
		}
		lastCorrect = ANS_RIGHT;
		repaint();
	}
	
	public void answerWrong() {
		if (lastCorrect == ANS_PENDING) {
			attempted++;
		}
		lastCorrect = ANS_WRONG;
		repaint();
	}
}
