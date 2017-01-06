package com.heliomug.music.gui;

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

	private static final int ANS_PENDING = 0;
	private static final int ANS_RIGHT = 1;
	private static final int ANS_WRONG = -1;
	
	private int attempted;
	private int correct;
	
	private boolean isLastAttempted;
	private int isLastCorrect;

	public TabPanel() {
	    attempted = 0;
		correct = 0;
		isLastAttempted = true;
		isLastCorrect = ANS_PENDING;

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
		
		add(getOptionPanel(), BorderLayout.WEST);
		add(getResponsePanel(), BorderLayout.EAST);
		add(getControlPanel(), BorderLayout.SOUTH);
	}
	
	public abstract JPanel getOptionPanel();
	public abstract JPanel getResponsePanel();
	
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
		subpanel.add(makeScoreLabel(), BorderLayout.CENTER);
		subpanel.add(new JLabel("???") {
			@Override
			public void paint(Graphics g) {
				if (isLastAttempted) {
					setText(" ");
				} else {
					setText("???");
				}
				super.paint(g);
			}
		}, BorderLayout.EAST);
		panel.add(subpanel, BorderLayout.NORTH);

		subpanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				if (isLastCorrect == ANS_RIGHT) {
					setBackground(COLOR_CORRECT);
				} else if (isLastCorrect == ANS_PENDING) {
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
	
	private JLabel makeScoreLabel() {
		return new JLabel("Score") {
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
		isLastAttempted = false;
		isLastCorrect = ANS_PENDING;
		repaint();
	}
	
	public void repeat() {
		//isLastAttempted = true;
	}
	
	
	public void answerCorrect() {
		if (!isLastAttempted) {
			attempted++;
			correct++;
		}
		isLastAttempted = true;
		isLastCorrect = ANS_RIGHT;
		repaint();
	}
	
	public void answerWrong() {
		if (!isLastAttempted) {
			attempted++;
		}
		isLastAttempted = true;
		isLastCorrect = ANS_WRONG;
		repaint();
	}
}
