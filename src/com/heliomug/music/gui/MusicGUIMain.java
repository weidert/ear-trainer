package com.heliomug.music.gui;

import java.awt.EventQueue;

public class MusicGUIMain {
	public static void main(String[] args) {
		showQuizBoard();
	}
	
	public static void showChordBoard() {
		EventQueue.invokeLater(() -> {
			ChordBoard board = new ChordBoard();
			board.setVisible(true);
		});
	}
	
	public static void showQuizBoard() {
		EventQueue.invokeLater(() -> {
			QuizMain board = new QuizMain();
			board.setVisible(true);
		});
	}
}
