package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class UpdatingCheckBox extends JCheckBox {
	Supplier<Boolean> source;
	
	public UpdatingCheckBox(String title, Consumer<Boolean> dest, Supplier<Boolean> source) {
		super(title);
		
		this.source = source;

		setFocusable(false);
		
		addActionListener((ActionEvent e) -> {
			dest.accept(isSelected());
		});
	}
	
	@Override
	public void paint(Graphics g) {
		this.setSelected(source.get());
		super.paint(g);
	}
}
