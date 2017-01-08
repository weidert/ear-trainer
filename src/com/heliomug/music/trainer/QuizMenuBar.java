package com.heliomug.music.trainer;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.heliomug.music.Note;
import com.heliomug.music.StdInstrument;
import com.heliomug.utils.Utils;
import com.heliomug.utils.gui.MenuSelector;
import com.heliomug.utils.gui.UpdatingCheckItem;

@SuppressWarnings("serial")
public class QuizMenuBar extends JMenuBar {
    private static final int NOTE_RANGE_START = 36;
  	private static final int NOTE_RANGE_END = 73; 
  	
  	private static final int[] INTERVAL_OPTIONS = new int[] {0, 100, 200, 400, 800};
  	private static final int[] ARPEGGIO_OPTIONS = new int[] {0, 5, 10, 20, 40, 80, 160, 320};
  	private static final int[] STRUM_OPTIONS = new int[] {0, 5, 10, 15, 20, 25, 30};
  	private static final int[] VOLUME_OPTIONS = new int[] {0, 1, 2, 5, 7, 10, 20, 40, 50, 70, 100};
    
    private static final StdInstrument[] BASIC_INSTRUMENTS = new StdInstrument[] { 
        	StdInstrument.PIANO_GRAND,	
        	StdInstrument.PIANO_ELECTRIC_1,
        	StdInstrument.GUITAR_NYLON,
        	StdInstrument.GUITAR_STEEL, 
        	StdInstrument.GUITAR_OVERDRIVE,
        	StdInstrument.BANJO,
        	StdInstrument.ORGAN_CHURCH,
        	StdInstrument.ORGAN_ROCK,
    };
    
	private static QuizMenuBar theBar;
	
	private QuizMenuBar() {
		super();

		JMenu menu;
		JMenuItem item;
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		item = new JMenuItem("Exit", KeyEvent.VK_X);
		item.addActionListener((ActionEvent e) -> QuizFrame.quit());
		menu.add(item);
		add(menu);
		
		add(getOptionMenu());
		
		menu = new JMenu("About");
		menu.setMnemonic(KeyEvent.VK_A);
		item = new JMenuItem("About");
		item.addActionListener((ActionEvent e) -> {
			String message = "By Craig Weidert, 2017";
			javax.swing.JOptionPane.showMessageDialog(QuizFrame.getTheFrame(), message);
		});
		menu.add(item);
		add(menu);
	}
	
	public JMenu getOptionMenu() {
		JMenu menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);
		
		menu.add(getInstrumentMenu());

		UpdatingCheckItem item = new UpdatingCheckItem(
				"Guitarify Chords",
				(Boolean b) -> QuizOptions.getOptions().setGuitarChords(b),
				() -> QuizOptions.getOptions().isGuitarChords()
		);
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
		
		JCheckBoxMenuItem item = new JCheckBoxMenuItem("Constant Root") {
			@Override
			public void paint(Graphics g) {
				this.setSelected(QuizOptions.getOptions().isConstantRoot());
				super.paint(g);
			}
		}; 
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
		menu.setMnemonic(KeyEvent.VK_I);
		
		menu.add(new MenuSelector<StdInstrument>(
				"Instrument",
				Arrays.asList(BASIC_INSTRUMENTS), 
				Arrays.asList(StdInstrument.values()),
				(StdInstrument instrument) -> QuizOptions.getOptions().setInstrument(instrument),
				(StdInstrument instrument) -> instrument.getShortName()
		));
	
		menu.add(new MenuSelector<Integer>( 
				"Volume",
				Utils.toIntegerList(VOLUME_OPTIONS),
				(Integer volume) -> QuizOptions.getOptions().setVolume(volume)
		));
		
		return menu;
	}
	
	public JMenu getDelayMenu() {
		JMenu menu = new JMenu("Delay Options");
		menu.setMnemonic(KeyEvent.VK_Y);
		
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

		JCheckBoxMenuItem item = new JCheckBoxMenuItem("Use Drone") {
			@Override
			public void paint(Graphics g) {
				this.setSelected(QuizOptions.getOptions().isDroneOn());
				super.paint(g);
			}
		}; 
		item.setSelected(QuizOptions.DEFAULT_IS_DRONE_ON);
		JCheckBoxMenuItem drone = item;
		item.addActionListener((ActionEvent e) -> {
			QuizOptions.getOptions().setDroneOn(drone.getState());
		});
		item.setMnemonic(KeyEvent.VK_D);
		menu.add(item);
		
		menu.add(new MenuSelector<StdInstrument>(
				"Instrument",
				Arrays.asList(BASIC_INSTRUMENTS),
				Arrays.asList(StdInstrument.values()),
				(StdInstrument instrument) -> QuizOptions.getOptions().setDroneInstrument(instrument)
		));
		
		menu.add(new MenuSelector<Integer>(
				"Volume", 
				Utils.toIntegerList(VOLUME_OPTIONS), 
				(Integer vol) -> {
					QuizOptions.getOptions().setDroneVolume(vol);
				}
		));
		
		return menu;
	}
	
	public static QuizMenuBar getTheBar() {
		if (theBar == null) {
			theBar = new QuizMenuBar();
		} 
		return theBar;
	}
}
