package com.rubalskyd.ms;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BoardGUI {

	private JFrame frame;
	private MinePanel minePanel;
	private Board board;
	private boolean isInitialized = false;
	JLabel gameOverLabel;

	/**
	 * Create the application.
	 */
	public BoardGUI() {
		frame = new JFrame();
		frame.setBounds(700, 300, 410, 410);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.board = new Board(5, 5);
		System.out.println(board);

		frame.getContentPane().add(minePanel = new MinePanel(this, this.board, frame));
		
		if (!isInitialized) { //Don't need anymore, eh
			final JButton startOverBtn = new JButton("Start Over");
			startOverBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			startOverBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					board = new Board(board.getWidth(), board.getHeight());
					minePanel.reset(board);
					System.out.println(board);
				}
			});
			
			gameOverLabel = new JLabel();
			gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			frame.getContentPane().add(gameOverLabel);
			frame.getContentPane().add(startOverBtn);
			isInitialized = true;
		}
		frame.pack();
	}
	
	public JFrame getFrame() {
		return frame;
	}

}
