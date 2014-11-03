package uk.co.codemachine.mazerun;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Generator {
	private List<Chunk> maze = new LinkedList<Chunk>();
	private List<Chunk> temp = new LinkedList<Chunk>();
	private int sizeX, sizeY, counter;
	private Random rnd = new Random();

	public Generator(int x, int y) {
		sizeX = x;
		sizeY = y;
		generate(sizeX, sizeY);
	}

	private void generate(int sizeX, int sizeY) {

		// GENERATION FRAME
		for (int i = 0; i < (sizeX * sizeY); i++) {
			maze.add(new Chunk());
			if (maze.size() <= sizeX)
				maze.get(i).setSide(DIR.DOWN);
			if ((i % sizeX) == 0) {
				maze.get(i).setSide(DIR.SINGLE);
				maze.get(i).setPack(1);
			}
		}
		maze.get(0).setSide(DIR.BLANK);
		for (int i = 1; i < (sizeX); i++) {
			maze.get(i).setPack(i);
		}
		// for(int row=1; row<sizeX; row++){
		// for(int i=sizeX*row; i<(sizeX*(row+1)); i++){
		// First line generation
		for (int i = sizeX; i < sizeX + sizeX; i++) {
			if ((i % sizeX) == 0) {

				continue;
			}
			if ((i % sizeX) == sizeX - 1) { // Setting up the last chunk on
											// right side
				maze.get(i).setSide(DIR.RDOWN);
				if (maze.get(i - 1).getSide() == DIR.RDOWN || maze.get(i - 1).getSide() == DIR.RIGHT) {
					maze.get(i).setPack(maze.get(i - 1).getPack() + 1);
					makeExit(temp, i);
				} else {
					maze.get(i).setPack(maze.get(i - 1).getPack());
					makeExit(temp, i);
				}
				continue;
			}
			setBorder(i);
		}

		int line = 1;
//		for(int d=1; d<sizeX-2; d++){
		copyIntoNextLine(line);
		display();
		generateNext(line + 1);
		// }
		display();
		line++;
//		}
	}
	private void setBorder(int i) {
		Chunk chunk = maze.get(i);
		if (rnd.nextBoolean()) { // Randomly set the "|" (true - continue a row,
									// false - setting "|")
			if (maze.get(i - 1).getSide() == DIR.RDOWN) {
				chunk.setPack(maze.get(i - 1).getPack() + 1);
				chunk.setSide(DIR.DOWN);
				temp.add(chunk);
			} else {
				chunk.setSide(DIR.DOWN);
				chunk.setPack(maze.get(i - 1).getPack());
				temp.add(chunk);
			}
		} else {
			chunk.setSide(DIR.RDOWN);
			chunk.setPack(maze.get(i - 1).getPack() + 1);
			makeExit(temp, i);
	}	}


	
	private void copyIntoNextLine(int line) {
		for (int i = (sizeX * line); i < (sizeX * (line + 1)); i++) {
			maze.get(i + sizeX).setPack(maze.get(i).getPack());
			maze.get(i + sizeX).setSide(maze.get(i).getSide());
	}	}


	
	private void generateNext(int line) {
		// Remove "RIGHT" "RDOWM" and their pack numbers
		for (int i = (sizeX * line); i < (sizeX * (line + 1)); i++) {
			Chunk chunk = maze.get(i);
			if (i == (sizeX * (line + 1) - 1)) {
				chunk.setPack(0);
				chunk.setSide(DIR.RIGHT);
				continue;
			}
			if (chunk.getSide() == DIR.DOWN || chunk.getSide() == DIR.RDOWN
					|| chunk.getSide() == DIR.RIGHT && (i % sizeX) != 0) {
				chunk.setPack(0);
				chunk.setSide(DIR.EMPTY);
			}
		}
		// Filling pack numbers in empty chunks
		for (int i = (sizeX * line); i < (sizeX * (line + 1)); i++) {
			Chunk chunk = maze.get(i);
			if (chunk.getPack() > 0)
				continue;
			chunk.setPack(i);
			if(chunk.getPack() == maze.get(i+1).getPack()){
				chunk.setSide(DIR.RDOWN);
				makeExit(temp, i);
				}
			setBorder(i);
		}
	}

	// A random exit from particular pack, at least one chunk from pack should
	// have exit
	private void makeExit(List<Chunk> temp, int i) {
		if (temp.size() != 0) {
			int exit = rnd.nextInt(temp.size());
			if (temp.get(exit).getSide() == DIR.DOWN)
				temp.get(exit).setSide(DIR.EMPTY);
			if (temp.get(exit).getSide() == DIR.RDOWN)
				temp.get(exit).setSide(DIR.RIGHT);
			temp.clear();
		} else {
			maze.get(i).setSide(DIR.RIGHT);
		}
	}

	private void display() {
		for (int r = 0; r < (sizeX) * (sizeY); r++) {
			if ((r % sizeX) == 0)
				System.out.println();
			System.out.printf(maze.get(r).toString());
		}
	}
}
