package uk.co.codemachine.mazerun;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Generator {
	private List<Chunk> maze = new LinkedList<Chunk>();
	private List<Chunk> temp = new LinkedList<Chunk>();
	private int sizeX, sizeY, counter=1;
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
		// First line generation
		for (int i = sizeX+1; i < sizeX + sizeX; i++) {

			// Setting up the last chunk on right side
			if ((i % sizeX) == sizeX - 1) {
//				maze.get(i).setSide(DIR.RDOWN);
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

		
		for(int line = 1; line<sizeX-1; line++){
		copyIntoNextLine(line);

		generateNext(line + 1);}
		displayP();
		display();
	}

	private void setBorder(int i) {
		Chunk chunk = maze.get(i);
		
		if((i % sizeX) == sizeX - 1){
			
				if (maze.get(i - 1).getSide() == DIR.RDOWN || maze.get(i - 1).getSide() == DIR.RIGHT) {
					chunk.setPack(maze.get(i - 1).getPack() + 1);
					chunk.setSide(DIR.RIGHT);
					temp.add(chunk);
				}
				if (maze.get(i - 1).getSide() == DIR.DOWN){
					chunk.setPack(maze.get(i - 1).getPack());
					chunk.setSide(DIR.RDOWN);
					makeExit(temp, i);
				}
				if (maze.get(i - 1).getSide() == DIR.EMPTY){
					chunk.setPack(maze.get(i - 1).getPack());
					chunk.setSide(DIR.RDOWN);
//					makeExit(temp, i);
				}
				
				return;
		}
		// Randomly set the "|"  
		if (rnd.nextBoolean()) { 	//continue a row
			if (maze.get(i - 1).getSide() == DIR.RDOWN || maze.get(i - 1).getSide() == DIR.RIGHT) {
				chunk.setPack(maze.get(i - 1).getPack() + 1);
				chunk.setSide(DIR.DOWN);
				temp.add(chunk);
			} else {
				chunk.setSide(DIR.DOWN);
				chunk.setPack(maze.get(i - 1).getPack());
				temp.add(chunk);
			}
		} else {					//setting "|"
			if (maze.get(i - 1).getSide() == DIR.SINGLE){
				chunk.setSide(DIR.RIGHT);
			}
			if (maze.get(i - 1).getSide() == DIR.RDOWN || maze.get(i - 1).getSide() == DIR.RIGHT) {
				chunk.setSide(DIR.RDOWN);
				chunk.setPack(maze.get(i - 1).getPack() + 1);
				makeExit(temp, i);
			} else {
				chunk.setSide(DIR.RDOWN);
				chunk.setPack(maze.get(i - 1).getPack());
				makeExit(temp, i);
			}
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


	private void generateNext(int line) {
		// Remove "RIGHT" "RDOWM" and their pack numbers while keeping exit pack numbers 
		for (int i = (sizeX * line)+1; i < (sizeX * (line + 1)); i++) {
			Chunk chunk = maze.get(i);
		// Setting the last one	in a row
			if (i == (sizeX * (line + 1) - 1)) {
				setBorder(i);
			}
			if(chunk.getSide() == DIR.RDOWN || chunk.getSide() == DIR.RIGHT)
				counter++;
		// Making exit
			if (chunk.getSide() == DIR.DOWN || chunk.getSide() == DIR.RDOWN
					&& (i % sizeX) != 0) {
				chunk.setPack(counter);
				chunk.setSide(DIR.EMPTY);
			}
			setBorder(i);
		}
//		 displayP();
//		 display();
		// Filling pack numbers in empty chunks
//		for (int i = (sizeX * line); i < (sizeX * (line + 1)); i++) {
//			Chunk chunk = maze.get(i);
//			if (chunk.getPack() > 0)
//				continue;
//		//	chunk.setPack(i);
//		// Cheking if the next chunk is exit in previous row
//			if (chunk.getPack() == maze.get(i + 1).getPack()) {
////				chunk.setSide(DIR.RDOWN);
//				makeExit(temp, i);
//			}
//			
//		}
		
	}


	private void copyIntoNextLine(int line) {
		for (int i = (sizeX * line); i < (sizeX * (line + 1)); i++) {
			maze.get(i + sizeX).setPack(maze.get(i).getPack());
			maze.get(i + sizeX).setSide(maze.get(i).getSide());
		}
	}

	private void display() {
		for (int r = 0; r < (sizeX) * (sizeY); r++) {
			if ((r % sizeX) == 0)
				System.out.println();
			System.out.printf(maze.get(r).toString());
		}
	}

	private void displayP() {
		for (int r = 0; r < (sizeX) * (sizeY); r++) {
			if ((r % sizeX) == 0)
				System.out.println();
			System.out.printf(String.valueOf(maze.get(r).getPack()));
		}
		System.out.println();
	}
}
