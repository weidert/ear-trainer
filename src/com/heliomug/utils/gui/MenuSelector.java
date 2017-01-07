package com.heliomug.utils.gui;

import java.util.List;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuSelector<T> extends JMenuItem {
	public MenuSelector(String title, List<T> items) {
		super(title);
	}
}
