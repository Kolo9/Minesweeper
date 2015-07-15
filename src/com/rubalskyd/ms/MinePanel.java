package com.rubalskyd.ms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rubalskyd.ms.Board.Point;

public class MinePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Board b;
	int height, width;
	JButton[][] grid;
	Color[][] colors;
	BoardGUI gui;
	
	public MinePanel(final BoardGUI gui, Board brd, JFrame frame) {
		this.gui = gui;
		b = brd;
		grid = new JButton[b.getHeight()][b.getWidth()];
		colors = new Color[b.getHeight()][b.getWidth()];
		
		width = b.getWidth();
		height = b.getHeight();
		
		setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
		setLayout(new GridLayout(height, width, 0, 0));
		
		Color[] color = {new Color(200, 200, 200), new Color(150, 150, 150)};
		int colorIndex = 0;
		// for every row
		for(int i = 0; i < height; i++) {
			// change color at each row
			colorIndex = i % 2;
			// for every column
			for(int j = 0; j < width; j++) {
				final JButton button = new JButton();
				button.setOpaque(true);
				colors[i][j] = color[colorIndex % 2];
				button.setBackground(colors[i][j]);  // use color 0 or 1
				button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 16));
				final int y = i;
				final int x = j;
		        button.addMouseListener(new MouseAdapter() {
		            public void mouseClicked(MouseEvent e) {
		            	if (!b.isGameOver()) {
		            		if (e.getButton() == 3) { // if right click
		            			b.toggleMark(x, y);
		            			if (b.isMarked(x, y)) button.setBackground(Color.RED);
		            			else button.setBackground(colors[y][x]);
		            			button.getModel().setPressed(false);
		            		} else if (button.getBackground() != Color.RED) {
								button.setText(b.moveAndGet(x, y));
								for (Point p: b.pointsToShowFromLastMove) {
									grid[p.y][p.x].setBackground(colors[p.y][p.x]);
									grid[p.y][p.x].setText(b.get(p.x, p.y));
								}
		            			button.getModel().setPressed(true);
		            		}
		            		
		            		if (b.isGameOver()) {
		            			if (b.hasWon()) {
		            				gui.gameOverLabel.setText("Game over, you won!");
		            			} else {
		            				gui.gameOverLabel.setText("Game over, you lost!");
		            			}
		            		}
		            	}
		            }
		        });
				grid[i][j] = button;
				add(button);
				colorIndex++;           // next color
			}
		}
	}
	
	public void reset(Board board) {
		gui.gameOverLabel.setText("");
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				grid[i][j].setText("");
				grid[i][j].setBackground(colors[i][j]);
				b = board;
			}
		}
	}
}
