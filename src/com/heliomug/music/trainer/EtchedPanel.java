package com.heliomug.music.trainer;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class EtchedPanel extends JPanel {
	public EtchedPanel(String title) {
		super();
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, title);
		setBorder(border);
	}
}
