package uk.co.codemachine.mazerun;

public class Chunk {

	private DIR side;
	private int pack;
	public Chunk() {
		side = DIR.RDOWN;
	}

	public int getPack() {
		return pack;
	}

	public void setPack(int pack) {
		this.pack = pack;
	}

	public DIR getSide() {
		return side;
	}

	public void setSide(DIR side) {
		this.side = side;
	}

	public String toString() {
		return side.prop;
	}
}
