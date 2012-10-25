package model;

public enum State {
	EMPTY("0"), SHIP("P"), NEAR_SHIP("V"), MISS(":"), HIT("X");
	private String symbol;

	State(String symbol) {
		this.symbol = symbol;
	}

	public String toString() {
		return symbol;
	}
}
