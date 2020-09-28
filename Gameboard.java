/*
 *  Program 1: Minesweeper
 * 
 *  @author - Brent Rawls
 *  @version  Jan 28, 2019
 * 
 *  Gameboard class:
 *  This class creates a 2D array of Cells and manages the game by interpreting user commands.
 *  This includes creating a board with user-specified dimensions, populating it with mines
 *  in random locations, and checking whether user guesses are correct.
 */

package edu.unca.csci202;

import java.util.Random;
import java.util.Scanner;

public class Gameboard {

	private static int defaultBoardHeight = 8;
	private static int defaultBoardWidth = 8;
	private static int defaultNumberOfMines = 10;
	private static int asciiOffset = '0';
	private static int asciiA = 'A';
	private static int minWidth = 8;
	private static int maxWidth = 60;
	private static int minHeight = 8;
	private static int maxHeight = 26;
	private static int minMines = 10;
	private static int maxMines = 100;
	
	private boolean playing = true;
	private boolean gameInProgress = false;
	private boolean peekMode = false;
	private int boardHeight, boardWidth, numberOfMines;
	private Cell[][] board; 
	private Random mineRandomizer = new Random();
	private Scanner scan = new Scanner(System.in);
	private String command;
	private int numberOfMinesFound;
	
	/* Gameboard constructor (default). 8x8 board with 10 mines.
	 */
	public Gameboard() {
		boardHeight = defaultBoardHeight;	
		boardWidth = defaultBoardWidth;
		numberOfMines = defaultNumberOfMines;
	}	
	
	/* Gameboard constructor.
	 * @param boardHeight - vertical dimension of board
	 * @param boardWidth - horizontal dimension of board
	 * @param numberOfMines - how many mines to place on the board
	 */
	public Gameboard(int boardHeight, int boardWidth, int numberOfMines) {
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.numberOfMines = numberOfMines;
		board = new Cell[boardHeight][boardWidth];	
	}
	
