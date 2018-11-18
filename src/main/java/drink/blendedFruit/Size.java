package drink.blendedFruit;

public enum Size {
	
	SMALL(3), MEDIUM(6), LARGE(9);

	private final String name;
	private final int factor;

	Size(int factor) {
		this.name = this.name();
		this.factor = factor;
	}

	public int getFactor() {
		return this.factor;
	}

	public String toString() {
		return this.name;
	}

}
