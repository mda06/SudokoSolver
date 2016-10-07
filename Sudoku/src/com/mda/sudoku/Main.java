package com.mda.sudoku;

import java.util.Scanner;

public class Main {
	private static Sudoku sud = new Sudoku();
	private static Scanner scan = new Scanner(System.in);
	
	private static boolean handleInput() {
		System.out.print("\nPlease enter value,x,y : ");
		String in = scan.nextLine();
		if(in.equals("stop")) return true;
		if(in.equals("solve")) {
			sud.solve();
			return false;
		}
		System.out.println();
		
		String[] split = in.split(",");
		if(split.length == 3) {
			try {
				int v = Integer.valueOf(split[0]), x = Integer.valueOf(split[1]), y = Integer.valueOf(split[2]);
				sud.put(v, x, y);
			} catch(Exception e) {
				System.err.println("Bad entry !");
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		while(true) {
			sud.print();
			if(handleInput()) break;
		}
	}
}
