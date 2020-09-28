/*
 *  Program 1: Minesweeper
 * 
 *  @author - Brent Rawls
 *  @version  Jan 28, 2019
 *  
 *  Cell class:
 *  This class represents one space on the Minesweeper gameboard.
 *  
 */
package edu.unca.csci202;

public class Cell {

	private boolean hasMine;
	private char displayOnBoard;
	private char displaySecretContents;
	private static int asciiOffset = '0';
	
	public Cell() {
		displayOnBoard = '-';
		displaySecretContents = '0';
		hasMine = false;
	}
	
	public void placeMine() {
		hasMine = true;
		displaySecretContents = 'M';
	}
	
	/* @param adjacentMines This is the number of mines in adjacent cells, calculated by Gameboard.
	 */
	public void setNearbyMines(int adjacentMines) {
		displaySecretContents = (char)(adjacentMines + asciiOffset);
	}
	
	public void resetCell() {
		hasMine = false;
		displayOnBoard = '-';
		displaySecretContents = '0';
	}
	
	/* @return boolean - true if this Cell contains a mine.
	 */
	public boolean isAMine() {
		return hasMine;
	}

	/* @return char - the "public" character displayed on the board, ~ if not guessed yet.
	 */
	public char getDisplayCharacter() {
		return displayOnBoard;
	}
	
	/* @return char - the character displayed if the board is correctly guessed.
	 */
	public char getSecretCharacter() {
		return displaySecretContents;
	}
	
	/* @return boolean - true if the guess was correct, false if incorrect.
	 */
	public boolean guess(boolean guessMine) {
		displayOnBoard = displaySecretContents;
		return (guessMine == hasMine);
	}
}
