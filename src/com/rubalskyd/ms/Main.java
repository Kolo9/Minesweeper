package com.rubalskyd.ms;

import java.awt.EventQueue;
import java.util.Scanner;

public class Main {

	public static void main (String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoardGUI window = new BoardGUI();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		/*
		Scanner in = new Scanner(System.in);
		Board b = new Board(8, 8);
		int x, y;
		String cmd;
		while (!b.isGameOver()) {
			System.out.println(b.showCur());
			System.out.print("Next move: ");
			x = in.nextInt();
			y = in.nextInt();
			cmd = in.nextLine();
			if (cmd.contains("m")) {
				b.toggleMark(x, y);
			} else {
				b.moveAndGet(x, y);
			}
			System.out.println();
		}
		System.out.println(b);
		if (b.hasWon()) {
			System.out.println("You won!");
		} else {
			System.out.println("You lost!");
		}
		
		in.close();
		*/
	}
}
