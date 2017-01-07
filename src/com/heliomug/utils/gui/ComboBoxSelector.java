package com.heliomug.utils.gui;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class ComboBoxSelector<T> extends JComboBox<T> {
	@SuppressWarnings("unchecked")
	public ComboBoxSelector(List<T> options, Consumer<T> toDo) {
		super((T[]) options.toArray());
		addActionListener((ActionEvent e) -> {
			T option = (T) e.getSource();
			toDo.accept(option);
		});
	}
}
