package com.heliomug.music.quizzer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.heliomug.music.Note;
import com.heliomug.music.StandardInstrument;
import com.heliomug.utils.Utils;
import com.heliomug.utils.gui.MenuSelector;

@SuppressWarnings("serial")
public class QuizMenuBar extends JMenuBar {
    private static final int NOTE_RANGE_START = 36;
  	private static final int NOTE_RANGE_END = 73; 
  	
  	private static final int[] INTERVAL_OPTIONS = new int[] {0, 100, 200, 400, 800};
  	private static final int[] ARPEGGIO_OPTIONS = new int[] {0, 5, 10, 20, 40, 80, 160, 320};
  	private static final int[] STRUM_OPTIONS = new int[] {0, 5, 10, 15, 20, 25, 30};
  	private static final int[] VOLUME_OPTIONS = new int[] {0, 1, 2, 5, 7, 10, 20, 40, 50, 70, 100};
    
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
		JCheckBoxMenuItem item;
				
		
		menu.add(getInstrumentMenu());

		item = new JCheckBoxMenuItem("Guitarify Chords"); 
		item.setSelected(QuizOptions.DEFAULT_IS_GUITAR_CHORDS);
		JCheckBoxMenuItem guit = item;
		item.addActionListener((ActionEvent e) -> {
			QuizOptions.getOptions().setGuitarChords(guit.getState());
		});
		item.setMnemonic(KeyEvent.VK_G);
		menu.add(item);

		menu.add(getDroneMenu());
		
		menu.add(getRootMenu());
		
		menu.add(getDelayMenu());
		
		return menu;
	}
	
	public JMenu getRootMenu() {
		JMenu menu = new JMenu("Root");
		menu.setMnemonic(KeyEvent.VK_R);
		JCheckBoxMenuItem item;
		
		item = new JCheckBoxMenuItem("Constant Root"); 
		item.setSelected(QuizOptions.DEFAULT_IS_CONSTANT_ROOT);
		JCheckBoxMenuItem root = item;
		item.addActionListener((ActionEvent e) -> {
			QuizOptions.getOptions().setConstantRoot(root.getState());
		});
		item.setMnemonic(KeyEvent.VK_C);
		menu.add(item);
		
		menu.add(new MenuSelector<Note>(
				"Root", 
				Note.getNoteRange(NOTE_RANGE_START, NOTE_RANGE_END), 
				(Note n) -> QuizOptions.getOptions().setConstantRoot(n),
				(Note n) -> n.longName().toString()
		));
		
		return menu;
	}
	
	public JMenu getInstrumentMenu() {
		JMenu menu = new JMenu("Instrument");
		JMenu submenu;
		
		submenu = new MenuSelector<StandardInstrument>(
				"Instrument",
				Arrays.asList(BASIC_INSTRUMENTS), 
				Arrays.asList(StandardInstrument.values()),
				(StandardInstrument instrument) -> QuizOptions.getOptions().setInstrument(instrument),
				(StandardInstrument instrument) -> instrument.getShortName()
		);
		menu.add(submenu);
		
		return menu;
	}
	
	public JMenu getDelayMenu() {
		JMenu menu = new JMenu("Delay Options");
		menu.setMnemonic(KeyEvent.VK_D);
		menu.add(new MenuSelector<Integer>(
				"Interval Delay", 
				Utils.toIntegerList(INTERVAL_OPTIONS), 
				(Integer delay) -> QuizOptions.getOptions().setIntervalDelay(delay)
		));
		menu.add(new MenuSelector<Integer>(
				"Strum Delay", 
				Utils.toIntegerList(STRUM_OPTIONS), 
				(Integer delay) -> QuizOptions.getOptions().setStrumDelay(delay)
		));
		menu.add(new MenuSelector<Integer>(
				"Arppegio Delay", 
				Utils.toIntegerList(ARPEGGIO_OPTIONS), 
				(Integer delay) -> QuizOptions.getOptions().setArpeggioDelay(delay)
		));
		return menu;
	}
	
	public JMenu getDroneMenu() {
		JMenu menu = new JMenu("Drone");
		menu.setMnemonic(KeyEvent.VK_D);

		JCheckBoxMenuItem item = new JCheckBoxMenuItem("Use Drone"); 
		item.setSelected(QuizOptions.DEFAULT_IS_DRONE_ON);
		JCheckBoxMenuItem drone = item;
		item.addActionListener((ActionEvent e) -> {
			QuizOptions.getOptions().setDroneOn(drone.getState());
		});
		item.setMnemonic(KeyEvent.VK_D);
		menu.add(item);
		
		MenuSelector<Integer> selector = new MenuSelector<>(
				"Drove Volume", 
				Utils.toIntegerList(VOLUME_OPTIONS), 
				(Integer vol) -> {
					QuizOptions.getOptions().setDroneVolume(vol);
				}
		);
		selector.setMnemonic(KeyEvent.VK_V);
		menu.add(selector);
		
		return menu;
	}
	
	public static QuizMenuBar getTheBar() {
		if (theBar == null) {
			theBar = new QuizMenuBar();
		} 
		return theBar;
	}
}
