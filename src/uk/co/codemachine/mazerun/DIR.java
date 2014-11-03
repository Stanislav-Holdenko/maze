package uk.co.codemachine.mazerun;

public enum DIR {
	DOWN("__"), RDOWN("_|"), RIGHT(" |"), SINGLE("|"), BLANK(""), EMPTY("  "); 
	String prop;
	private DIR(String prop){
		this.prop = prop;
	}
}