	/* Create a new Cell array, initialize with "blank" Cells.
	 */
	private void newBoard() {
		board = new Cell[boardHeight][boardWidth];
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				board[i][j] = new Cell();
			}
		}
		gameInProgress = true;
	}
	
	/* Randomly place mines in the board, checking to ensure no overlap.
	 */
	private void placeMines() {
		int minesOnBoard = 0;
		int nextX = 0;
		int nextY = 0;
		while (minesOnBoard < numberOfMines) {
			do {
				nextX = mineRandomizer.nextInt(boardWidth);
				nextY = mineRandomizer.nextInt(boardHeight);				
			} while (board[nextY][nextX].isAMine());
			board[nextY][nextX].placeMine();
			minesOnBoard++;
		}
		numberOfMinesFound = 0;
	}
	
	/* For each Cell on the board, if it doesn't contain a mine, count mines in neighbor Cells
	 * and set secret contents to ASCII character of this value.
	 */
	private void countMines() {
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				if(board[i][j].isAMine()) continue;
				
				int nearbyMines = 0;
				for(int a = -1; a <= 1; a++) {
					if(i + a < 0 || i + a >= boardHeight) continue;
					for(int b = -1; b <= 1; b++) {
						if(j + b < 0 || j + b >= boardWidth) continue;
						if(board[i + a][j + b].isAMine()) nearbyMines++;
					}
				}
				board[i][j].setNearbyMines(nearbyMines);
				
			}
		}
	}
	
	/* Draw the board in beautiful ASCII graphics.
	 */
	private void drawBoard() {
		if(boardWidth >= 10) {
			System.out.print("\t");
			for(int i = 0; i < boardWidth; i++) {
				if(i >= 10) System.out.print(i / 10 + " ");
				else System.out.print("  ");
			}	
			if(peekMode) {
				System.out.print("\t");
				for(int i = 0; i < boardWidth; i++) {
					if(i >= 10) System.out.print(i / 10 + " ");
					else System.out.print("  ");
				}
			}
			System.out.println("");
		}
		System.out.print("\t");
		for(int i = 0; i < boardWidth; i++) System.out.print(i % 10 + " ");
		if(peekMode) {
			System.out.print("\t");
			for(int i = 0; i < boardWidth; i++) System.out.print(i % 10 + " ");
		}
		System.out.println("\n");
		for(int i = 0; i < boardHeight; i++) {
			char lineLabel = (char)(i + asciiA);
			System.out.print(lineLabel + ":\t");
			for(int j = 0; j < boardWidth; j++) {
				System.out.print(board[i][j].getDisplayCharacter() + " ");
			}
			if(peekMode) {
				System.out.print("\t");
				for(int j = 0; j < boardWidth; j++) {
					if(board[i][j].isAMine()) {
						System.out.print(board[i][j].getSecretCharacter() + " ");
					} else {
						System.out.print(board[i][j].getDisplayCharacter() + " ");
					}
				}
			}
			System.out.println("");
		}
		System.out.println("\nYou've found " + numberOfMinesFound + " mines, out of a total of " + numberOfMines + ".\n");			
	}
		
	/* Prompt user for new game dimensions and number of mines, then call functions to create the board.
	 */
	private void newGame() {
		System.out.println("Starting a new game!");
		do {
			System.out.println("Enter board width (min: " + minWidth + ". max: " + maxWidth + ".)");
			boardWidth = scan.nextInt();
		} 
		while (boardWidth < minWidth || boardWidth > maxWidth);
		do {
			System.out.println("Enter board height (min: " + minHeight + ". max: " + maxHeight + ".)");
			boardHeight = scan.nextInt();
		} 
		while (boardHeight < minHeight || boardHeight > maxHeight);
		maxMines = (boardWidth * boardHeight) / 4;
		do {
			System.out.println("Enter number of mines (min: " + minMines + ". max: " + maxMines + ".)");
			numberOfMines = scan.nextInt();
		} while (numberOfMines < minMines || numberOfMines > maxMines);
		
		newBoard();
		placeMines();
		countMines();
		drawBoard();
	}
	
	/* Restart the game with the same parameters as before.
	 */
	private void resetGame() {
		newBoard();
		placeMines();
		countMines();
		drawBoard();
	}

	/* Deal with a user command.
	 * @param command Most recent line of text entered by the user.
	 */
	private void parseCommand(String command) {
		if(command.equalsIgnoreCase("help") || command.charAt(0) == '?') {
			System.out.println("new\t- start a new game");
			System.out.println("peek\t- view contents of each cell (for debugging)");
			System.out.println("A1\t- guess that cell A1 is safe");
			System.out.println("A1!\t- guess that cell A1 has a mine");
			System.out.println("reset\t- start over with the same parameters");
			System.out.println("help\t- view this menu");
			System.out.println("quit\t- exit the program");
			System.out.println("");
			System.out.println("All commands are case-insensitive. Good luck, and try not to step on a mine!");
		}
		else if(command.equalsIgnoreCase("quit")) {
			playing = false;
			System.out.println("Thanks for playing Minesweeper!");			
		}
		else if(command.equalsIgnoreCase("new")) {
			newGame();			
		}
		else if(command.equalsIgnoreCase("reset")) {
			resetGame();			
		}
		else if(command.equalsIgnoreCase("peek")) {
			if(peekMode) {
				System.out.println("Peek mode deactivated.");
				peekMode = false;
			}
			else {
				System.out.println("Peek mode activated.");
				peekMode = true;
			}
			drawBoard();
		}
		else {
			guessCell(command.toUpperCase());
		}
	}
	
	/* Guess that a Cell has a mine, or not!
	 * @param command String that represents a cell address.
	 */
	private void guessCell(String command) {
		if(!gameInProgress) {
			System.out.println("Type \"new\" to start a new game, or \"?\" for help.");
			return;
		}
			
		int vertPosition = (int)command.charAt(0) - asciiA;
		if(vertPosition < 0 || vertPosition >= boardHeight) {
			System.out.println("...What? Unknown command.");
			return;
		}
		
		int horizPosition = 0;
		int commandPointer = 1;
		while(commandPointer < command.length() && Character.isDigit(command.charAt(commandPointer))){
			horizPosition *= 10;
			horizPosition += ((int)command.charAt(commandPointer) - asciiOffset);
			commandPointer++;
		}
		if(horizPosition < 0 || horizPosition >= boardWidth) {
			System.out.println("...What? Unknown command.");
			return;
		}

		if(board[vertPosition][horizPosition].getDisplayCharacter() == board[vertPosition][horizPosition].getSecretCharacter()) {
			System.out.println("That location has already been guessed!");
			return;
		}
			
		boolean guessedMine = command.charAt(command.length()-1) == '!';
		
		if(board[vertPosition][horizPosition].guess(guessedMine)) {
			if(board[vertPosition][horizPosition].isAMine()) {
				numberOfMinesFound++;
				if(numberOfMinesFound == numberOfMines) {
					youWin();
					return;
				} else {
					System.out.println("Correct! Mine found.");
				}
			}
			else if(board[vertPosition][horizPosition].getSecretCharacter() == '0') {
				revealNeighbors(vertPosition, horizPosition);
			}
		} else {
			youLose();
			return;
		}
		drawBoard();
	}
	
	/* If a cell had zero mines in neighboring cells, go ahead and reveal their neighbors (and so on, recursively).
	 * @param v vertical address of the cell
	 * @param h horizontal address of the cell
	 */
	private void revealNeighbors(int v, int h) {
		for(int a = -1; a <= 1; a++) {
			if(v + a < 0 || v + a >= boardHeight) continue;
			for(int b = -1; b <= 1; b++) {
				if(h + b < 0 || h + b >= boardWidth) continue;
				if (board[v + a][h + b].getDisplayCharacter() == '-') {
					board[v + a][h + b].guess(false);
					if(board[v + a][h + b].getSecretCharacter() == '0') {
						revealNeighbors(v + a, h + b);
					}
				}
			}
		}		
	}
	
	public void youWin() {
		gameInProgress = false;
		System.out.println("Congratulations! You've won the game. Nice going.");
		System.out.println("Type \"new\" or \"reset\" to play again, or \"quit\" to exit.");
		return;
	}
	
	public void youLose() {
		gameInProgress = false;
		System.out.println("Wrong move... your inevitable defeat has come to pass, I guess.");
		System.out.println("Type \"new\" or \"reset\" to play again, or \"quit\" to exit.");
		return;
	}
	
	public void run() {
		System.out.println("Welcome to Minesweeper!");
		newGame();
		
		System.out.println("Type a command, or '?' for help.");
		while(playing) {
			command = scan.next();
			parseCommand(command.trim());
			
		}
	}
	
	
}
