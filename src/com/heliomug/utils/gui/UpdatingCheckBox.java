package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JCheckBox;

import com.heliomug.music.trainer.QuizOptions;

@SuppressWarnings("serial")
public class UpdatingCheckBox extends JCheckBox {
	public UpdatingCheckBox(String title, Consumer<Boolean> dest, Supplier<Boolean> source) {
		super(title);

		addActionListener((ActionEvent e) -> {
			dest.accept(isSelected());
		});
	}
	
	@Override
	public void paint(Graphics g) {
		this.setSelected(QuizOptions.getOptions().isGuitarChords());
		super.paint(g);
	}
}
