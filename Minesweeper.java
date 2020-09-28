/*
 *  Program 1: Minesweeper
 * 
 *  @author - Brent Rawls
 *  @version  Jan 28, 2019
 * 
 *  Main class:
 *  This class creates an instance of the Gameboard and calls its run() method.
 *  
 */

package edu.unca.csci202;

public class Minesweeper {

	/*
	 * This is the main method, which creates an instance of Gameboard and calls run().
	 * @return nothing.
	 */
	public static void main(String[] args) {
				
		Gameboard game = new Gameboard();	
		game.run();

	}

}
