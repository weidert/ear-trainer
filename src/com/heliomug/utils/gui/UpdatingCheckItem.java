package com.heliomug.utils.gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JCheckBoxMenuItem;

@SuppressWarnings("serial")
public class UpdatingCheckItem extends JCheckBoxMenuItem {
	private Supplier<Boolean> source;
	
	public UpdatingCheckItem(String title, Consumer<Boolean> toDo, Supplier<Boolean> source) {
		super(title);
		this.source = source;
		this.setFocusable(false);
		
		addActionListener((ActionEvent e) -> toDo.accept(isSelected()));
	}
	
	@Override
	public void paint(Graphics g) {
		this.setSelected(source.get());
		super.paint(g);
	}
}
