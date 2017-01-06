package com.heliomug.music.quizzer;

import java.awt.EventQueue;

public class MainMusicGUI {
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
			QuizFrame board = QuizFrame.getTheFrame();
			board.setVisible(true);
		});
	}
}
