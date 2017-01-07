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
		setFocusable(false);
		addActionListener((ActionEvent e) -> {
			JComboBox<T> box = (JComboBox<T>) e.getSource();
			T option = (T) (box.getSelectedItem());
			toDo.accept(option);
		});
	}
}
