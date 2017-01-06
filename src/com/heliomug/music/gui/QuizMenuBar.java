package com.heliomug.music.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;

@SuppressWarnings("serial")
public class QuizMenuBar extends JMenuBar {
    private static final int NOTE_RANGE_START = 36;
  	private static final int NOTE_RANGE_END = 73; 
    
    private static final StandardInstrument[] BASIC_INSTRUMENTS = new StandardInstrument[] { 
        	StandardInstrument.PIANO_GRAND,	
        	StandardInstrument.PIANO_ELECTRIC_1,
        	StandardInstrument.GUITAR_NYLON,
        	StandardInstrument.GUITAR_STEEL, 
        	StandardInstrument.GUITAR_OVERDRIVE,
        	StandardInstrument.BANJO,
        	StandardInstrument.ORGAN_CHURCH,
        	StandardInstrument.ORGAN_ROCK,
    };
    
	private static QuizMenuBar theBar;
	
	private QuizMenuBar() {
		super();

		JMenu menu;
		JMenuItem item;
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		item = new JMenuItem("Exit", KeyEvent.VK_X);
		item.addActionListener((ActionEvent e) -> System.exit(0));
		menu.add(item);
		add(menu);
		
		add(getOptionMenu());
		
	}
	
	public JMenu getOptionMenu() {
		JMenu menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		JMenu submenu, subsubmenu;
		JCheckBoxMenuItem item;
				
		
		submenu = new JMenu("Instrument");
		submenu.setMnemonic(KeyEvent.VK_I);
		for (StandardInstrument instrument : BASIC_INSTRUMENTS) {
			submenu.add(new InstrumentSelector(instrument));
		}
		subsubmenu = new JMenu("...");
		for (StandardInstrument instrument : StandardInstrument.values()) {
			if (!Arrays.asList().contains(instrument)) {
				subsubmenu.add(new InstrumentSelector(instrument));
			}
		}
		submenu.add(subsubmenu);
		menu.add(submenu);

		item = new JCheckBoxMenuItem("Guitarify Chords"); 
		item.setSelected(QuizOptions.DEFAULT_IS_GUITAR_CHORDS);
		JCheckBoxMenuItem guit = item;
		item.addActionListener((ActionEvent e) -> {
			QuizFrame.getOptions().setGuitarChords(guit.getState());
		});
		item.setMnemonic(KeyEvent.VK_G);
		menu.add(item);

		item = new JCheckBoxMenuItem("Use Drone"); 
		item.setSelected(QuizOptions.DEFAULT_IS_DRONE_ON);
		JCheckBoxMenuItem drone = item;
		item.addActionListener((ActionEvent e) -> {
			QuizFrame.getOptions().setDroneOn(drone.getState());
		});
		item.setMnemonic(KeyEvent.VK_D);
		menu.add(item);
		
		item = new JCheckBoxMenuItem("Constant Root"); 
		item.setSelected(QuizOptions.DEFAULT_IS_CONSTANT_ROOT);
		JCheckBoxMenuItem root = item;
		item.addActionListener((ActionEvent e) -> {
			QuizFrame.getOptions().setConstantRoot(root.getState());
		});
		item.setMnemonic(KeyEvent.VK_C);
		menu.add(item);
		
		submenu = new JMenu("Root");
		submenu.setMnemonic(KeyEvent.VK_R);
		for (Note n : Note.getNoteRange(NOTE_RANGE_START, NOTE_RANGE_END)) {
			submenu.add(new NoteSelector(n));
		}
		menu.add(submenu);
		
		return menu;
	}
	
	public static QuizMenuBar getBar() {
		if (theBar == null) {
			theBar = new QuizMenuBar();
		} 
		return theBar;
	}
	
	private class NoteSelector extends JMenuItem {
		public NoteSelector(Note n) {
			super(n.longString());
			addActionListener((ActionEvent e) -> {
				QuizFrame.getOptions().setConstantRoot(n);
			});
		}
	}

	private class InstrumentSelector extends JMenuItem {
		public InstrumentSelector(StandardInstrument instrument) {
			super(instrument.getShortName());
			addActionListener((ActionEvent e) -> {
				QuizFrame.getOptions().setInstrument(instrument);
			});
		}
	}
}
